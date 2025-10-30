package com.aims.datamodel.core.schema;

import com.aims.datamodel.core.aggregate.AggregateField;
import com.aims.datamodel.core.aggregate.AggregateFunc;
import com.aims.datamodel.core.aggregate.AggregateModel;
import com.aims.datamodel.core.query.Operator;
import com.aims.datamodel.core.query.QueryCondition;
import com.aims.datamodel.core.query.QueryModel;
import com.aims.datamodel.core.store.StoreColumn;
import com.aims.datamodel.core.store.StoreTable;
import com.aims.datamodel.core.submit.SubmitModel;
import com.aims.datamodel.core.submit.SubmitType;
import com.aims.datamodel.core.view.StoreMapping;
import com.aims.datamodel.core.view.ViewColumn;
import com.aims.datamodel.core.view.ViewModel;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ModelDefinitionTest {

    @Test
    void shouldCreateRuntimeModelsFromUnifiedDefinition() {
        ModelDefinition definition = new ModelDefinition()
                .setId("user")
                .setName("User")
                .setMemo("User master data");

        // store section
        definition.getStore()
                .setMainTable("user")
                .putTable("user", new StoreTable().setTable("t_user").setPrimaryKey("id"))
                .putColumn("id", new StoreColumn().setType("bigint"))
                .putColumn("name", new StoreColumn().setType("varchar(100)"))
                .putColumn("status", new StoreColumn().setType("varchar(20)"));

        // view section
        definition.getView()
                .putColumn("id", new ViewColumn().setLabel("ID"))
                .putColumn("name", new ViewColumn().setLabel("Name"))
                .putColumn("status", new ViewColumn().setLabel("Status"))
                .mapToStore("id", new StoreMapping().setStoreTable("user").setStoreColumn("id"))
                .mapToStore("name", new StoreMapping().setStoreTable("user").setStoreColumn("name"))
                .mapToStore("status", new StoreMapping().setStoreTable("user").setStoreColumn("status"));

        // query defaults
        definition.getQuery()
                .addDefaultColumn("id")
                .addDefaultColumn("name")
                .addOrderBy("id DESC")
                .addPresetFilter(new QueryCondition()
                        .setColumn("status")
                        .setOperator(Operator.EQ)
                        .setValues(new Object[]{"ENABLED"}));

        // submit defaults
        definition.getSubmit()
                .require("name")
                .defaultValue("status", "ENABLED");

        // aggregate defaults
        definition.getAggregate()
                .addMetric(AggregateField.of(AggregateFunc.COUNT, "id", "total"))
                .addGroupBy("status");

        ViewModel viewModel = definition.toViewModel();
        assertEquals("user", viewModel.getMainTable());
        assertTrue(viewModel.getColumns().containsKey("name"));
        assertEquals("t_user", viewModel.getStoreTables().get("user").getTable());

        QueryModel queryModel = definition.newQueryModel();
        assertEquals(viewModel, queryModel.getView());
        assertEquals(2, queryModel.getSelectColumns().size());
        assertEquals("id DESC", queryModel.getOrderBy().get(0));
        assertEquals(1, queryModel.getFilters().size());
        assertEquals("ENABLED", queryModel.getFilters().get(0).getValues()[0]);

        SubmitModel submitModel = definition.newSubmitModel(SubmitType.INSERT);
        assertEquals("ENABLED", submitModel.getData().get("status"));
        assertTrue(definition.getSubmit().isRequired("name"));

        AggregateModel aggregateModel = definition.newAggregateModel();
        assertEquals(viewModel, aggregateModel.getView());
        assertEquals(1, aggregateModel.getAggregates().size());
        assertEquals("total", aggregateModel.getAggregates().get(0).getAlias());
        assertEquals(1, aggregateModel.getGroupBy().size());
    }

    @Test
    void shouldRespectSubmitGuards() {
        ModelDefinition definition = new ModelDefinition().setId("demo");
        definition.getStore()
                .setMainTable("demo")
                .putTable("demo", new StoreTable().setTable("t_demo"));
        definition.getView(); // ensure view initialised

        definition.getSubmit().setAllowUpdate(false);

        assertDoesNotThrow(() -> definition.newSubmitModel(SubmitType.INSERT));
        assertThrows(IllegalStateException.class, () -> definition.newSubmitModel(SubmitType.UPDATE));
    }
}
