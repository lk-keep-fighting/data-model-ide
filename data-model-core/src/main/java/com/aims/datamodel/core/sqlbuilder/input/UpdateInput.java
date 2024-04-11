package com.aims.datamodel.core.sqlbuilder.input;

import com.aims.datamodel.core.dsl.DataModel;
import com.aims.datamodel.core.dsl.DataViewCondition;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors
public class UpdateInput {
    private DataModel dataModel;
    private JSONObject value;
    private List<DataViewCondition> conditions;
    private String id;
}
