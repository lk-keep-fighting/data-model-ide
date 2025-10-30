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
}

export const CrudDataManager: React.FC<CrudDataManagerProps> = ({ dataModelId }) => {
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
    <div className="space-y-6">
      <Card className="shadow-md">
        <CardHeader>
          <div className="flex items-center justify-between">
            <CardTitle className="text-lg">数据列表</CardTitle>
            <Button onClick={handleCreate} size="sm" className="shadow-sm">
              <Plus className="w-4 h-4 mr-2" />
              新增数据
            </Button>
          </div>
        </CardHeader>
        <CardContent className="p-6">
          <div className="mb-6 flex gap-2">
            <Input
              placeholder="搜索数据..."
              value={searchText}
              onChange={(e) => setSearchText(e.target.value)}
              className="max-w-md"
            />
            <Button variant="outline" className="shadow-sm">
              <Search className="w-4 h-4" />
            </Button>
          </div>

          <div className="border rounded-lg overflow-hidden">
            <Table>
              <TableHeader>
                <TableRow className="bg-gray-50">
                  {displayColumns.map((col) => (
                    <TableHead key={col} className="font-semibold">{col}</TableHead>
                  ))}
                  <TableHead className="text-right font-semibold">操作</TableHead>
                </TableRow>
              </TableHeader>
              <TableBody>
                {data.length === 0 ? (
                  <TableRow>
                    <TableCell colSpan={displayColumns.length + 1} className="text-center py-12 text-gray-500">
                      暂无数据
                    </TableCell>
                  </TableRow>
                ) : (
                  data.map((item, index) => (
                    <TableRow key={index} className="hover:bg-gray-50">
                      {displayColumns.map((col) => (
                        <TableCell key={col} className="max-w-xs truncate">
                          {typeof item[col] === 'object' ? JSON.stringify(item[col]) : String(item[col] || '-')}
                        </TableCell>
                      ))}
                      <TableCell className="text-right">
                        <div className="flex justify-end gap-2">
                          <Button
                            onClick={() => handleEdit(item)}
                            size="sm"
                            variant="outline"
                            className="shadow-sm"
                          >
                            <Edit className="w-4 h-4" />
                          </Button>
                          <Button
                            onClick={() => handleDelete(item)}
                            size="sm"
                            variant="destructive"
                            className="shadow-sm"
                          >
                            <Trash2 className="w-4 h-4" />
                          </Button>
                        </div>
                      </TableCell>
                    </TableRow>
                  ))
                )}
              </TableBody>
            </Table>
          </div>

          <div className="flex items-center justify-between mt-6">
            <div className="text-sm text-gray-600">
              共 <span className="font-medium text-gray-900">{total}</span> 条数据，
              第 <span className="font-medium text-gray-900">{page}</span> / {totalPages || 1} 页
            </div>
            <div className="flex gap-2">
              <Button
                onClick={() => setPage(p => Math.max(1, p - 1))}
                disabled={page === 1}
                size="sm"
                variant="outline"
                className="shadow-sm"
              >
                <ChevronLeft className="w-4 h-4 mr-1" />
                上一页
              </Button>
              <Button
                onClick={() => setPage(p => Math.min(totalPages, p + 1))}
                disabled={page === totalPages || totalPages === 0}
                size="sm"
                variant="outline"
                className="shadow-sm"
              >
                下一页
                <ChevronRight className="w-4 h-4 ml-1" />
              </Button>
            </div>
          </div>
        </CardContent>
      </Card>

      <Dialog open={editDialogOpen} onOpenChange={setEditDialogOpen}>
        <DialogContent className="sm:max-w-[600px]">
          <DialogHeader>
            <DialogTitle className="text-xl">
              {editingData ? '编辑数据' : '新增数据'}
            </DialogTitle>
          </DialogHeader>

          <div className="space-y-4 max-h-[60vh] overflow-y-auto pr-2">
            {displayColumns.map((col) => (
              <div key={col}>
                <label className="block text-sm font-medium text-gray-700 mb-2">
                  {col}
                </label>
                <Input
                  value={formData[col] || ''}
                  onChange={(e) => setFormData({ ...formData, [col]: e.target.value })}
                  placeholder={`请输入${col}`}
                  className="w-full"
                />
              </div>
            ))}
          </div>

          <DialogFooter className="gap-2">
            <Button variant="outline" onClick={() => setEditDialogOpen(false)} className="shadow-sm">
              取消
            </Button>
            <Button onClick={handleSave} className="shadow-sm">
              保存
            </Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>
    </div>
  );
};
