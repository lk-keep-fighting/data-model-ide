package com.aims.lowcode.tools.datamodel.core.dsl;

import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class DataViewTableAliasMap {
    private String alias;
    private String table;
    private String dbName;
    public String getFullSqlName() {
        if (StringUtils.hasText(this.getDbName()))
            return this.getDbName() + "." + this.getTable();
        else return this.getTable();
    }
}
