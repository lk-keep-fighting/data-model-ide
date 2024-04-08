package com.aims.lowcode.tools.datamodel.core.dsl;

import lombok.Data;

import java.util.List;

@Data
public class DataView {
    private String mainTable;
    private List<DataViewColumn> columns;
    private List<DataViewCondition> conditions;
    private List<DataViewJoin> joins;
    private DataViewAliasMap aliasMap;
}
