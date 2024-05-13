package com.aims.datamodel.core.dsl;

import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class DataViewColumnAliasMap extends DataTableColumn {
    private String alias;
    private String table;

    public String buildFullSqlName() {
        String sql = "`" + (StringUtils.hasText(getColumn()) ? getColumn() : alias) + "`";
        if (!StringUtils.isEmpty(table))
            sql = table + "." + sql;
        return sql;
    }
}
