package com.aims.lowcode.tools.datamodel.core.sqlbuilder.input;

import lombok.Data;

@Data
public class OrderByColumn {
    private String column;
    private String direction;

    public OrderByColumn(String alias, String direction) {
        this.column = alias;
        this.direction=direction;
    }
}
