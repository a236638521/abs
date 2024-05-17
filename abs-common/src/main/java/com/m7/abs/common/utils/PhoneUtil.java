package com.m7.abs.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberToCarrierMapper;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.google.i18n.phonenumbers.geocoding.PhoneNumberOfflineGeocoder;
import com.m7.abs.common.constant.common.CarrierTypeEnum;
import com.m7.abs.common.domain.base.PhoneInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class PhoneUtil {
    private static PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

    private static PhoneNumberOfflineGeocoder geocoder = PhoneNumberOfflineGeocoder.getInstance();
    private static PhoneNumberToCarrierMapper carrierMapper = PhoneNumberToCarrierMapper.getInstance();

    /**
     * 根据国家代码和手机号 判断手机号是否有效
     *
     * @param phoneNumber 手机号码
     * @param countryCode 国号(区号)
     * @return true / false
     */
    public static boolean checkPhoneNumber(long phoneNumber, int countryCode) {
        Phonenumber.PhoneNumber pn = new Phonenumber.PhoneNumber();
        pn.setCountryCode(countryCode);
        pn.setNationalNumber(phoneNumber);
        return phoneNumberUtil.isValidNumber(pn);
    }

    private static Pattern phoneReg = Pattern.compile("\\+(9[976]\\d|8[987530]\\d|6[987]\\d|5[90]\\d|42\\d|3[875]\\d|2[98654321]\\d|9[8543210]|8[6421]|6[6543210]|5[87654321]|4[987654310]|3[9643210]|2[70]|7|1)\\d{1,14}$");

    public static PhoneInfo getPhoneNumberInfo(String phoneNumber) throws Exception {
        // 正则校验
        Matcher matcher = phoneReg.matcher(phoneNumber);
        if (!matcher.find()) {
            throw new Exception("The phone number maybe like:" + "+[National Number][Phone number], but got " + phoneNumber);
        }

        Phonenumber.PhoneNumber referencePhonenumber;
        try {
            String language = "CN";
            referencePhonenumber = phoneNumberUtil.parse(phoneNumber, language);
        } catch (NumberParseException e) {
            throw new Exception(e.getMessage());
        }
        String regionCodeForNumber = phoneNumberUtil.getRegionCodeForNumber(referencePhonenumber);

        if (regionCodeForNumber == null) {
            throw new Exception("Missing region code by phone number " + phoneNumber);
        }

        boolean checkSuccess = PhoneUtil.checkPhoneNumber(referencePhonenumber.getNationalNumber(), referencePhonenumber.getCountryCode());
        if (!checkSuccess) {
            throw new Exception("Not an active number:" + phoneNumber);
        }

        String description = geocoder.getDescriptionForNumber(referencePhonenumber, Locale.CHINA);
        int countryCode = referencePhonenumber.getCountryCode();
        long nationalNumber = referencePhonenumber.getNationalNumber();

        String nameForNumber = carrierMapper.getNameForNumber(referencePhonenumber, Locale.US);
        CarrierTypeEnum carrierTypeEnum = null;
        switch (nameForNumber) {
            case "China Unicom":
                carrierTypeEnum = CarrierTypeEnum.CHINA_UNICOM;
                break;
            case "China Mobile":
                carrierTypeEnum = CarrierTypeEnum.CHINA_MOBILE;
                break;
            case "China Telecom":
                carrierTypeEnum = CarrierTypeEnum.CHINA_TELECOM;
                break;
            default:
                carrierTypeEnum = CarrierTypeEnum.UNKNOWN;
        }

        PhoneInfo phoneInfo = PhoneInfo.builder()
                .regionCode(regionCodeForNumber)
                .countryCode(countryCode)
                .nationalNumber(nationalNumber)
                .description(description)
                .number(String.valueOf(countryCode) + nationalNumber)
                .fullNumber(phoneNumber)
                .carrier(carrierTypeEnum)
                .build();

        return phoneInfo;

    }

    /**
     * 调用360接口识别手机号归属地
     *
     * @param phone
     * @return
     */
    public static Map<String, String> getPhoneInfoFrom360(String phone) {
        StringBuilder r = new StringBuilder();
        try {
            //360URL
            URL url = new URL("https://cx.shouji.360.cn/phonearea.php?number=" + phone);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            PrintWriter out = null;
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(3000);
            conn.setRequestProperty("accept", "*/*");
            conn.setDoOutput(true);
            out = new PrintWriter(conn.getOutputStream());
            out.flush();
            InputStream is = conn.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "gb2312"));
            String result = "";
            while ((result = br.readLine()) != null) {
                r.append(result);
            }
            is.close();
            conn.disconnect();
        } catch (Exception e) {
            log.error("360获取号码归属地失败", e);
        }
        String province;
        String city;
        String sp;
        try {
            //360返回结果处理
            JSONObject resultObj = JSONObject.parseObject(r.toString());
            JSONObject data = (JSONObject) resultObj.get("data");
            province = ascii2native(data.get("province").toString());
            city = ascii2native(data.get("city").toString());
            sp = ascii2native(data.get("sp").toString());
        } catch (Exception e) {
            log.error("处理360返回结果失败", e);
            province = "";
            city = "";
            sp = "";
        }
        Map<String, String> map = new HashMap<>();
        map.put("location", province + city);
        map.put("operator", sp);
        return map;
    }


    /**
     * 360返回来的中文为unicode,需要转换
     */
    private static String ascii2native(String asciiCode) {
        if (StringUtils.isEmpty(asciiCode)) {
            return asciiCode;
        }

        String[] asciis = asciiCode.split("\\\\u");
        String nativeValue = asciis[0];
        try {
            for (int i = 1; i < asciis.length; i++) {
                String code = asciis[i];
                nativeValue += (char) Integer.parseInt(code.substring(0, 4), 16);
                if (code.length() > 4) {
                    nativeValue += code.substring(4, code.length());
                }
            }
        } catch (NumberFormatException e) {
            return asciiCode;
        }
        return nativeValue;
    }


    public static PhoneInfo getPhoneInfo(String phoneNumber, int countryCode) throws Exception {
        boolean checkSuccess = PhoneUtil.checkPhoneNumber(Long.valueOf(phoneNumber), countryCode);
        if (checkSuccess) {
            PhoneInfo phoneInfo = null;
            try {
                phoneInfo = PhoneUtil.getPhoneNumberInfo("+" + countryCode + phoneNumber);
            } catch (Exception e) {
                log.error("谷歌解析手机号失败:{}", phoneNumber, e);
            }
            if (phoneInfo != null && !"中国".equals(phoneInfo.getDescription())) {
                return phoneInfo;
            } else {
                if (phoneInfo == null) {
                    phoneInfo = new PhoneInfo();
                }
                if (StringUtils.isNotEmpty(phoneNumber)) {
                    String phoneNum = "";
                    if (phoneNumber.startsWith("+86")) {
                        phoneNum = phoneNumber.substring(3);
                    } else {
                        phoneNum = phoneNumber;
                    }
                    Map<String, String> phoneInfoFrom360 = getPhoneInfoFrom360(phoneNum);
                    if (phoneInfoFrom360 != null) {
                        phoneInfo.setDescription(phoneInfoFrom360.get("location"));
                        String operator = phoneInfoFrom360.get("operator");
                        CarrierTypeEnum carrierTypeEnum = null;
                        switch (operator) {
                            case "联通":
                                carrierTypeEnum = CarrierTypeEnum.CHINA_UNICOM;
                                break;
                            case "移动":
                                carrierTypeEnum = CarrierTypeEnum.CHINA_MOBILE;
                                break;
                            case "电信":
                                carrierTypeEnum = CarrierTypeEnum.CHINA_TELECOM;
                                break;
                            default:
                                carrierTypeEnum = CarrierTypeEnum.UNKNOWN;
                        }
                        phoneInfo.setCarrier(carrierTypeEnum);
                        return phoneInfo;
                    }
                }
            }
        }
        return null;
    }

    public static void main(String[] args) throws Exception {
        String testMobileNumber = "19858846235";
        int countryCode = 86;
        PhoneInfo phoneInfo = getPhoneInfo(testMobileNumber, countryCode);
        Map<String, String> phoneInfoFrom360 = getPhoneInfoFrom360(testMobileNumber);
        log.info("手机信息:{}", FastJsonUtils.toJSONString(phoneInfo));
        log.info("手机信息:{}", FastJsonUtils.toJSONString(phoneInfoFrom360));
    }


}
