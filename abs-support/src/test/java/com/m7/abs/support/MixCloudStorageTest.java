package com.m7.abs.support;

import com.m7.abs.support.core.storage.MixCloudStorage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Kejie Peng
 * @date 2023年 05月23日 11:15:32
 */
@SpringBootTest
public class MixCloudStorageTest {
    @Autowired
    private MixCloudStorage mixCloudStorage;

    @Test
    void test01() throws Exception {
        String fileUrl = "https://haier-bucket01.eos-zhengzhou-1.cmecloud.cn/MID_NUM/1631192745456701442/2023-05-23/20230523-113054_1631192745456701442_1684812654921_02023052311341911224076890.wav";
        String key = "20230523-113054_1631192745456701442_1684812654921_02023052311341911224076890.wav";
        List<String> list = new ArrayList<>();
        list.add("ydCloudStorage");
        mixCloudStorage.multipartUploadFile(fileUrl, key, list);
        System.out.println(fileUrl);
    }
}
