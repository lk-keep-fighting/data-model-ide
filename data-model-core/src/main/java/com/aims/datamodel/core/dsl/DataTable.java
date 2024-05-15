package com.aims.datamodel.core.dsl;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.util.List;

@Data
public class DataTable {
    private String storeTable;
    private String storeDatabase;
    List<DataTableColumn> columns;

}
