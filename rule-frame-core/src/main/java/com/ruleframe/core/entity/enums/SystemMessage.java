package com.ruleframe.core.entity.enums;

import java.util.function.Function;

import lombok.Getter;

@Getter
public enum SystemMessage {
    FORESEE_SYSTEM_EXCEPTION_MESSAGE("SYS_FORESEE", "SYS_FORESEE-"),
    CONDITION_SYSTEM_ERROR_MESSAGE("SYS_ERR_CONDITION", "SYS_ERR-条件执行器执行异常, 请联系管理员处理");

    private final String code;
    private final String message;
    private final Function<String, String> messageFunction;

    private SystemMessage(String code, String message) {
        this.code = code;
        this.message = message;
        this.messageFunction = item_msg -> this.message + item_msg;
    }

    public String getMessage(String item_msg) {
        return messageFunction.apply(item_msg);
    }
}
