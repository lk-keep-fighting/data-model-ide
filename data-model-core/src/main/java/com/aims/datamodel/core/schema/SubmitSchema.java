package com.aims.datamodel.core.schema;

import com.aims.datamodel.core.submit.SubmitModel;
import com.aims.datamodel.core.submit.SubmitType;
import com.aims.datamodel.core.view.ViewModel;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.Assert;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * Declarative defaults and guard rails for submit (form) operations.
 */
@Data
@Accessors(chain = true)
public class SubmitSchema {

    /** toggle to allow insert */
    private boolean allowInsert = true;

    /** toggle to allow update */
    private boolean allowUpdate = true;

    /** required logical columns */
    private Set<String> requiredColumns = new LinkedHashSet<>();

    /** read only columns that should be excluded from write payloads */
    private Set<String> readOnlyColumns = new LinkedHashSet<>();

    /** default values applied when creating submit payloads */
    private Map<String, Object> defaultValues = new LinkedHashMap<>();

    public SubmitSchema require(String column) {
        requiredColumns.add(column);
        return this;
    }

    public SubmitSchema readOnly(String column) {
        readOnlyColumns.add(column);
        return this;
    }

    public SubmitSchema defaultValue(String column, Object value) {
        defaultValues.put(column, value);
        return this;
    }

    SubmitModel toSubmitModel(ModelDefinition definition, ViewModel viewModel, SubmitType type) {
        if (type == SubmitType.INSERT) {
            Assert.state(allowInsert, () -> "Insert is not allowed for model " + definition.getId());
        }
        if (type == SubmitType.UPDATE) {
            Assert.state(allowUpdate, () -> "Update is not allowed for model " + definition.getId());
        }

        SubmitModel submitModel = new SubmitModel().setView(viewModel).setType(type);
        defaultValues.forEach(submitModel.getData()::put);
        return submitModel;
    }

    public boolean isRequired(String column) {
        return requiredColumns.contains(column);
    }

    public boolean isReadOnly(String column) {
        return readOnlyColumns.contains(column);
    }
}
