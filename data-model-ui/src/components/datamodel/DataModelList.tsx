import React, { useEffect, useState } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table';
import { dataModelApi } from '@/services/api';
import { DataModel } from '@/types/api';
import { FileText, Plus, Edit, Trash2, RefreshCw } from 'lucide-react';

interface DataModelListProps {
  onEdit?: (model: DataModel) => void;
  onCreate?: () => void;
  onSelect?: (modelId: string) => void;
}

export const DataModelList: React.FC<DataModelListProps> = ({ onEdit, onCreate, onSelect }) => {
  const [models, setModels] = useState<DataModel[]>([]);

  const loadModels = async () => {
    try {
      const result = await dataModelApi.list();
      if (result.code === 0 && result.data) {
        setModels(result.data);
      }
    } catch (error) {
      console.error('Failed to load data models:', error);
    }
  };

  const handleDelete = async (id: string) => {
    if (!confirm('确定要删除这个数据模型吗？')) return;

    try {
      const result = await dataModelApi.delete(id);
      if (result.code === 0) {
        await loadModels();
      }
    } catch (error) {
      console.error('Failed to delete data model:', error);
      alert('删除失败');
    }
  };

  useEffect(() => {
    loadModels();
  }, []);

  return (
    <Card>
      <CardHeader>
        <div className="flex items-center justify-between">
          <CardTitle className="flex items-center gap-2">
            <FileText className="w-5 h-5" />
            数据模型列表
          </CardTitle>
          <div className="flex gap-2">
            <Button onClick={loadModels} size="sm" variant="outline">
              <RefreshCw className="w-4 h-4 mr-2" />
              刷新
            </Button>
            <Button onClick={onCreate} size="sm">
              <Plus className="w-4 h-4 mr-2" />
              新建模型
            </Button>
          </div>
        </div>
      </CardHeader>
      <CardContent>
        <Table>
          <TableHeader>
            <TableRow>
              <TableHead>ID</TableHead>
              <TableHead>名称</TableHead>
              <TableHead>类型</TableHead>
              <TableHead>分组</TableHead>
              <TableHead>主表</TableHead>
              <TableHead>备注</TableHead>
              <TableHead className="text-right">操作</TableHead>
            </TableRow>
          </TableHeader>
          <TableBody>
            {models.map((model) => (
              <TableRow key={model.id}>
                <TableCell className="font-medium">{model.id}</TableCell>
                <TableCell>{model.name}</TableCell>
                <TableCell>{model.type || '-'}</TableCell>
                <TableCell>{model.group || '-'}</TableCell>
                <TableCell>{model.mainTable || '-'}</TableCell>
                <TableCell>{model.memo || '-'}</TableCell>
                <TableCell className="text-right">
                  <div className="flex justify-end gap-2">
                    <Button
                      onClick={() => onSelect?.(model.id)}
                      size="sm"
                      variant="outline"
                    >
                      管理数据
                    </Button>
                    <Button
                      onClick={() => onEdit?.(model)}
                      size="sm"
                      variant="outline"
                    >
                      <Edit className="w-4 h-4" />
                    </Button>
                    <Button
                      onClick={() => handleDelete(model.id)}
                      size="sm"
                      variant="destructive"
                    >
                      <Trash2 className="w-4 h-4" />
                    </Button>
                  </div>
                </TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </CardContent>
    </Card>
  );
};
