package com.aims.datamodel.core.schema;

import com.aims.datamodel.core.aggregate.AggregateField;
import com.aims.datamodel.core.aggregate.AggregateModel;
import com.aims.datamodel.core.query.QueryCondition;
import com.aims.datamodel.core.view.ViewModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Declarative defaults for statistics/aggregation queries.
 */
@Data
@Accessors(chain = true)
public class AggregateSchema {

    /** aggregate metrics that should be produced */
    private List<AggregateField> metrics = new ArrayList<>();

    /** group by columns */
    private List<String> groupBy = new ArrayList<>();

    /** preset filters applied before aggregation */
    private List<QueryCondition> presetFilters = new ArrayList<>();

    public AggregateSchema addMetric(AggregateField field) {
        metrics.add(field);
        return this;
    }

    public AggregateSchema addGroupBy(String column) {
        groupBy.add(column);
        return this;
    }

    public AggregateSchema addPresetFilter(QueryCondition condition) {
        presetFilters.add(condition);
        return this;
    }

    AggregateModel toAggregateModel(ViewModel viewModel) {
        AggregateModel aggregateModel = new AggregateModel().setView(viewModel);
        metrics.stream().map(this::copyField).forEach(aggregateModel::addAggregate);
        groupBy.forEach(aggregateModel::addGroupBy);
        presetFilters.stream().map(this::copyCondition).forEach(aggregateModel::addFilter);
        return aggregateModel;
    }

    private AggregateField copyField(AggregateField field) {
        return new AggregateField()
                .setColumn(field.getColumn())
                .setFunc(field.getFunc())
                .setAlias(field.getAlias());
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
}
