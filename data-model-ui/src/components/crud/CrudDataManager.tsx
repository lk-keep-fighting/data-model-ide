import React, { useEffect, useState } from 'react';
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from '@/components/ui/dialog';
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table';
import { crudApi, dataModelApi } from '@/services/api';
import { DataModel, QueryInput } from '@/types/api';
import { Plus, Edit, Trash2, Search, ChevronLeft, ChevronRight } from 'lucide-react';

interface CrudDataManagerProps {
  dataModelId: string;
  onBack?: () => void;
}

export const CrudDataManager: React.FC<CrudDataManagerProps> = ({ dataModelId, onBack }) => {
  const [model, setModel] = useState<DataModel | null>(null);
  const [data, setData] = useState<any[]>([]);
  const [total, setTotal] = useState(0);
  const [page, setPage] = useState(1);
  const [pageSize] = useState(10);
  const [searchText, setSearchText] = useState('');
  const [editDialogOpen, setEditDialogOpen] = useState(false);
  const [editingData, setEditingData] = useState<any>(null);
  const [formData, setFormData] = useState<any>({});

  const loadModel = async () => {
    try {
      const result = await dataModelApi.get(dataModelId);
      if (result.code === 0 && result.data) {
        setModel(result.data);
      }
    } catch (error) {
      console.error('Failed to load model:', error);
    }
  };

  const loadData = async () => {
    try {
      const queryInput: QueryInput = {
        page,
        pageSize,
      };

      const result = await crudApi.queryPage(dataModelId, queryInput);
      if (result.code === 0 && result.data) {
        setData(result.data.list || []);
        setTotal(result.data.total || 0);
      }
    } catch (error) {
      console.error('Failed to load data:', error);
    }
  };

  useEffect(() => {
    loadModel();
  }, [dataModelId]);

  useEffect(() => {
    if (model) {
      loadData();
    }
  }, [model, page]);

  const handleCreate = () => {
    setEditingData(null);
    setFormData({});
    setEditDialogOpen(true);
  };

  const handleEdit = (item: any) => {
    setEditingData(item);
    setFormData(item);
    setEditDialogOpen(true);
  };

  const handleDelete = async (item: any) => {
    if (!confirm('确定要删除这条数据吗？')) return;

    try {
      const id = item.id || item['id'];
      const result = await crudApi.delete(dataModelId, id);
      if (result.code === 0) {
        await loadData();
      }
    } catch (error) {
      console.error('Failed to delete data:', error);
      alert('删除失败');
    }
  };

  const handleSave = async () => {
    try {
      if (editingData) {
        const id = editingData.id || editingData['id'];
        const result = await crudApi.edit(dataModelId, id, formData);
        if (result.code === 0) {
          alert('更新成功');
          setEditDialogOpen(false);
          await loadData();
        }
      } else {
        const result = await crudApi.add(dataModelId, formData);
        if (result.code === 0) {
          alert('创建成功');
          setEditDialogOpen(false);
          await loadData();
        }
      }
    } catch (error) {
      console.error('Failed to save data:', error);
      alert('保存失败');
    }
  };

  const columns = model?.columns?.map(col => col.column) || [];
  const displayColumns = columns.length > 0 ? columns : (data[0] ? Object.keys(data[0]) : []);

  const totalPages = Math.ceil(total / pageSize);

  return (
    <div className="space-y-4">
      <Card>
        <CardHeader>
          <div className="flex items-center justify-between">
            <div>
              <Button onClick={onBack} variant="outline" size="sm" className="mb-2">
                <ChevronLeft className="w-4 h-4 mr-1" />
                返回
              </Button>
              <CardTitle>数据管理 - {model?.name || dataModelId}</CardTitle>
            </div>
            <Button onClick={handleCreate} size="sm">
              <Plus className="w-4 h-4 mr-2" />
              新增数据
            </Button>
          </div>
        </CardHeader>
        <CardContent>
          <div className="mb-4 flex gap-2">
            <Input
              placeholder="搜索..."
              value={searchText}
              onChange={(e) => setSearchText(e.target.value)}
              className="max-w-sm"
            />
            <Button variant="outline">
              <Search className="w-4 h-4" />
            </Button>
          </div>

          <Table>
            <TableHeader>
              <TableRow>
                {displayColumns.map((col) => (
                  <TableHead key={col}>{col}</TableHead>
                ))}
                <TableHead className="text-right">操作</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {data.map((item, index) => (
                <TableRow key={index}>
                  {displayColumns.map((col) => (
                    <TableCell key={col}>
                      {typeof item[col] === 'object' ? JSON.stringify(item[col]) : String(item[col] || '')}
                    </TableCell>
                  ))}
                  <TableCell className="text-right">
                    <div className="flex justify-end gap-2">
                      <Button
                        onClick={() => handleEdit(item)}
                        size="sm"
                        variant="outline"
                      >
                        <Edit className="w-4 h-4" />
                      </Button>
                      <Button
                        onClick={() => handleDelete(item)}
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

          <div className="flex items-center justify-between mt-4">
            <div className="text-sm text-gray-500">
              共 {total} 条数据，第 {page} / {totalPages} 页
            </div>
            <div className="flex gap-2">
              <Button
                onClick={() => setPage(p => Math.max(1, p - 1))}
                disabled={page === 1}
                size="sm"
                variant="outline"
              >
                <ChevronLeft className="w-4 h-4" />
              </Button>
              <Button
                onClick={() => setPage(p => Math.min(totalPages, p + 1))}
                disabled={page === totalPages}
                size="sm"
                variant="outline"
              >
                <ChevronRight className="w-4 h-4" />
              </Button>
            </div>
          </div>
        </CardContent>
      </Card>

      <Dialog open={editDialogOpen} onOpenChange={setEditDialogOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>{editingData ? '编辑数据' : '新增数据'}</DialogTitle>
          </DialogHeader>

          <div className="space-y-4 max-h-96 overflow-y-auto">
            {displayColumns.map((col) => (
              <div key={col}>
                <label className="block text-sm font-medium mb-1">{col}</label>
                <Input
                  value={formData[col] || ''}
                  onChange={(e) => setFormData({ ...formData, [col]: e.target.value })}
                  placeholder={`请输入${col}`}
                />
              </div>
            ))}
          </div>

          <DialogFooter>
            <Button variant="outline" onClick={() => setEditDialogOpen(false)}>
              取消
            </Button>
            <Button onClick={handleSave}>
              保存
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
};
