package com.tengo.core.config;

/**
 * @author dx
 * @date 2026/3/21
 */
public interface KeyConf {

    /** session 存储 key: TENGO_SSO_SESSION:{userId} */
    String PREFIX = "TENGO_SSO_SESSION:";

    /** 用户会话索引 key: TENGO_SSO_USER:{userId}:{sessionId} → tokenKey */
    String USER_SESSION_INDEX = "TENGO_SSO_USER:";

    String COOKIES = "Cookies";
    String AUTHORIZATION = "Authorization";

    String SEPARATOR = "@";
}
