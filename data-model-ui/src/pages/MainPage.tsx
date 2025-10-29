import React, { useState } from 'react';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { DatabaseList } from '@/components/database/DatabaseList';
import { DataModelList } from '@/components/datamodel/DataModelList';
import { DataModelEditor } from '@/components/datamodel/DataModelEditor';
import { CrudDataManager } from '@/components/crud/CrudDataManager';
import { DataModel } from '@/types/api';
import { dataModelApi } from '@/services/api';

export const MainPage: React.FC = () => {
  const [activeTab, setActiveTab] = useState('database');
  const [editorOpen, setEditorOpen] = useState(false);
  const [editingModel, setEditingModel] = useState<DataModel | null>(null);
  const [selectedModelId, setSelectedModelId] = useState<string | null>(null);
  const [refreshKey, setRefreshKey] = useState(0);

  const handleCreateFromDb = async (dbName: string, tableName: string) => {
    try {
      const result = await dataModelApi.autoCreateFromDb(dbName, tableName);
      if (result.code === 0) {
        alert('模型创建成功');
        setActiveTab('model');
        setRefreshKey(k => k + 1);
      }
    } catch (error) {
      console.error('Failed to create model:', error);
      alert('创建失败');
    }
  };

  const handleEditModel = (model: DataModel) => {
    setEditingModel(model);
    setEditorOpen(true);
  };

  const handleCreateModel = () => {
    setEditingModel(null);
    setEditorOpen(true);
  };

  const handleModelSaved = () => {
    setRefreshKey(k => k + 1);
  };

  const handleSelectModel = (modelId: string) => {
    setSelectedModelId(modelId);
    setActiveTab('crud');
  };

  if (selectedModelId && activeTab === 'crud') {
    return (
      <CrudDataManager
        dataModelId={selectedModelId}
        onBack={() => {
          setSelectedModelId(null);
          setActiveTab('model');
        }}
      />
    );
  }

  return (
    <div className="container mx-auto py-8 px-4">
      <div className="mb-8">
        <h1 className="text-3xl font-bold mb-2">数据模型设计器</h1>
        <p className="text-gray-600">管理数据库、设计数据模型、进行数据操作</p>
      </div>

      <Tabs value={activeTab} onValueChange={setActiveTab}>
        <TabsList className="mb-4">
          <TabsTrigger value="database">数据库管理</TabsTrigger>
          <TabsTrigger value="model">数据模型</TabsTrigger>
        </TabsList>

        <TabsContent value="database">
          <DatabaseList onTableSelect={handleCreateFromDb} />
        </TabsContent>

        <TabsContent value="model">
          <DataModelList
            key={refreshKey}
            onEdit={handleEditModel}
            onCreate={handleCreateModel}
            onSelect={handleSelectModel}
          />
        </TabsContent>
      </Tabs>

      <DataModelEditor
        open={editorOpen}
        onOpenChange={setEditorOpen}
        model={editingModel}
        onSaved={handleModelSaved}
      />
    </div>
  );
};
