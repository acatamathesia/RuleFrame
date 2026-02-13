package com.ruleframe.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtil {

    public static final ObjectMapper om = new ObjectMapper();

    public static ObjectMapper getObjectMapper() {
        return om;
    }

}
