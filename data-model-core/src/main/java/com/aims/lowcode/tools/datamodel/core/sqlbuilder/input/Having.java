package com.aims.lowcode.tools.datamodel.core.sqlbuilder.input;

import com.aims.lowcode.tools.datamodel.core.dsl.DataViewAliasMap;
import com.aims.lowcode.tools.datamodel.core.dsl.DataViewCondition;
import lombok.Data;

import java.util.List;

@Data
public class Having {
    private List<DataViewCondition> conditions;
    private String logic;
    public String generateHavingSql(DataViewAliasMap aliasMap) {
        if (conditions == null || conditions.isEmpty()) {
            return ""; // 如果没有聚合条件，则返回空字符串
        }
        StringBuilder havingSql = new StringBuilder();
        havingSql.append("HAVING ");
        String conditionLogic = logic != null && !logic.isEmpty() ? logic.toUpperCase() : "AND";
        for (int i = 0; i < conditions.size(); i++) {
            DataViewCondition condition = conditions.get(i);
            havingSql.append(condition.buildConditionSql(aliasMap));
            if (i < conditions.size() - 1) {
                havingSql.append(" ").append(conditionLogic).append(" ");
            }
        }
        return havingSql.toString();
    }
    // Getters and setters
}
