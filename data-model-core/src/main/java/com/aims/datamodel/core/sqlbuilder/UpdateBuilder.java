package com.aims.datamodel.core.sqlbuilder;

import com.aims.datamodel.core.dsl.DataModel;
import com.aims.datamodel.core.dsl.DataViewCondition;
import com.aims.datamodel.core.sqlbuilder.input.UpdateInput;
import org.apache.logging.log4j.util.Strings;

import java.util.List;

public class UpdateBuilder {
    public static String buildByInput(UpdateInput input) {
        DataModel dm = input.getDataModel();
        StringBuilder sb = new StringBuilder();
        sb.append("UPDATE ");
        sb.append(input.getDataModel().buildTableSql(dm.getMainTable()));
        sb.append(" AS ");
        sb.append(input.getDataModel().findTableAlias(dm.getMainTable()));
        sb.append(" SET ");
        input.getValue().forEach((key, value) -> {
            sb.append(dm.buildColumnSql(key));
            sb.append(" = '");
            sb.append(value == null ? null : value.toString().replace("'", "''"));
            sb.append("',");
        });
        sb.deleteCharAt(sb.length() - 1);
        sb.append(" WHERE ");
        if (Strings.isNotBlank(input.getId())) {
            String primaryKey = input.getDataModel().findPrimaryKey();
            sb.append(primaryKey);
            sb.append(" = '").append(input.getId()).append("'");
        }
        if (input.getConditions() != null && !input.getConditions().isEmpty()) {
            if (Strings.isNotBlank(input.getId()))
                sb.append(" AND ");
            sb.append(buildConditionsSql(input));
        }
        return sb.toString();
    }

    public static String buildConditionsSql(UpdateInput input) {
        List<DataViewCondition> conditions = input.getConditions();
        DataModel dm = input.getDataModel();
        StringBuilder sql = new StringBuilder();
        for (var condition : conditions) {
            sql.append(condition.buildConditionSql(dm) + " AND ");
        }
        return sql.substring(0, sql.length() - 5);
    }
}
