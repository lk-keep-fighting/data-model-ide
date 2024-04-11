package com.aims.datamodel.core.sqlbuilder.input;

import com.aims.datamodel.core.dsl.DataModel;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors
public class InsertInput {
    private DataModel dataModel;
    private JSONObject value;
    private JSONArray values;
}
