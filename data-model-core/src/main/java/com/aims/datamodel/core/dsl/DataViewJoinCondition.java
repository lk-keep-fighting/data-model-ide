package com.aims.datamodel.core.dsl;

import lombok.Data;

@Data
public class DataViewJoinCondition {
    private String column1;
    private String column2;
    private String type;
}
