package com.aims.datamodel.core.dsl;

import lombok.Data;

import java.util.List;

@Data
public class DataModel {
    private String mainTable;
    private List<DataModelColumn> columns;
    private List<DataViewCondition> conditions;
    private List<DataViewJoin> joins;
    private DataViewAliasMap aliasMap;
    public DataModel()
    {
        aliasMap = new DataViewAliasMap();
    }
}
