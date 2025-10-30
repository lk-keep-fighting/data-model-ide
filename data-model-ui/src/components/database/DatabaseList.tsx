import React, { useEffect, useState } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table';
import { databaseApi } from '@/services/api';
import { Database, Table as TableType } from '@/types/api';
import { Database as DatabaseIcon, Table as TableIcon, RefreshCw } from 'lucide-react';
import { cn } from '@/lib/utils';

interface DatabaseListProps {
  onTableSelect?: (dbName: string, tableName: string) => void;
}

export const DatabaseList: React.FC<DatabaseListProps> = ({ onTableSelect }) => {
  const [databases, setDatabases] = useState<Database[]>([]);
  const [selectedDb, setSelectedDb] = useState<string | null>(null);
  const [tables, setTables] = useState<TableType[]>([]);
  const [loadingTables, setLoadingTables] = useState(false);

  const loadDatabases = async () => {
    try {
      const result = await databaseApi.getDatabaseList();
      if (result.code === 0 && result.data) {
        setDatabases(result.data);
        if (result.data.length > 0 && !selectedDb) {
          const firstDb = result.data[0];
          const dbName = firstDb.DATABASE_NAME || firstDb.name || Object.values(firstDb)[0] as string;
          loadTables(dbName);
        }
      }
    } catch (error) {
      console.error('Failed to load databases:', error);
    }
  };

  const loadTables = async (dbId: string) => {
    setLoadingTables(true);
    try {
      const result = await databaseApi.getTableList(dbId);
      if (result.code === 0 && result.data) {
        setTables(result.data);
        setSelectedDb(dbId);
      }
    } catch (error) {
      console.error('Failed to load tables:', error);
    } finally {
      setLoadingTables(false);
    }
  };

  useEffect(() => {
    loadDatabases();
  }, []);

  const handleDatabaseClick = (dbName: string) => {
    if (selectedDb !== dbName) {
      loadTables(dbName);
    }
  };

  return (
    <div className="flex gap-4 h-full">
      {/* 左侧：数据库列表 */}
      <div className="w-80 flex-shrink-0">
        <Card className="h-full flex flex-col">
          <CardHeader className="flex-shrink-0">
            <div className="flex items-center justify-between">
              <CardTitle className="flex items-center gap-2 text-lg">
                <DatabaseIcon className="w-5 h-5" />
                数据库列表
              </CardTitle>
              <Button onClick={loadDatabases} size="sm" variant="outline">
                <RefreshCw className="w-4 h-4" />
              </Button>
            </div>
          </CardHeader>
          <CardContent className="flex-1 overflow-auto p-3">
            <div className="space-y-1">
              {databases.map((db, index) => {
                const dbName = db.DATABASE_NAME || db.name || Object.values(db)[0] as string;
                const isSelected = selectedDb === dbName;
                return (
                  <button
                    key={index}
                    onClick={() => handleDatabaseClick(dbName)}
                    className={cn(
                      "w-full text-left px-4 py-3 rounded-md transition-colors",
                      isSelected
                        ? "bg-blue-50 border border-blue-200 text-blue-700"
                        : "hover:bg-gray-50 border border-transparent"
                    )}
                  >
                    <div className="flex items-center gap-2">
                      <DatabaseIcon className={cn(
                        "w-4 h-4",
                        isSelected ? "text-blue-600" : "text-gray-400"
                      )} />
                      <span className="font-medium truncate">{dbName}</span>
                    </div>
                  </button>
                );
              })}
              {databases.length === 0 && (
                <div className="text-center py-8 text-gray-500 text-sm">
                  暂无数据库
                </div>
              )}
            </div>
          </CardContent>
        </Card>
      </div>

      {/* 右侧：表列表 */}
      <div className="flex-1">
        <Card className="h-full flex flex-col">
          <CardHeader className="flex-shrink-0">
            <CardTitle className="flex items-center gap-2 text-lg">
              <TableIcon className="w-5 h-5" />
              {selectedDb ? `表列表 - ${selectedDb}` : '表列表'}
            </CardTitle>
          </CardHeader>
          <CardContent className="flex-1 overflow-auto">
            {loadingTables ? (
              <div className="flex items-center justify-center h-full text-gray-500">
                <div className="text-center">
                  <RefreshCw className="w-8 h-8 animate-spin mx-auto mb-2" />
                  <p>加载中...</p>
                </div>
              </div>
            ) : selectedDb ? (
              <div className="border rounded-lg overflow-hidden">
                <Table>
                  <TableHeader>
                    <TableRow className="bg-gray-50">
                      <TableHead className="font-semibold">表名</TableHead>
                      <TableHead className="text-right font-semibold w-32">操作</TableHead>
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    {tables.length === 0 ? (
                      <TableRow>
                        <TableCell colSpan={2} className="text-center py-12 text-gray-500">
                          该数据库暂无表
                        </TableCell>
                      </TableRow>
                    ) : (
                      tables.map((table, index) => {
                        const tableName = table.TABLE_NAME || table.name || Object.values(table)[0] as string;
                        return (
                          <TableRow key={index} className="hover:bg-gray-50">
                            <TableCell className="font-medium">
                              <div className="flex items-center gap-2">
                                <TableIcon className="w-4 h-4 text-gray-400" />
                                {tableName}
                              </div>
                            </TableCell>
                            <TableCell className="text-right">
                              <Button
                                onClick={() => onTableSelect?.(selectedDb, tableName)}
                                size="sm"
                                className="shadow-sm"
                              >
                                创建模型
                              </Button>
                            </TableCell>
                          </TableRow>
                        );
                      })
                    )}
                  </TableBody>
                </Table>
              </div>
            ) : (
              <div className="flex items-center justify-center h-full text-gray-500">
                <div className="text-center">
                  <DatabaseIcon className="w-12 h-12 mx-auto mb-3 text-gray-300" />
                  <p className="text-lg mb-1">请选择一个数据库</p>
                  <p className="text-sm">从左侧列表中选择数据库以查看表</p>
                </div>
              </div>
            )}
          </CardContent>
        </Card>
      </div>
    </div>
  );
};
