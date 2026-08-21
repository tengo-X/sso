package com.tengo.core.config;

/**
 * @author dx
 * @date 2026/3/21
 */
public interface KeyConf {

    /** session 存储 key: TENGO_SSO_SESSION:{userId} */
    String PREFIX = "TENGO_SSO_SESSION:";

    String COOKIES = "Cookies";
    String AUTHORIZATION = "Authorization";

    String SEPARATOR = "@";
}
