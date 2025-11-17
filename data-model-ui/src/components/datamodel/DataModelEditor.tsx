import React, { useState, useEffect } from 'react';
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogFooter } from '@/components/ui/dialog';
import { Button } from '@/components/ui/button';
import { Input } from '@/components/ui/input';
import { Textarea } from '@/components/ui/textarea';
import { dataModelApi } from '@/services/api';
import { DataModel } from '@/types/api';
import { Plus, Trash2 } from 'lucide-react';

interface DataModelEditorProps {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  model?: DataModel | null;
  onSaved?: () => void;
}

export const DataModelEditor: React.FC<DataModelEditorProps> = ({
  open,
  onOpenChange,
  model,
  onSaved,
}) => {
  const [formData, setFormData] = useState<DataModel>({
    id: '',
    name: '',
    type: '',
    group: '',
    memo: '',
    mainTable: '',
    version: '1.0',
    columns: [],
  });

  useEffect(() => {
    if (model) {
      setFormData(model);
    } else {
      setFormData({
        id: '',
        name: '',
        type: '',
        group: '',
        memo: '',
        mainTable: '',
        version: '1.0',
        columns: [],
      });
    }
  }, [model]);

  const handleSubmit = async () => {
    if (!formData.id || !formData.name) {
      alert('请填写必填项');
      return;
    }

    try {
      const result = await dataModelApi.createOrSave(formData.id, formData);
      if (result.code === 0) {
        alert('保存成功');
        onSaved?.();
        onOpenChange(false);
      }
    } catch (error) {
      console.error('Failed to save data model:', error);
      alert('保存失败');
    }
  };

  const addColumn = () => {
    setFormData({
      ...formData,
      columns: [...(formData.columns || []), { column: '' }],
    });
  };

  const removeColumn = (index: number) => {
    const newColumns = [...(formData.columns || [])];
    newColumns.splice(index, 1);
    setFormData({ ...formData, columns: newColumns });
  };

  const updateColumn = (index: number, value: string) => {
    const newColumns = [...(formData.columns || [])];
    newColumns[index] = { column: value };
    setFormData({ ...formData, columns: newColumns });
  };

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="max-w-2xl">
        <DialogHeader>
          <DialogTitle>{model ? '编辑数据模型' : '创建数据模型'}</DialogTitle>
        </DialogHeader>

        <div className="space-y-4">
          <div>
            <label className="block text-sm font-medium mb-1">模型ID *</label>
            <Input
              value={formData.id}
              onChange={(e) => setFormData({ ...formData, id: e.target.value })}
              placeholder="请输入模型ID"
              disabled={!!model}
            />
          </div>

          <div>
            <label className="block text-sm font-medium mb-1">模型名称 *</label>
            <Input
              value={formData.name}
              onChange={(e) => setFormData({ ...formData, name: e.target.value })}
              placeholder="请输入模型名称"
            />
          </div>

          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className="block text-sm font-medium mb-1">类型</label>
              <Input
                value={formData.type || ''}
                onChange={(e) => setFormData({ ...formData, type: e.target.value })}
                placeholder="请输入类型"
              />
            </div>

            <div>
              <label className="block text-sm font-medium mb-1">分组</label>
              <Input
                value={formData.group || ''}
                onChange={(e) => setFormData({ ...formData, group: e.target.value })}
                placeholder="请输入分组"
              />
            </div>
          </div>

          <div>
            <label className="block text-sm font-medium mb-1">主表</label>
            <Input
              value={formData.mainTable || ''}
              onChange={(e) => setFormData({ ...formData, mainTable: e.target.value })}
              placeholder="请输入主表名称"
            />
          </div>

          <div>
            <label className="block text-sm font-medium mb-1">备注</label>
            <Textarea
              value={formData.memo || ''}
              onChange={(e) => setFormData({ ...formData, memo: e.target.value })}
              placeholder="请输入备注"
              rows={3}
            />
          </div>

          <div>
            <div className="flex items-center justify-between mb-2">
              <label className="block text-sm font-medium">列定义</label>
              <Button onClick={addColumn} size="sm" variant="outline">
                <Plus className="w-4 h-4 mr-1" />
                添加列
              </Button>
            </div>
            <div className="space-y-2 max-h-40 overflow-y-auto">
              {formData.columns?.map((col, index) => (
                <div key={index} className="flex gap-2">
                  <Input
                    value={col.column}
                    onChange={(e) => updateColumn(index, e.target.value)}
                    placeholder="列名"
                  />
                  <Button
                    onClick={() => removeColumn(index)}
                    size="sm"
                    variant="destructive"
                  >
                    <Trash2 className="w-4 h-4" />
                  </Button>
                </div>
              ))}
            </div>
          </div>
        </div>

        <DialogFooter>
          <Button variant="outline" onClick={() => onOpenChange(false)}>
            取消
          </Button>
          <Button onClick={handleSubmit}>
            保存
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};
