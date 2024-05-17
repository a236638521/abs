package com.m7.abs.admin.domain.vo.common;
/**
 * @author PKJ
 * @date 2018/5/17 0017 上午 11:16
 * @content
 **/

import lombok.*;

import java.util.List;

/**
 * @author pkj
 * @create 2018-05-17 上午 11:16
 **/
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MenuVO {
    private String id;
    private String name;
    private String url;
    private List<MenuVO> children;
}
