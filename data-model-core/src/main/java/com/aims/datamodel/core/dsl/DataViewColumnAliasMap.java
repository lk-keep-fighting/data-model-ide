package com.aims.datamodel.core.dsl;

import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class DataViewColumnAliasMap {
    private String alias;
    private String table;
    private String column;
    private String sqlType;
//    private DataTableColumn column;

    public String getFullSqlName() {
        String sql = (StringUtils.hasText(column) ? "`" + column + "`" : alias);
        if (!StringUtils.isEmpty(table))
            sql = table + "." + sql;
        return sql;
    }
//
//    public String getFullSqlNameWithAlias() {
//        return table + "." + (StringUtils.hasText(column) ? column : alias) + " as " + alias;
//    }
}
