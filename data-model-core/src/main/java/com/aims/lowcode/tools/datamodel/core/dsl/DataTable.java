package com.aims.lowcode.tools.datamodel.core.dsl;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.List;

@Data
public class DataTable {
    private String tableName;
    private String dbName;
    List<DataTableColumn> columns;

    public String getFullSqlName() {
        if (StringUtils.hasText(this.getDbName()))
            return this.getDbName() + "." + this.getTableName();
        else return this.getTableName();
    }
}
