package com.aims.datamodel.core.dsl;

import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class DataViewTableAliasMap extends DataTable {
    private String alias;
}
