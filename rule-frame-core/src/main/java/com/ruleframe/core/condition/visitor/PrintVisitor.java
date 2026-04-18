package com.ruleframe.core.condition.visitor;

import com.ruleframe.core.condition.CompositeCondition;
import com.ruleframe.core.condition.Condition;
import com.ruleframe.core.condition.LeafCondition;

/**
 * 条件树打印访问者
 * 以美观的树形结构输出条件层次关系
 */
public class PrintVisitor implements ConditionVisitor<String> {

    private static final String INDENT = "    ";
    private static final String BRANCH = "├── ";
    private static final String LAST_BRANCH = "└── ";
    private static final String VERTICAL = "│   ";
    private static final String EMPTY = "    ";

    @Override
    public String visit(LeafCondition condition) {
        return visitLeaf(condition, 0, true, "");
    }

    @Override
    public String visit(CompositeCondition condition) {
        return visitComposite(condition, 0, true, "");
    }

    /**
     * 访问叶子条件
     */
    private String visitLeaf(LeafCondition condition, int depth, boolean isLast, String prefix) {
        StringBuilder sb = new StringBuilder();

        String connector = isLast ? LAST_BRANCH : BRANCH;
        sb.append(prefix).append(connector);

        sb.append("[Leaf] ");

        if (condition.getElement() != null) {
            sb.append("元素: ").append(condition.getElement().getName());
        } else {
            sb.append("元素: null");
        }

        sb.append(" | 运算符: ").append(condition.getOperator() != null
                ? condition.getOperator().getClass().getSimpleName()
                : "null");

        if (condition.isSimple()) {
            sb.append(" | 预期值: ").append(condition.getExpectedValue());
        } else {
            sb.append(" | 预期元素: ").append(condition.getExpectedElement() != null
                    ? condition.getExpectedElement().getName()
                    : "null");
        }

        if (condition.getFailureCode() != null) {
            sb.append(" | 失败码: ").append(condition.getFailureCode());
        }

        sb.append("\n");

        return sb.toString();
    }

    /**
     * 访问组合条件
     */
    private String visitComposite(CompositeCondition condition, int depth, boolean isLast, String prefix) {
        StringBuilder sb = new StringBuilder();

        String connector = isLast ? LAST_BRANCH : BRANCH;
        sb.append(prefix).append(connector);

        sb.append("[Composite] ");
        sb.append("逻辑: ").append(condition.getLogicalOperator());
        sb.append(" | 模式: ").append(condition.getEvaluationMode());
        sb.append(" | 子条件数: ").append(condition.getConditions() != null
                ? condition.getConditions().size()
                : 0);
        sb.append("\n");

        if (condition.getConditions() != null && !condition.getConditions().isEmpty()) {
            String newPrefix = prefix + (isLast ? EMPTY : VERTICAL);

            for (int i = 0; i < condition.getConditions().size(); i++) {
                boolean isLastChild = (i == condition.getConditions().size() - 1);
                Condition child = condition.getConditions().get(i);

                if (child instanceof LeafCondition) {
                    sb.append(visitLeaf((LeafCondition) child, depth + 1, isLastChild, newPrefix));
                } else if (child instanceof CompositeCondition) {
                    sb.append(visitComposite((CompositeCondition) child, depth + 1, isLastChild, newPrefix));
                }
            }
        }

        return sb.toString();
    }

    /**
     * 便捷静态方法：打印条件树
     * 
     * @param condition 条件对象
     * @return 格式化的条件树字符串
     */
    public static String print(Condition condition) {
        PrintVisitor visitor = new PrintVisitor();

        if (condition instanceof LeafCondition) {
            return "条件树:\n" + visitor.visitLeaf((LeafCondition) condition, 0, true, "");
        } else if (condition instanceof CompositeCondition) {
            return "条件树:\n" + visitor.visitComposite((CompositeCondition) condition, 0, true, "");
        } else {
            return "未知条件类型: " + condition.getClass().getSimpleName();
        }
    }
}
