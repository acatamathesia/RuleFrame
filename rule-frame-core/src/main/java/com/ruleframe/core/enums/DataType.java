package com.ruleframe.core.enums;

import java.util.Arrays;
import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DataType {
    CLASS_STRING("string", "字符串类型的数据"),
    CLASS_NUMBER("number", "数字类型的数据"),
    CLASS_BOOLEAN("boolean", "条件类型的数据");

    private String dataTypeCode;
    private String dataTypeName;

    public static Optional<DataType> select(String dataTypeCode) {
        return Arrays.stream(DataType.values())
                .filter(p -> p.getDataTypeCode().equals(dataTypeCode))
                .findFirst();
    }

}
