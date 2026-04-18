package com.ruleframe.core.condition;

import java.util.List;

import com.ruleframe.core.condition.visitor.ConditionVisitor;
import com.ruleframe.core.element.Element;
import com.ruleframe.core.element.ElementValue;
import com.ruleframe.core.entity.enums.SystemMessage;
import com.ruleframe.core.fact.FactContext;
import com.ruleframe.core.operator.Operator;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 叶子条件：包含元素、运算器、预期值
 * 
 * 预期值: 可能是一个值， 也可能是需要被提取的对象中的一种
 */
@Data
@Builder
@Slf4j
public class LeafCondition implements Condition {

    private Element element;
    private Operator operator;
    private boolean simple; // 判断比较对象是否是对象, true-基础数据, false-对象数据，从element中提取
    private Element expectedElement;
    private Object expectedValue;

    private String failureCode; // 失败码
    private String failureMessage; // 失败消息(可以是用{}模板替换)

    private void checkTargetExists() {
        if (element == null)
            throw new IllegalArgumentException("元素(element)不存在");
        if (operator == null)
            throw new IllegalArgumentException("运算器(operator)不存在");
        if (!simple && expectedElement == null)
            throw new IllegalArgumentException("预期值(expectedElement)不存在");
        if (simple && expectedValue == null)
            throw new IllegalArgumentException("预期值(expectedValue)不存在");
    }

    @Override
    public ConditionResult evaluate(FactContext ctx) {
        try {
            checkTargetExists();
            ElementValue elementValue = element.resolve(ctx);
            if (!simple) {
                log.info("预期值是事实模型(FactContext)中的对象的提取值");
                expectedValue = expectedElement.resolve(ctx).getConvertedValue();
                if (expectedValue instanceof List) {
                    return ConditionResult
                            .failure(SystemMessage.FORESEE_SYSTEM_EXCEPTION_MESSAGE.getMessage("预期值不能是列表"));
                }
            }
            boolean boolRes = operator.apply(elementValue.getConvertedValue(), expectedValue);
            if (boolRes)
                return ConditionResult.success();
            return ConditionResult.failure(buildFailureMessage(elementValue.getConvertedValue(), expectedValue));
        } catch (IllegalArgumentException e) {
            return ConditionResult.failure(SystemMessage.FORESEE_SYSTEM_EXCEPTION_MESSAGE.getMessage(e.getMessage()));
        } catch (Exception e) {
            log.error("条件执行器(Condition)执行异常", e);
            return ConditionResult.failure(SystemMessage.FORESEE_SYSTEM_EXCEPTION_MESSAGE.getMessage());
        }
    }

    private String buildFailureMessage(Object factValue, Object expectedValue) {
        if (factValue instanceof List) {
            return failureCode + "-" + failureMessage.replace("{factValue}", ((List<?>) factValue).toString())
                    .replace("{expectedValue}", expectedValue.toString());
        }
        return failureCode + "-" + failureMessage.replace("{factValue}", factValue.toString())
                .replace("{expectedValue}", expectedValue.toString());
    }

    @Override
    public <T> T accept(ConditionVisitor<T> visitor) {
        return visitor.visit(this);
    }

}