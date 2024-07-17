package com.aims.datamodel.sdk.service;

import com.aims.datamodel.core.dsl.*;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DatabaseServiceImpl {
    @Autowired
    private DataModelServiceImpl dataModelService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 获取当前链接的数据库名
     *
     * @return
     */
    public String getCurrentDbName() {
        return jdbcTemplate.queryForObject("SELECT DATABASE()", String.class);
    }

    /**
     * 获取数据库列表
     *
     * @return
     */
    public List<Map<String, Object>> getDatabaseList() {
        return queryBySql("SELECT SCHEMA_NAME as id,SCHEMA_NAME as dbName FROM INFORMATION_SCHEMA.SCHEMATA");
    }

    public List<Map<String, Object>> queryBySql(String sql) {
        log.info("接收到查询sql：{}", sql);
        sql = sql.replaceAll("\\s+", " ");//替换空格转义字符为空格，如\t,\n,\r等
        log.info("处理转义后sql：{}", sql);
        return jdbcTemplate.queryForList(sql);
    }

    /**
     * 查询指定数据库的table清单
     */
    public List<Map<String, Object>> getTableList(String dbName) {
        return queryBySql("SELECT TABLE_NAME as id,TABLE_NAME as tableName FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = '" + dbName + "'");
    }

    /**
     * 查询指定表的column清单
     */
    public List<DataTableColumn> getColumnList(String dbName, String tableName) {
        var columns = queryBySql("SELECT COLUMN_NAME,IS_NULLABLE,COLUMN_DEFAULT,DATA_TYPE,CHARACTER_MAXIMUM_LENGTH,NUMERIC_PRECISION,NUMERIC_SCALE,COLUMN_TYPE,COLUMN_KEY,COLUMN_COMMENT FROM INFORMATION_SCHEMA.COLUMNS WHERE TABLE_SCHEMA = '" + dbName + "' AND TABLE_NAME = '" + tableName + "'");
        if (!columns.isEmpty())
            return columns.stream().map(column -> {
                var dtColumn = new DataTableColumn();
                dtColumn.setStoreColumn(column.get("COLUMN_NAME").toString());
                dtColumn.setStoreColumnType(column.get("COLUMN_TYPE").toString());
                dtColumn.setStoreDataType(column.get("DATA_TYPE").toString());
                dtColumn.setStoreDataLength(String.valueOf(column.get("CHARACTER_MAXIMUM_LENGTH")));
                var storePrecision = column.get("NUMERIC_PRECISION");
                if (storePrecision != null)
                    dtColumn.setStorePrecision(Integer.parseInt(storePrecision.toString()));
                var storeScale = column.get("NUMERIC_SCALE");
                if (storeScale != null)
                    dtColumn.setStoreScale(Integer.parseInt(storeScale.toString()));
                dtColumn.setStoreComment(String.valueOf(column.get("COLUMN_COMMENT")));
                dtColumn.setStoreIsNullable("YES".equals(String.valueOf(column.get("IS_NULLABLE"))));
                dtColumn.setStoreDefaultValue(String.valueOf(column.get("COLUMN_DEFAULT")));
                dtColumn.setStoreIsPrimaryKey("PRI".equals(String.valueOf(column.get("COLUMN_KEY"))));
                return dtColumn;
            }).toList();
        return List.of();
    }

    public void saveTableToDataModel(String dbName, String tableName, String dataModelId) throws Exception {
        DataModel dataModel = new DataModel();
        dataModel.setMainTable(tableName);
        var columns = getColumnList(dbName, tableName);
        var tableMapValue = new TableAliasMap();
//        tableMapValue.setTable(tableName);
        tableMapValue.setStoreTable(tableName);
//        tableMap.setColumns(columns);
        tableMapValue.setPrimaryKey(columns.stream().filter(DataTableColumn::isStoreIsPrimaryKey).findFirst().map(DataTableColumn::getStoreColumn).orElse(""));
        tableMapValue.setStoreDatabase(dbName);
        LinkedHashMap<String, TableAliasMap> tableMap = new LinkedHashMap<>();
        tableMap.put(tableName, tableMapValue);
        dataModel.setTableMap(tableMap);
        LinkedHashMap<String, ColumnAliasMap> columnMap = new LinkedHashMap<>();
        columns.forEach(column -> {
            ColumnAliasMap clmMapValue = JSONObject.parseObject(JSONObject.toJSONString(column), ColumnAliasMap.class);
            clmMapValue.setTable(tableName);
//            clmMapValue.setColumn(column.getStoreColumn());
            clmMapValue.setDataType(column.getStoreDataType());
            clmMapValue.setDataLength(column.getStoreDataLength());
            columnMap.put(clmMapValue.getStoreColumn(), clmMapValue);
        });
        dataModel.setColumnMap(columnMap);
        if (dataModelId == null) dataModelId = tableName;
        dataModelService.saveDataModel(dataModelId, dataModel);
    }

    public void saveTableToDataModel(String dbName, String tableName) throws Exception {
        saveTableToDataModel(dbName, tableName, tableName);
    }

    @Deprecated
    public void saveTableToFile(String dbName, String tableName) throws Exception {
        saveTableToDataModel(dbName, tableName, tableName);
    }

    @Deprecated
    public void saveTableToFile(String dbName, String tableName, String fileName) throws Exception {
        saveTableToDataModel(dbName, tableName, fileName);
    }

}
