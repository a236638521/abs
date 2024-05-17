package com.m7.abs.common.constant.error.code;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <h2>错误码 7 位</h2>
 * <h3>前 3 位表示模块</h3>
 * <ul>
 *   <li>000 作为保留值表示公共模块</li>
 *   <li>其他模块从第三位开始递增，如 001 表示 call-queue、002 表示 call-agent-status........、011 表示call-config-cache</li>
 * </ul>
 * <h3>后 4 位表示业务码</h3>
 * <ul>
 *     <li>业务码[0005, 0020) 作为公共错误码保留值，后续要加公共错误码在上面加</li>
 *     <li>从 0020 开始(包括0020) 递增</li>
 *     <li>不同模块错误码中的业务码可以相同</li>
 * </ul>
 * <p>
 * 为了避免错误码多了之后放在一个类里面太多，将每个模块拆分到不同类中，并通过本类访问。
 * 增加模块错误码时先添加一个仅允许包级访问的类，在其中调用{@link ErrorCodeConstant#ErrorCodeConstant(String, String)}
 * 或{@link ErrorCodeConstant#ErrorCodeConstant(String, String, String)} 创建常量，比如{@link AbsCommonErrorCodePart}，
 * 再在本类中使用{@link AbsCommonErrorCodePart} 这样的方式创建一个类
 * </p>
 *
 * @author zhuhf
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ErrorCodeConstant {
    private final static int MODULE_CODE_LENGTH = 3;
    private final static int BUSINESS_CODE_LENGTH = 4;
    private final static int ERROR_CODE_LENGTH = MODULE_CODE_LENGTH + BUSINESS_CODE_LENGTH;

    private final String code;
    @EqualsAndHashCode.Exclude
    private final String errMsg;


    ErrorCodeConstant(String moduleCode, String businessCode, String errMsg) {
        Assert.isTrue(StringUtils.hasLength(moduleCode) && moduleCode.length() == MODULE_CODE_LENGTH,
                "模块码必须为 " + MODULE_CODE_LENGTH + " 位");
        Assert.isTrue(StringUtils.hasLength(businessCode) && businessCode.length() == BUSINESS_CODE_LENGTH,
                "业务码必须为 " + BUSINESS_CODE_LENGTH + " 位");
        this.code = moduleCode + businessCode;
        this.errMsg = errMsg;
    }

    ErrorCodeConstant(String code, String errMsg) {
        Assert.isTrue(StringUtils.hasLength(code) && code.length() == ERROR_CODE_LENGTH,
                "错误码必须为 " + ERROR_CODE_LENGTH + " 位");
        this.code = code;
        this.errMsg = errMsg;
    }

    public static class CommonErrorCode implements AbsCommonErrorCodePart {
    }

    public static class AdminErrorCode implements AbsAdminErrorCodePart {
    }

    public static class ApiErrorCode implements AbsApiErrorCodePart {
    }

    public static class SupportErrorCode implements AbsSupportErrorCodePart {
    }



    /**
     * 打印错误码常量表格 Markdown 格式
     */
    public static void main(String[] args) {
        printMarkdownTable(true);
    }

    private static void printMarkdownTable(boolean printJavaVar) {
        Map<String, ErrorCodeConstant> errorCodeConstantMap = new HashMap<>();
        System.out.println("#### 错误码 7 位\n" +
                "\n" +
                "- 前 3 位表示模块\n" +
                "  - 000 作为保留值表示公共模块\n" +
                "  - 其他模块从第三位开始递增，如 001 表示 call-queue、002 表示 call-agent-status........、011 表示call-config-cache\n" +
                "\n" +
                "- 后 4 位表示业务码\n" +
                "  - 业务码[0005, 0020) 作为公共错误码保留值，后续要加公共错误码在上面加\n" +
                "  - 从 0020 开始(包括0020) 递增\n" +
                "  - 不同模块错误码中的业务码可以相同");
        Arrays.stream(ErrorCodeConstant.class.getClasses())
                .sorted((o1, o2) -> Objects.equals(o1, CommonErrorCode.class)
                        ? -1 : o1.getName().compareTo(o2.getName()))
                .forEach(clazz -> {
                    System.out.println("### 模块 " + clazz.getSimpleName());
                    System.out.println("| code | 说明 |" + (printJavaVar ? " Java 变量 |" : ""));
                    System.out.println("| :--- | :--- |" + (printJavaVar ? " :---|" : ""));
                    Arrays.stream(clazz.getFields())
                            .sorted((o1, o2) -> {
                                try {
                                    ErrorCodeConstant errorCode1 = (ErrorCodeConstant) o1.get(null);
                                    ErrorCodeConstant errorCode2 = (ErrorCodeConstant) o2.get(null);
                                    return errorCode1.getCode().compareTo(errorCode2.getCode());
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                    return 0;
                                }

                            })
                            .forEach(field -> {
                                try {
                                    ErrorCodeConstant errorCode = (ErrorCodeConstant) field.get(null);
                                    errorCodeConstantMap.computeIfPresent(errorCode.getCode(), (s, errorCodeConstant) -> {
                                        throw new RuntimeException(errorCodeConstantMap.get(errorCode.getCode()) + "和" + errorCode + "存在相同的错误码");
                                    });
                                    errorCodeConstantMap.put(errorCode.getCode(), errorCode);
                                    System.out.println("| " + errorCode.getCode() + " | " + errorCode.getErrMsg() +
                                            " |" + (printJavaVar ? (" " + field.getName() + " |") : ""));
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                            });
                });
    }
}
