package com.ruleframe.resolver;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.ruleframe.core.element.ElementValue;
import com.ruleframe.core.fact.FactContext;

public class MapPathResolver implements PathResolver {
    @Override
    public ElementValue resolve(FactContext context, String path) {
        if (context == null) {
            throw new IllegalArgumentException("缺少FactContext");
        }
        // 去掉JSONPath的$.前缀，适配JsonFallternParser的扁平化键名
        String lookupPath = path.startsWith("$.") ? path.substring(2) : path;
        if (!lookupPath.contains("*")) {
            Object value = context.getValue(lookupPath);
            Class<?> valueType = value != null ? value.getClass() : null;
            return ElementValue.success(value, value, valueType);
        }
        // 会返回一个集合
        Object matchedValue = matchWithPathPattern(lookupPath, context);
        Class<?> valueType = matchedValue != null ? matchedValue.getClass() : null;
        return ElementValue.success(matchedValue, matchedValue, valueType);
    }

    /**
     * 使用路径模式匹配Map中的键
     * 
     * @param pattern 路径模式
     * @param pMap 扁平化后的Map
     * @return 匹配的值（单个值或列表）
     */
    private Object matchWithPathPattern(String pattern, FactContext context) {
        List<Object> matchedValues = new ArrayList<>();
        String patternRegex = convertPatternToRegex(pattern);
        Pattern regex = Pattern.compile(patternRegex);
        
        for (String factName : context.getFactNames()) {
            if (regex.matcher(factName).matches()) {
                matchedValues.add(context.getValue(factName));
            }
        }
        
        if (matchedValues.isEmpty()) {
            return null;
        }
        
        if (matchedValues.size() == 1) {
            return matchedValues.get(0);
        }
        
        return matchedValues;
    }
    
    /**
     * 将路径模式转换为正则表达式
     * 
     * @param pattern 路径模式
     * @return 正则表达式
     */
    private String convertPatternToRegex(String pattern) {
        StringBuilder regex = new StringBuilder();
        int i = 0;
        int len = pattern.length();
        
        while (i < len) {
            char c = pattern.charAt(i);
            
            if (c == '*') {
                if (i + 1 < len && pattern.charAt(i + 1) == '[') {
                    regex.append("\\[\\d+\\]");
                    i += 2;
                } else {
                    regex.append("[^/]+");
                    i++;
                }
            } else if (c == '[') {
                int closeBracket = pattern.indexOf(']', i);
                if (closeBracket != -1) {
                    String indexStr = pattern.substring(i + 1, closeBracket);
                    if (indexStr.equals("*")) {
                        regex.append("\\[\\d+\\]");
                    } else {
                        regex.append("\\[").append(indexStr).append("\\]");
                    }
                    i = closeBracket + 1;
                } else {
                    regex.append(Pattern.quote(String.valueOf(c)));
                    i++;
                }
            } else if (c == '/') {
                regex.append("/");
                i++;
            } else {
                int nextSpecial = len;
                for (int j = i + 1; j < len; j++) {
                    char ch = pattern.charAt(j);
                    if (ch == '*' || ch == '[' || ch == '/') {
                        nextSpecial = j;
                        break;
                    }
                }
                String segment = pattern.substring(i, nextSpecial);
                regex.append(Pattern.quote(segment));
                i = nextSpecial;
            }
        }
        
        return "^" + regex.toString() + "$";
    }
    
}

