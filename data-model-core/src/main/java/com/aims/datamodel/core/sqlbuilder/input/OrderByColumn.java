package com.aims.datamodel.core.sqlbuilder.input;

import lombok.Data;

@Data
public class OrderByColumn {
    private String column;
    private String direction;

    public OrderByColumn() {

    }

    public OrderByColumn(String alias, String direction) {
        this.column = alias;
        this.direction = direction;
    }
}
