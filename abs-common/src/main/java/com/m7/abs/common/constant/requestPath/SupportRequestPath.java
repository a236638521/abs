package com.m7.abs.common.constant.requestPath;

/**
 * @author KejiePeng
 * @Title:
 * @Description
 * @date 2021/12/30 14:34
 */
public class SupportRequestPath {

    /**
     * 推送数据
     */
    public static final String PUSH_DATA = "/push-data";
    /**
     * 重推数据
     */
    public static final String PUSH_DATA_RETRY = "/push-data/retry";
    /**
     * 文件转存接口
     */
    public static final String RECORDING_DUMP = "/save-to-oss";
    /**
     * 文件转存重试接口
     */
    public static final String RECORDING_DUMP_RETRY = "/save-to-oss/retry";
    /**
     * os获取文件预览链接
     */
    public static final String OS_GENERATE_PRE_SIGNED_URL = "/os/generatePreSignedUrl";
}
