package com.aims.datamodel.core.schema;

import com.aims.datamodel.core.store.StoreTable;
import com.aims.datamodel.core.view.StoreMapping;
import com.aims.datamodel.core.view.ViewColumn;
import com.aims.datamodel.core.view.ViewModel;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.Assert;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Describes the logical view layer that decouples business facing fields from
 * the physical storage columns.
 */
@Data
@Accessors(chain = true)
public class ViewSchema {

    /** logical columns keyed by column name */
    private Map<String, ViewColumn> columns = new LinkedHashMap<>();

    /** mapping between logical columns and physical storage */
    private Map<String, StoreMapping> mappings = new LinkedHashMap<>();

    public ViewSchema putColumn(String name, ViewColumn column) {
        Assert.hasText(name, "View column name must not be empty");
        Assert.notNull(column, "View column metadata must not be null");
        if (column.getName() == null || column.getName().isEmpty()) {
            column.setName(name);
        }
        columns.put(name, column);
        return this;
    }

    public ViewSchema mapToStore(String column, StoreMapping mapping) {
        Assert.hasText(column, "View column name must not be empty");
        Assert.notNull(mapping, "Store mapping must not be null");
        mapping.setViewColumn(column);
        mappings.put(column, mapping);
        return this;
    }

    ViewModel toViewModel(ModelDefinition definition) {
        StoreSchema storeSchema = definition.getStore();
        StoreTable mainStore = storeSchema.requireMainStoreTable();

        ViewModel viewModel = new ViewModel()
                .setId(definition.getId())
                .setName(definition.getName())
                .setMainTable(storeSchema.getMainTable());

        viewModel.getColumns().putAll(columns);
        viewModel.getMappings().putAll(mappings);
        viewModel.getStoreTables().putAll(storeSchema.getTables());

        // ensure main table exists in the view store map for downstream builders
        viewModel.getStoreTables().putIfAbsent(storeSchema.getMainTable(), mainStore);
        return viewModel;
    }
}
