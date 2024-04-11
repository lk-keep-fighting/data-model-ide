package com.aims.datamodel.core.dsl;

import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class DataViewTableAliasMap {
    private String alias;
    private String table;
    private String dbName;
    private String primaryKey;

    public String getPrimaryKeySql() {
        return this.getFullSqlName() + "." + this.getPrimaryKey();
    }

    public String getPrimaryKey() {
        if (StringUtils.isEmpty(this.primaryKey))
            return "id";
        return this.primaryKey;
    }

    public String getFullSqlName() {
        if (StringUtils.hasText(this.getDbName()))
            return this.getDbName() + "." + this.getTable();
        else return this.getTable();
    }
}
