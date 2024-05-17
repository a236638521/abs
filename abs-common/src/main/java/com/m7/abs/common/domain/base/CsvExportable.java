package com.m7.abs.common.domain.base;

/**
 * @author Kejie Peng
 * @date 2023年 04月17日 10:08:32
 */
public interface CsvExportable {
    /**
     * @methodName        : outputTitleLine
     * @description        : 输出标题行字符串
     * @return            : 标题行字符串
     */
    public String[] outputCsvTitleLine();

    /**
     * @methodName        : outputDataLine
     * @description        : 输出数据行
     * @return            : 符合CSV格式的数据行字符串
     */
    public String[] outputCsvDataLine();

    /**
     * @param input : 输入字符串
     * @methodName        : CSVFormat
     * @description        : 将输入字符串格式化成CSV格式的字符串
     * @return            : 符合CSV格式的字符串
     */
    public static String CSVFormat(String input) {
        boolean bFound = false;
        //如果值中含有逗号、换行符、制表符（Tab）、单引号，双引号，则需要用双引号括起来；
        //如果值中包含双引号，则需要用两个双引号来替换。
        //正则匹配：",'\"\r\n\t"
        bFound = input.matches("(.*)(,|'|\"|\r|\n|\t)(.*)");
        if (bFound) {
            //如果存在匹配字符
            //先将双引号替换为两个双引号
            String sTemp = input.replaceAll("\"", "\"\"");
            //然后，两端使用"字符
            sTemp = "\"" + sTemp + "\"";
            return sTemp;
        }
        return input;
    }
}
