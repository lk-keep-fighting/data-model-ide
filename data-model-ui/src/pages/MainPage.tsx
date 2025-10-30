import React, { useState, useEffect } from 'react';
import { Sidebar } from '@/components/layout/Sidebar';
import { Header } from '@/components/layout/Header';
import { DatabaseList } from '@/components/database/DatabaseList';
import { DataModelEditor } from '@/components/datamodel/DataModelEditor';
import { CrudDataManager } from '@/components/crud/CrudDataManager';
import { DataModel } from '@/types/api';
import { dataModelApi } from '@/services/api';
import { Dialog, DialogContent, DialogHeader, DialogTitle } from '@/components/ui/dialog';

type ViewMode = 'crud' | 'database' | 'settings';

export const MainPage: React.FC = () => {
  const [viewMode, setViewMode] = useState<ViewMode>('crud');
  const [selectedModelId, setSelectedModelId] = useState<string | null>(null);
  const [currentModel, setCurrentModel] = useState<DataModel | null>(null);
  const [editorOpen, setEditorOpen] = useState(false);
  const [editingModel, setEditingModel] = useState<DataModel | null>(null);
  const [databaseDialogOpen, setDatabaseDialogOpen] = useState(false);
  const [refreshKey, setRefreshKey] = useState(0);

  useEffect(() => {
    if (selectedModelId) {
      loadCurrentModel();
    }
  }, [selectedModelId]);

  const loadCurrentModel = async () => {
    if (!selectedModelId) return;
    try {
      const result = await dataModelApi.get(selectedModelId);
      if (result.code === 0 && result.data) {
        setCurrentModel(result.data);
      }
    } catch (error) {
      console.error('Failed to load model:', error);
    }
  };

  const handleSelectModel = (modelId: string) => {
    setSelectedModelId(modelId);
    setViewMode('crud');
  };

  const handleOpenDatabase = () => {
    setDatabaseDialogOpen(true);
  };

  const handleOpenSettings = () => {
    setViewMode('settings');
  };

  const handleCreateModel = () => {
    setEditingModel(null);
    setEditorOpen(true);
  };

  const handleModelSaved = () => {
    setRefreshKey(k => k + 1);
    loadCurrentModel();
  };

  const handleCreateFromDb = async (dbName: string, tableName: string) => {
    try {
      const result = await dataModelApi.autoCreateFromDb(dbName, tableName);
      if (result.code === 0) {
        alert('模型创建成功');
        setDatabaseDialogOpen(false);
        setRefreshKey(k => k + 1);
      }
    } catch (error) {
      console.error('Failed to create model:', error);
      alert('创建失败');
    }
  };

  const handleRefresh = () => {
    setRefreshKey(k => k + 1);
    loadCurrentModel();
  };

  return (
    <div className="flex h-screen overflow-hidden bg-gray-50">
      {/* Left Sidebar */}
      <Sidebar
        selectedModelId={selectedModelId}
        onSelectModel={handleSelectModel}
        onOpenDatabase={handleOpenDatabase}
        onOpenSettings={handleOpenSettings}
        onCreateModel={handleCreateModel}
        refreshKey={refreshKey}
      />

      {/* Main Content */}
      <div className="flex-1 flex flex-col overflow-hidden">
        {/* Header */}
        <Header model={currentModel} onRefresh={handleRefresh} />

        {/* Content Area */}
        <div className="flex-1 overflow-auto p-6">
          {viewMode === 'crud' && selectedModelId && (
            <div key={`${selectedModelId}-${refreshKey}`}>
              <CrudDataManager dataModelId={selectedModelId} />
            </div>
          )}

          {viewMode === 'crud' && !selectedModelId && (
            <div className="flex items-center justify-center h-full">
              <div className="text-center text-gray-500">
                <p className="text-lg mb-2">请从左侧选择一个数据模型</p>
                <p className="text-sm">或点击"创建模型"按钮创建新模型</p>
              </div>
            </div>
          )}

          {viewMode === 'settings' && (
            <div className="max-w-4xl mx-auto">
              <div className="bg-white rounded-lg shadow p-6">
                <h2 className="text-2xl font-bold mb-4">系统设置</h2>
                <p className="text-gray-600">设置功能开发中...</p>
              </div>
            </div>
          )}
        </div>
      </div>

      {/* Database Dialog */}
      <Dialog open={databaseDialogOpen} onOpenChange={setDatabaseDialogOpen}>
        <DialogContent className="max-w-6xl h-[85vh]">
          <DialogHeader>
            <DialogTitle>数据库管理</DialogTitle>
          </DialogHeader>
          <div className="flex-1 overflow-hidden">
            <DatabaseList onTableSelect={handleCreateFromDb} />
          </div>
        </DialogContent>
      </Dialog>

      {/* Model Editor Dialog */}
      <DataModelEditor
        open={editorOpen}
        onOpenChange={setEditorOpen}
        model={editingModel}
        onSaved={handleModelSaved}
      />
    </div>
  );
};
