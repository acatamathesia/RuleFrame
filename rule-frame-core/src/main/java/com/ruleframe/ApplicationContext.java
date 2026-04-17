package com.ruleframe;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import com.ruleframe.core.element.ConfigurableElement;
import com.ruleframe.core.element.Element;
import com.ruleframe.core.element.ElementValue;
import com.ruleframe.core.fact.FactContext;
import com.ruleframe.core.fact.MapFactContext;
import com.ruleframe.resolver.MapPathResolver;

public class ApplicationContext {
    public static void main(String[] args) {
        String jsonStr = "{\"invoiceCode\":\"0001\",\"invoiceTime\":\"2025-10-21\",\"details\":[{\"code\":\"1\",\"name\":\"测试数据\",\"obj\":{\"info\":\"你好世界\",\"array\":[\"难顶\"]}},{\"code\":\"1\",\"name\":\"测试数据\"},\"测试数据\",\"测试数据01\"]}";
        FactContext factContext = new MapFactContext(jsonStr);
        System.out.println(factContext.getFactNames());
        // 创建配置解析元素对象
        Element element = new ConfigurableElement("账号编码", "details[*]/code", "to_bigdecimal", new MapPathResolver());
        ElementValue elementValue = element.resolve(factContext);
        System.out.println(elementValue);
        System.out.println("转换后的类型: " + elementValue.getClzz());

        // 使用新增的 isType 方法判断类型
        if (elementValue.isType(List.class)) {
            System.out.println("这是一个 List 类型");

            // 使用 getValueAs 方法安全地转换为 List
            List<?> list = elementValue.getValueAs(List.class);
            System.out.println("集合大小: " + list.size());

            // 判断 List 中的元素类型
            if (!list.isEmpty()) {
                Object firstElement = list.get(0);
                Class<?> elementType = firstElement.getClass();
                System.out.println("List 中元素的类型: " + elementType.getName());

                // 如果是 BigDecimal 类型，可以进一步转换
                if (elementValue.isType(List.class) && elementType == BigDecimal.class) {
                    @SuppressWarnings("unchecked")
                    List<BigDecimal> bigDecimalList = (List<BigDecimal>) list;
                    System.out.println("转换为 List<BigDecimal>: " + bigDecimalList);
                }
            }
        }
    }
}
