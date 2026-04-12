package com.ruleframe.resolver;

import com.ruleframe.core.element.ElementValue;
import com.ruleframe.core.fact.FactContext;

public class JsonPathResolver implements PathResolver {

    /**
        {
            "invoiceCode": "0001",
            "invoiceTime": "2025-10-21",
            "details": [
                {
                    "code": "1",
                    "name": "测试数据",
                    "obj": {
                        "info": "你好世界",
                        "array": [
                            "难顶"
                        ]
                    }
                },
                {
                    "code": "1",
                    "name": "测试数据"
                },
                "测试数据",
                "测试数据01"
            ]
        }
    */
    @Override
    public ElementValue resolve(FactContext context, String path) {
        if (context == null) {
            throw new IllegalArgumentException("缺少FactContext");
        }
        return ElementValue.success(context.getValue(path), null);
    }
    
}
