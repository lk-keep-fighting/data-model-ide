package com.aims.lowcode.tools.datamodel.core.sqlbuilder.input;

import lombok.Data;

@Data
public class Column {
    private String column;
    private String aggregate;

    public Column(String alias) {
        this.column = alias;
    }
}
