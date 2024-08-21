package com.aims.datamodel.sdk.service;

import com.aims.datamodel.core.dsl.DataTableColumn;

import java.util.List;
import java.util.Map;

public interface DatabaseService {
    String getDefaultDbNameIfNull(String dbName);

    String getCurrentDbName();

    List<Map<String, Object>> getDatabaseList();

    List<Map<String, Object>> queryBySql(String sql);

    void executeSql(String sql);

    List<Map<String, Object>> getDbTableList(String dbName);

    List<DataTableColumn> getDbColumnList(String dbName, String tableName);
}
