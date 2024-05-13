package com.aims.datamodel.core.dsl;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(chain = true)
public class DataModel {
    @Setter
    @Getter
    private String mainTable;
    @Setter
    @Getter
    private List<DataModelColumn> columns;
    @Setter
    @Getter
    private List<DataViewCondition> conditions;
    @Setter
    @Getter
    private List<DataViewJoin> joins;
    @Setter
    @Getter
    private DataViewAliasMap aliasMap = new DataViewAliasMap();

    public String findPrimaryKey() {
        return aliasMap.findTablePrimaryKey(mainTable);
    }

    public String buildPrimaryKeySql() {
        return "m." + aliasMap.findTablePrimaryKey(mainTable);
    }
}
