package com.aims.datamodel.core.submit;

import com.aims.datamodel.core.view.ViewModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 提交场景模型（表单提交）
 */
@Data
@Accessors(chain = true)
public class SubmitModel {
    /**
     * 关联的视图模型
     */
    private ViewModel view;
    
    /**
     * 提交类型
     */
    private SubmitType type;
    
    /**
     * 提交的数据 key=逻辑列名, value=列值
     */
    private Map<String, Object> data = new LinkedHashMap<>();
    
    /**
     * 主键值（UPDATE时必填）
     */
    private Object primaryKeyValue;
    
    /**
     * 添加字段数据
     */
    public SubmitModel putData(String column, Object value) {
        this.data.put(column, value);
        return this;
    }
}
