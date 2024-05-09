package com.aims.datamodel.core.dsl;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
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
