package com.aims.datamodel.core.schema;

import com.aims.datamodel.core.store.StoreColumn;
import com.aims.datamodel.core.store.StoreTable;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.Assert;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Describes the physical persistence layer (databases/tables/columns) that the
 * logical model maps onto.
 */
@Data
@Accessors(chain = true)
public class StoreSchema {

    /** alias for the main table used by the logical view */
    private String mainTable;

    /** registered store tables keyed by logical alias */
    private Map<String, StoreTable> tables = new LinkedHashMap<>();

    /** column metadata keyed by physical column name */
    private Map<String, StoreColumn> columns = new LinkedHashMap<>();

    public StoreSchema putTable(String alias, StoreTable table) {
        Assert.hasText(alias, "Store table alias must not be empty");
        Assert.notNull(table, "Store table metadata must not be null");
        tables.put(alias, table);
        if (mainTable == null) {
            mainTable = alias;
        }
        return this;
    }

    public StoreSchema putColumn(String column, StoreColumn meta) {
        Assert.hasText(column, "Store column name must not be empty");
        Assert.notNull(meta, "Store column metadata must not be null");
        if (meta.getName() == null || meta.getName().isEmpty()) {
            meta.setName(column);
        }
        columns.put(column, meta);
        return this;
    }

    public Optional<StoreTable> findTable(String alias) {
        return Optional.ofNullable(tables.get(alias));
    }

    public StoreTable requireMainStoreTable() {
        Assert.hasText(mainTable, "Main store table alias must be configured");
        StoreTable table = tables.get(mainTable);
        Assert.notNull(table, "Main store table metadata is missing");
        return table;
    }
}
