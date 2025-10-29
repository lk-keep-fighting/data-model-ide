import React, { useEffect, useState } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table';
import { databaseApi } from '@/services/api';
import { Database, Table as TableType } from '@/types/api';
import { Database as DatabaseIcon, Table as TableIcon, RefreshCw } from 'lucide-react';

interface DatabaseListProps {
  onTableSelect?: (dbName: string, tableName: string) => void;
}

export const DatabaseList: React.FC<DatabaseListProps> = ({ onTableSelect }) => {
  const [databases, setDatabases] = useState<Database[]>([]);
  const [selectedDb, setSelectedDb] = useState<string | null>(null);
  const [tables, setTables] = useState<TableType[]>([]);

  const loadDatabases = async () => {
    try {
      const result = await databaseApi.getDatabaseList();
      if (result.code === 0 && result.data) {
        setDatabases(result.data);
      }
    } catch (error) {
      console.error('Failed to load databases:', error);
    }
  };

  const loadTables = async (dbId: string) => {
    try {
      const result = await databaseApi.getTableList(dbId);
      if (result.code === 0 && result.data) {
        setTables(result.data);
        setSelectedDb(dbId);
      }
    } catch (error) {
      console.error('Failed to load tables:', error);
    }
  };

  useEffect(() => {
    loadDatabases();
  }, []);

  return (
    <div className="space-y-4">
      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <CardTitle className="flex items-center gap-2">
              <DatabaseIcon className="w-5 h-5" />
              数据库列表
            </CardTitle>
            <Button onClick={loadDatabases} size="sm" variant="outline">
              <RefreshCw className="w-4 h-4 mr-2" />
              刷新
            </Button>
          </div>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>数据库名称</TableHead>
                <TableHead className="text-right">操作</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {databases.map((db, index) => (
                <TableRow key={index}>
                  <TableCell className="font-medium">
                    {db.DATABASE_NAME || db.name || Object.values(db)[0]}
                  </TableCell>
                  <TableCell className="text-right">
                    <Button
                      onClick={() => loadTables(db.DATABASE_NAME || db.name || Object.values(db)[0] as string)}
                      size="sm"
                      variant="outline"
                    >
                      查看表
                    </Button>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>

      {selectedDb && (
        <Card>
          <CardHeader>
            <CardTitle className="flex items-center gap-2">
              <TableIcon className="w-5 h-5" />
              表列表 - {selectedDb}
            </CardTitle>
          </CardHeader>
          <CardContent>
            <Table>
              <TableHeader>
                <TableRow>
                  <TableHead>表名</TableHead>
                  <TableHead className="text-right">操作</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {tables.map((table, index) => (
                  <TableRow key={index}>
                    <TableCell className="font-medium">
                      {table.TABLE_NAME || table.name || Object.values(table)[0]}
                    </TableCell>
                    <TableCell className="text-right">
                      <Button
                        onClick={() => onTableSelect?.(
                          selectedDb,
                          table.TABLE_NAME || table.name || Object.values(table)[0] as string
                        )}
                        size="sm"
                      >
                        创建模型
                      </Button>
                    </TableCell>
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </CardContent>
        </Card>
      )}
    </div>
  );
};
