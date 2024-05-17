package com.m7.abs.admin.core.security;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CasUserDetails {
    private String id;
    private String name;
    private String email;
    private String mobile;
}
