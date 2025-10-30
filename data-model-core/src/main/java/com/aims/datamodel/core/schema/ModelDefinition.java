package com.aims.datamodel.core.schema;

import com.aims.datamodel.core.aggregate.AggregateModel;
import com.aims.datamodel.core.query.QueryModel;
import com.aims.datamodel.core.submit.SubmitModel;
import com.aims.datamodel.core.submit.SubmitType;
import com.aims.datamodel.core.view.ViewModel;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.util.Assert;

/**
 * Unified data model definition that wires together the metadata required by
 * storage, query, submit and statistics scenes. It acts as a single entry point
 * that both humans and AI agents can reason about when constructing runtime
 * models.
 */
@Data
@Accessors(chain = true)
public class ModelDefinition {

    /** unique identifier of the model */
    private String id;

    /** human readable name */
    private String name;

    /** optional description */
    private String memo;

    /** optional version tag */
    private String version;

    /** physical storage description */
    private StoreSchema store = new StoreSchema();

    /** logical view description */
    private ViewSchema view = new ViewSchema();

    /** query behaviour defaults */
    private QuerySchema query = new QuerySchema();

    /** submit behaviour defaults */
    private SubmitSchema submit = new SubmitSchema();

    /** aggregate/statistics defaults */
    private AggregateSchema aggregate = new AggregateSchema();

    /**
     * Build the logical {@link ViewModel} that downstream builders rely on.
     */
    public ViewModel toViewModel() {
        Assert.notNull(store, "Store schema must be provided");
        Assert.notNull(view, "View schema must be provided");
        store.requireMainStoreTable();
        return view.toViewModel(this);
    }

    /**
     * Create a {@link QueryModel} with the default configuration from the
     * definition. Callers can further mutate the returned instance.
     */
    public QueryModel newQueryModel() {
        ViewModel viewModel = toViewModel();
        if (query == null) {
            return new QueryModel().setView(viewModel);
        }
        return query.toQueryModel(viewModel);
    }

    /**
     * Create a {@link SubmitModel} instance for the given submit type applying
     * default values and guard rails defined in the schema.
     */
    public SubmitModel newSubmitModel(SubmitType type) {
        ViewModel viewModel = toViewModel();
        if (submit == null) {
            return new SubmitModel().setView(viewModel).setType(type);
        }
        return submit.toSubmitModel(this, viewModel, type);
    }

    /**
     * Create an {@link AggregateModel} applying the defaults from the
     * definition.
     */
    public AggregateModel newAggregateModel() {
        ViewModel viewModel = toViewModel();
        if (aggregate == null) {
            return new AggregateModel().setView(viewModel);
        }
        return aggregate.toAggregateModel(viewModel);
    }
}
