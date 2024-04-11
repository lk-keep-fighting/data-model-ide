package com.aims.datamodel.core.sqlbuilder.input;

import lombok.Data;

import java.util.List;

@Data
public class GroupBy {
    private List<String> columns;

    public String buildGroupBySql() {
        if (columns == null || columns.isEmpty()) {
            return ""; // 如果没有分组列，则返回空字符串
        }
        return "GROUP BY " + String.join(", ", columns);
    }
}
