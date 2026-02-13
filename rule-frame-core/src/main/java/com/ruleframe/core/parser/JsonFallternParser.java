package com.ruleframe.core.parser;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * json数据扁平化解析器
 */
public class JsonFallternParser {

    public static Map<String, String> parserToMap(JsonNode resultJsonNode) {
        // 首先判断节点数据类型，然后确定节点处理方式
        PaserResult pr = objectNodeParser(resultJsonNode);
        Map<String, String> result = pr.fieldMap;
        pr.arrayMap.forEach((k, v) -> arrayNodeParser(v, k).forEach(result::put));
        // 这一步会将导致对象逃逸，所以result对象，或者说 pr中的这个字段 不会进行栈上分配
        return result;
    }

    /**
     * object类型的jsonnode节点处理数据
     * 
     * @param jn ObjectJson类型的数据
     * @return {fieldMap:字符串结果，arrayMap:集合类型的数据的JsonNode}
     */
    public static PaserResult objectNodeParser(JsonNode jn) {
        if (!jn.isObject()) {
            throw new IllegalArgumentException("JsonNode数据不是对象类型");
        }
        PaserResult pr = new PaserResult(new HashMap<>(), new HashMap<>());
        Iterator<Entry<String, JsonNode>> itr = jn.fields();
        while (itr.hasNext()) {
            Entry<String, JsonNode> field = itr.next();
            JsonNode curNode = field.getValue();
            if (curNode.isObject()) {
                PaserResult npr = objectNodeParser(curNode);
                npr.fieldMap.forEach((k, v) -> pr.fieldMap.put(field.getKey() + "/" + k, v));
                npr.arrayMap.forEach((k, v) -> pr.arrayMap.put(field.getKey() + "/" + k, v));
                continue;
            }
            if (curNode.isArray()) {
                pr.arrayMap.put(field.getKey(), curNode);
                continue;
            }
            pr.fieldMap.put(field.getKey(), curNode.asText());
        }
        return pr;
    }

    public static Map<String, String> arrayNodeParser(JsonNode jn, String key) {
        if (!jn.isArray()) {
            throw new IllegalArgumentException("JsonNode数据不是集合类型");
        }
        Map<String, String> resultMap = new HashMap<>();
        Iterator<JsonNode> elements = jn.elements();
        int count = -1;
        while (elements.hasNext()) {
            count += 1;
            JsonNode curNode = elements.next();
            String itemKey = key + "[" + count + "]";
            if (curNode.isObject()) {
                PaserResult opr = objectNodeParser(curNode);
                // 所有键值对直接转换为结果
                opr.fieldMap.forEach((k, v) -> resultMap.put(itemKey + "/" + k, v));
                // 集合类型的数据持续处理
                opr.arrayMap.forEach((k, v) -> resultMap.putAll(arrayNodeParser(v, itemKey + "/" + k)));
                continue;
            }
            if (curNode.isArray()) {
                arrayNodeParser(curNode, itemKey).forEach(resultMap::put);
                continue;
            }
            resultMap.put(itemKey, curNode.asText());
        }
        return resultMap;
    }

    public record PaserResult(Map<String, String> fieldMap, Map<String, JsonNode> arrayMap) {
    }

    public static void main(String[] args) {
        String jsonStr = "{\"invoiceCode\":\"0001\",\"invoiceTime\":\"2025-10-21\",\"details\":[{\"code\":\"1\",\"name\":\"测试数据\",\"obj\":{\"info\":\"你好世界\",\"array\":[\"难顶\"]}},{\"code\":\"1\",\"name\":\"测试数据\"},\"测试数据\",\"测试数据01\"]}";
        try {
            JsonNode jsonResultNode = new ObjectMapper().readTree(jsonStr);
            parserToMap(jsonResultNode).forEach((k, v) -> System.out.println(k + ": " + v));
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
