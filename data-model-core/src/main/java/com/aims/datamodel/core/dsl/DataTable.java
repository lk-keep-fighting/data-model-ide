package com.aims.datamodel.core.dsl;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.List;

@Data
public class DataTable {
    private String storeTable;
    private String storeDatabase;
    List<DataTableColumn> columns;

    public String storeTableNameWithDb() {
        if (!StringUtils.hasText(this.storeDatabase))
            return this.storeTable;
        else return this.storeDatabase + "." + this.storeTable;
    }
}
