package com.aims.datamodel.core.dsl;

import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class TableAliasMap extends DataTable {
    private String primaryKey;
    public String findPrimaryKey() {
        if (!StringUtils.hasText(this.primaryKey))
            return "id";
        return this.primaryKey;
    }
}
