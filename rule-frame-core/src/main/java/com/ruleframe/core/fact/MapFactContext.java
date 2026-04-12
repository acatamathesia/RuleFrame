package com.ruleframe.core.fact;


import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.ruleframe.config.parser.JsonFallternParser;

import lombok.AllArgsConstructor;

/**
 * 映射数据取值器
 * 涉及：JSON
 */
@AllArgsConstructor
public class MapFactContext implements FactContext {

    private final JsonNode jsonNode;

    @Override
    public Object getValue(String path) {
        if (path == null || path.trim().length() == 0){
            return null;
        }
        // */xx, xx[1]/xx, xx[*]/xx, xx[0]/*/xx
        Map<String, String> pMap = JsonFallternParser.parserToMap(jsonNode);
        return pMap.get(path);
    }
    
}
