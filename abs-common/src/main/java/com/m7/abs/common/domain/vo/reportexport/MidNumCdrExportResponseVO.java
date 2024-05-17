package com.m7.abs.common.domain.vo.reportexport;

import com.m7.abs.common.domain.base.StorageFileBox;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 小号话单导出VO
 *
 * @author Kejie Peng
 * @date 2023年 03月22日 15:08:04
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MidNumCdrExportResponseVO {
    /**
     * 存储地址
     */
    private List<StorageFileBox> storageConf;
    /**
     * 对象存储key
     */
    private String key;
    /**
     * 文件名称
     */
    private String zipFileName;
    /**
     * 预计下载量
     */
    private Integer expectCount;
    /**
     * 账户编号
     */
    private String billAccountId;

}
