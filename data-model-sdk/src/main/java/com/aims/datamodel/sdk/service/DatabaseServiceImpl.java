package com.aims.datamodel.sdk.service;

import com.aims.datamodel.core.dsl.*;
import com.alibaba.fastjson2.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DatabaseServiceImpl {
    @Autowired
    private DataModelServiceImpl dataModelService;

    /**
     * 获取数据库列表
     *
     * @return
     */
    public List<Map<String, Object>> getDatabaseList() {
        return dataModelService.queryBySql("SELECT SCHEMA_NAME as id,SCHEMA_NAME as dbName FROM INFORMATION_SCHEMA.SCHEMATA");
    }

    /**
     * 查询指定数据库的table清单
     */
    public List<Map<String, Object>> getTableList(String dbName) {
        return dataModelService.queryBySql("SELECT TABLE_NAME as id,TABLE_NAME as tableName FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = '" + dbName + "'");
    }

    /**
     * 查询指定表的column清单
     */
    public List<DataTableColumn> getColumnList(String dbName, String tableName) {
        var columns = dataModelService.queryBySql("SELECT COLUMN_NAME,IS_NULLABLE,COLUMN_DEFAULT,DATA_TYPE,CHARACTER_MAXIMUM_LENGTH,NUMERIC_PRECISION,NUMERIC_SCALE,COLUMN_TYPE,COLUMN_KEY,COLUMN_COMMENT FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '" + dbName + "' AND TABLE_NAME = '" + tableName + "'");
        if (!columns.isEmpty())
            return columns.stream().map(column -> {
                var dtColumn = new DataTableColumn();
                dtColumn.setColumn(column.get("COLUMN_NAME").toString());
                dtColumn.setDataType(column.get("DATA_TYPE").toString());
                dtColumn.setStoreDataType(column.get("COLUMN_TYPE").toString());
                dtColumn.setStoreLength(String.valueOf(column.get("CHARACTER_MAXIMUM_LENGTH")));
                var storePrecision = column.get("NUMERIC_PRECISION");
                if (storePrecision != null)
                    dtColumn.setStorePrecision(Integer.parseInt(storePrecision.toString()));
                var storeScale = column.get("NUMERIC_SCALE");
                if (storeScale != null)
                    dtColumn.setStoreScale(Integer.parseInt(storeScale.toString()));
                dtColumn.setStoreComment(String.valueOf(column.get("COLUMN_COMMENT")));
                dtColumn.setStoreIsNullable("YES".equals(String.valueOf(column.get("IS_NULLABLE"))));
                dtColumn.setDefaultValue(String.valueOf(column.get("COLUMN_DEFAULT")));
                dtColumn.setStoreIsPrimaryKey("PRI".equals(String.valueOf(column.get("COLUMN_KEY"))));
                return dtColumn;
            }).toList();
        return List.of();
    }

    public void saveTableToFile(String dbName, String tableName, String fileName) throws Exception {
        DataModel dataModel = new DataModel();
        dataModel.setMainTable(tableName);
        var columns = getColumnList(dbName, tableName);
        var aliasMap = new DataViewAliasMap();
        var tableMap = new DataViewTableAliasMap();
        tableMap.setTable(tableName);
//        tableMap.setColumns(columns);
        tableMap.setPrimaryKey(columns.stream().filter(DataTableColumn::isStoreIsPrimaryKey).findFirst().map(DataTableColumn::getColumn).orElse(""));
        tableMap.setDbName(dbName);
        aliasMap.setTableMaps(List.of(tableMap));
        aliasMap.setColumnMaps(columns.stream().map(column -> {
            DataViewColumnAliasMap columnMap = JSONObject.parseObject(JSONObject.toJSONString(column), DataViewColumnAliasMap.class);
            columnMap.setAlias(column.getColumn());
            columnMap.setTable(tableName);
            return columnMap;
        }).toList());
        dataModel.setAliasMap(aliasMap);
        if (fileName == null) fileName = tableName;
        dataModelService.saveDataModel(fileName, dataModel);
    }

    public void saveTableToFile(String dbName, String tableName) throws Exception {
        saveTableToFile(dbName, tableName, tableName);
    }
}
