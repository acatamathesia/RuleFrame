package com.ruleframe.core.param;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class FieldConvertResult {

    private String strResult;
    private Boolean boolResult;
    private BigDecimal decimalResult;

}
