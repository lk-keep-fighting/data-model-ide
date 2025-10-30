package com.aims.datamodel.core.schema;

import com.aims.datamodel.core.query.Pagination;
import com.aims.datamodel.core.query.QueryCondition;
import com.aims.datamodel.core.query.QueryModel;
import com.aims.datamodel.core.view.ViewModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Declarative defaults for query behaviour including selectable columns,
 * intrinsic filters and sorting hints.
 */
@Data
@Accessors(chain = true)
public class QuerySchema {

    /** default columns returned by list queries */
    private List<String> defaultSelectColumns = new ArrayList<>();

    /** columns that are allowed to appear in ORDER BY */
    private Set<String> sortableColumns = new LinkedHashSet<>();

    /** preset filters applied automatically */
    private List<QueryCondition> presetFilters = new ArrayList<>();

    /** default ORDER BY clauses */
    private List<String> defaultOrderBy = new ArrayList<>();

    /** optional default pagination */
    private Pagination defaultPagination;

    public QuerySchema addDefaultColumn(String column) {
        defaultSelectColumns.add(column);
        return this;
    }

    public QuerySchema addOrderBy(String orderBy) {
        defaultOrderBy.add(orderBy);
        return this;
    }

    public QuerySchema allowSortOn(String column) {
        sortableColumns.add(column);
        return this;
    }

    public QuerySchema addPresetFilter(QueryCondition condition) {
        presetFilters.add(condition);
        return this;
    }

    QueryModel toQueryModel(ViewModel viewModel) {
        QueryModel queryModel = new QueryModel().setView(viewModel);
        if (!defaultSelectColumns.isEmpty()) {
            queryModel.getSelectColumns().addAll(defaultSelectColumns);
        }
        if (!defaultOrderBy.isEmpty()) {
            queryModel.getOrderBy().addAll(defaultOrderBy);
        }
        if (defaultPagination != null) {
            queryModel.setPagination(copyPagination(defaultPagination));
        }
        presetFilters.stream()
                .map(this::copyCondition)
                .forEach(queryModel::addFilter);
        return queryModel;
    }

    private QueryCondition copyCondition(QueryCondition condition) {
        QueryCondition copy = new QueryCondition()
                .setColumn(condition.getColumn())
                .setOperator(condition.getOperator());
        if (condition.getValues() != null) {
            copy.setValues(Arrays.copyOf(condition.getValues(), condition.getValues().length));
        }
        return copy;
    }

    private Pagination copyPagination(Pagination pagination) {
        return new Pagination()
                .setPage(pagination.getPage())
                .setSize(pagination.getSize());
    }

    /** expose sortable columns as comma joined description for documentation */
    public String describeSortableColumns() {
        return sortableColumns.stream().collect(Collectors.joining(", "));
    }
}
