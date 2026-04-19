package com.ruleframe.core.fact;


import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ruleframe.config.parser.JsonFallternParser;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 映射数据取值器
 * 涉及：JSON
 */
@AllArgsConstructor
@Slf4j
public class MapFactContext implements FactContext {

    private final String json;
    private final Map<String,String> PARSER_MAP;

    public MapFactContext(String jsonResult) {
        json = jsonResult;
        try {
            PARSER_MAP  = JsonFallternParser.parserToMap(jsonResult);
        } catch (JsonProcessingException e) {
            log.error(json);
            throw new RuntimeException("解析json结果失败");
        }
    }

    @Override
    public Object getValue(String path) {
        if (PARSER_MAP.isEmpty()) {
            return null;
        }
        return PARSER_MAP.get(path);
    }

    @Override
    public Set<String> getFactNames() {
        return Collections.unmodifiableSet(PARSER_MAP.keySet());
    }

    @Override
    public boolean hasFact(String name) {
        return PARSER_MAP.containsKey(name);
    }
    
    
    
}
