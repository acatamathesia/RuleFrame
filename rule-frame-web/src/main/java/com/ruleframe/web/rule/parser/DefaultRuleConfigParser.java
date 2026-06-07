package com.ruleframe.web.rule.parser;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruleframe.core.condition.Condition;
import com.ruleframe.core.condition.LeafCondition;
import com.ruleframe.core.condition.builder.ConditionBuilder;
import com.ruleframe.core.element.ConfigurableElement;
import com.ruleframe.core.element.Element;
import com.ruleframe.core.group.RuleGroup;
import com.ruleframe.core.operator.Operator;
import com.ruleframe.core.operator.OperatorRegistry;
import com.ruleframe.core.rule.Rule;
import com.ruleframe.core.rule.RuleResult;
import com.ruleframe.resolver.MapPathResolver;
import com.ruleframe.web.rule.entity.RuleDef;
import com.ruleframe.web.rule.entity.RuleGroupDef;
import com.ruleframe.web.rule.entity.SetElement;
import com.ruleframe.web.rule.mapper.RuleDefMapper;
import com.ruleframe.web.rule.mapper.RuleGroupDefMapper;
import com.ruleframe.web.rule.mapper.SetElementMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultRuleConfigParser {

    private final RuleGroupDefMapper groupDefMapper;
    private final RuleDefMapper ruleDefMapper;
    private final SetElementMapper elementMapper;
    private final ObjectMapper objectMapper;

    public List<RuleGroup> parseByGroupCode(String groupCode) {
        LambdaQueryWrapper<RuleGroupDef> query = new LambdaQueryWrapper<>();
        query.eq(RuleGroupDef::getGroupCode, groupCode)
             .eq(RuleGroupDef::getDeleted, 0)
             .eq(RuleGroupDef::getStatus, 1);
        List<RuleGroupDef> groups = groupDefMapper.selectList(query);
        return groups.stream().map(this::buildGroup).toList();
    }

    public RuleGroup parseSingleRule(String ruleCode) {
        LambdaQueryWrapper<RuleDef> query = new LambdaQueryWrapper<>();
        query.eq(RuleDef::getRuleCode, ruleCode)
             .eq(RuleDef::getDeleted, 0)
             .eq(RuleDef::getStatus, 1);
        List<RuleDef> rules = ruleDefMapper.selectList(query);
        if (rules.isEmpty()) {
            throw new RuntimeException("规则不存在: " + ruleCode);
        }
        RuleDef rule = rules.get(0);
        Rule coreRule = buildRule(rule);
        RuleGroup group = new RuleGroup();
        group.setGroupId("single_" + ruleCode);
        group.setGroupName(rule.getRuleName());
        group.setRules(List.of(coreRule));
        group.setStrategy(RuleGroup.ExecutionStrategyEnum.ALL_MATCH);
        return group;
    }

    private RuleGroup buildGroup(RuleGroupDef groupDef) {
        LambdaQueryWrapper<RuleDef> query = new LambdaQueryWrapper<>();
        query.eq(RuleDef::getGroupId, groupDef.getId())
             .eq(RuleDef::getDeleted, 0)
             .eq(RuleDef::getStatus, 1)
             .orderByAsc(RuleDef::getPriority);
        List<RuleDef> ruleDefs = ruleDefMapper.selectList(query);

        List<Rule> rules = ruleDefs.stream().map(this::buildRule).toList();

        RuleGroup group = new RuleGroup();
        group.setGroupId(groupDef.getGroupCode());
        group.setGroupName(groupDef.getGroupName());
        group.setRules(rules);
        group.setStrategy(parseStrategy(groupDef.getStrategy()));
        return group;
    }

    private Rule buildRule(RuleDef ruleDef) {
        Condition condition = buildConditions(ruleDef.getConditionsJson());
        return Rule.builder()
            .id(ruleDef.getRuleCode())
            .name(ruleDef.getRuleName())
            .priority(ruleDef.getPriority())
            .conditionList(List.of(condition))
            .unifiedReturn(ruleDef.getUnifiedReturn() != null && ruleDef.getUnifiedReturn() == 1)
            .result(RuleResult.builder()
                .ruleId(ruleDef.getRuleCode())
                .ruleName(ruleDef.getRuleName())
                .failureResult(ruleDef.getResultMessage())
                .build())
            .build();
    }

    private Condition buildConditions(String conditionsJson) {
        if (conditionsJson == null || conditionsJson.isBlank()) {
            throw new RuntimeException("条件配置不能为空");
        }
        try {
            JsonNode root = objectMapper.readTree(conditionsJson);
            if (root.isArray()) {
                return parseConditionsArray(root);
            }
            if (root.isObject()) {
                JsonNode conditionsNode = root.path("conditions");
                if (conditionsNode.isArray()) {
                    return parseCompositeCondition(root);
                }
                throw new RuntimeException("条件JSON对象缺少有效的 conditions 数组");
            }
            throw new RuntimeException("条件配置必须是JSON对象或JSON数组");
        } catch (JsonProcessingException e) {
            throw new RuntimeException("条件JSON解析失败: " + e.getMessage(), e);
        }
    }

    private Condition parseConditionsArray(JsonNode arrayNode) {
        if (arrayNode.size() == 0) {
            throw new RuntimeException("条件数组不能为空");
        }
        ConditionBuilder builder = ConditionBuilder.create();
        boolean first = true;
        Iterator<JsonNode> it = arrayNode.elements();
        while (it.hasNext()) {
            JsonNode node = it.next();
            Condition cond = parseConditionNode(node);
            if (first) {
                builder.with(cond);
                first = false;
            } else {
                builder.and(cond);
            }
        }
        return builder.build();
    }

    private Condition parseConditionNode(JsonNode node) {
        String type = node.path("type").asText("leaf");
        if ("composite".equals(type) || node.has("conditions") || node.has("logicalOperator")) {
            return parseCompositeCondition(node);
        }
        return parseLeafCondition(node);
    }

    private Condition parseLeafCondition(JsonNode node) {
        String elementCode = node.path("elementCode").asText();
        if (elementCode.isEmpty()) {
            elementCode = node.path("element").asText();
        }
        String operatorSymbol = node.path("operator").asText();
        JsonNode expectedValueNode = node.path("expectedValue");
        if (expectedValueNode.isMissingNode()) {
            expectedValueNode = node.path("value");
        }
        String failureCode = node.path("failureCode").asText(null);
        String failureMessage = node.path("failureMessage").asText(null);

        if (elementCode.isEmpty() || operatorSymbol.isEmpty()) {
            throw new RuntimeException("叶子条件缺少必填字段 element/elementCode 或 operator");
        }

        LambdaQueryWrapper<SetElement> query = new LambdaQueryWrapper<>();
        query.eq(SetElement::getCode, elementCode)
             .eq(SetElement::getDeleted, 0);
        SetElement element = elementMapper.selectOne(query);
        if (element == null) {
            throw new RuntimeException("规则元素不存在: " + elementCode);
        }

        Element coreElement = ConfigurableElement.builder()
            .elName(element.getName())
            .elPath(element.getElPath())
            .convertName(element.getConvertType() != null ? "to_" + element.getConvertType() : null)
            .pathResolver(new MapPathResolver())
            .build();

        Operator operator = OperatorRegistry.getOperator(operatorSymbol);
        if (operator == null) {
            throw new RuntimeException("不支持的运算符: " + operatorSymbol);
        }

        Object expectedVal = convertExpectedValue(expectedValueNode);

        LeafCondition.LeafConditionBuilder leafBuilder = LeafCondition.builder()
            .element(coreElement)
            .operator(operator)
            .expectedValue(expectedVal)
            .simple(true);

        if (failureCode != null) {
            leafBuilder.failureCode(failureCode);
        }
        if (failureMessage != null) {
            leafBuilder.failureMessage(failureMessage);
        }

        return leafBuilder.build();
    }

    private Condition parseCompositeCondition(JsonNode node) {
        String logicalOperator = node.path("logicalOperator").asText("AND");
        JsonNode conditionsNode = node.path("conditions");
        if (!conditionsNode.isArray() || conditionsNode.size() == 0) {
            throw new RuntimeException("复合条件必须包含非空的 conditions 数组");
        }

        ConditionBuilder builder = ConditionBuilder.create();
        boolean first = true;
        Iterator<JsonNode> it = conditionsNode.elements();
        while (it.hasNext()) {
            JsonNode child = it.next();
            Condition childCond = parseConditionNode(child);
            if (first) {
                builder.with(childCond);
                first = false;
            } else {
                if ("OR".equalsIgnoreCase(logicalOperator)) {
                    builder.or(childCond);
                } else {
                    builder.and(childCond);
                }
            }
        }
        return builder.build();
    }

    private Object convertExpectedValue(JsonNode node) {
        if (node == null || node.isNull()) return null;
        if (node.isBoolean()) return node.asBoolean();
        if (node.isInt()) return node.asInt();
        if (node.isLong()) return node.asLong();
        if (node.isDouble()) return node.asDouble();
        if (node.isTextual()) return node.asText();
        return node.toString();
    }

    private RuleGroup.ExecutionStrategyEnum parseStrategy(String strategy) {
        if (strategy == null) return RuleGroup.ExecutionStrategyEnum.ALL_MATCH;
        return switch (strategy.toUpperCase()) {
            case "FIRST_FAIL" -> RuleGroup.ExecutionStrategyEnum.FIRST_FAIL;
            case "FIRST_SUCCESS" -> RuleGroup.ExecutionStrategyEnum.FIRST_SUCCESS;
            default -> RuleGroup.ExecutionStrategyEnum.ALL_MATCH;
        };
    }
}

