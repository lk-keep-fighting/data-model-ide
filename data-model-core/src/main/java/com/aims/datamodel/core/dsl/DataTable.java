package com.aims.datamodel.core.dsl;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.List;

@Data
public class DataTable {
    private String table;
    private String dbName;
    private String primaryKey;
    List<DataTableColumn> columns;

    public String getFullSqlName() {
        if (StringUtils.hasText(this.getDbName()))
            return this.getDbName() + "." + this.getTable();
        else return this.getTable();
    }

    public String getPrimaryKey() {
        if (StringUtils.isEmpty(this.primaryKey))
            return "id";
        return this.primaryKey;
    }
}
