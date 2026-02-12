package com.ruleframe.web.controller;

import lombok.Data;

import java.util.List;

@Data
public class ContextRequest {
    private String contextName;
    private List<String> ruleNames;
}