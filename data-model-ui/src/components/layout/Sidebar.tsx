import React, { useEffect, useState } from 'react';
import { dataModelApi } from '@/services/api';
import { DataModel } from '@/types/api';
import { Database, FileText, Plus, Settings } from 'lucide-react';
import { cn } from '@/lib/utils';

interface SidebarProps {
  selectedModelId: string | null;
  onSelectModel: (modelId: string) => void;
  onOpenDatabase: () => void;
  onOpenSettings: () => void;
  onCreateModel: () => void;
  refreshKey?: number;
}

export const Sidebar: React.FC<SidebarProps> = ({
  selectedModelId,
  onSelectModel,
  onOpenDatabase,
  onOpenSettings,
  onCreateModel,
  refreshKey,
}) => {
  const [models, setModels] = useState<DataModel[]>([]);
  const [collapsed, setCollapsed] = useState(false);

  const loadModels = async () => {
    try {
      const result = await dataModelApi.list();
      if (result.code === 0 && result.data) {
        setModels(result.data);
        if (!selectedModelId && result.data.length > 0) {
          onSelectModel(result.data[0].id);
        }
      }
    } catch (error) {
      console.error('Failed to load models:', error);
    }
  };

  useEffect(() => {
    loadModels();
  }, [refreshKey]);

  return (
    <div className={cn(
      "h-screen bg-slate-900 text-white flex flex-col transition-all duration-300",
      collapsed ? "w-16" : "w-64"
    )}>
      {/* Logo */}
      <div className="h-16 flex items-center justify-between px-4 border-b border-slate-700">
        {!collapsed && (
          <div className="flex items-center gap-2">
            <FileText className="w-6 h-6 text-blue-400" />
            <span className="font-bold text-lg">数据模型</span>
          </div>
        )}
        <button
          onClick={() => setCollapsed(!collapsed)}
          className="p-2 hover:bg-slate-800 rounded-md"
        >
          {collapsed ? '→' : '←'}
        </button>
      </div>

      {/* Menu Items */}
      <div className="flex-1 overflow-y-auto py-4">
        {/* Action Buttons */}
        <div className="px-3 mb-4 space-y-2">
          <button
            onClick={onCreateModel}
            className="w-full flex items-center gap-3 px-3 py-2 rounded-md bg-blue-600 hover:bg-blue-700 transition-colors"
            title="创建模型"
          >
            <Plus className="w-5 h-5" />
            {!collapsed && <span>创建模型</span>}
          </button>
          
          <button
            onClick={onOpenDatabase}
            className="w-full flex items-center gap-3 px-3 py-2 rounded-md hover:bg-slate-800 transition-colors"
            title="数据库"
          >
            <Database className="w-5 h-5" />
            {!collapsed && <span>数据库</span>}
          </button>
          
          <button
            onClick={onOpenSettings}
            className="w-full flex items-center gap-3 px-3 py-2 rounded-md hover:bg-slate-800 transition-colors"
            title="设置"
          >
            <Settings className="w-5 h-5" />
            {!collapsed && <span>设置</span>}
          </button>
        </div>

        {/* Model List */}
        {!collapsed && (
          <>
            <div className="px-4 mb-2">
              <div className="text-xs text-slate-400 uppercase font-semibold">数据模型列表</div>
            </div>
            <div className="px-3 space-y-1">
              {models.map((model) => (
                <button
                  key={model.id}
                  onClick={() => onSelectModel(model.id)}
                  className={cn(
                    "w-full text-left px-3 py-2 rounded-md transition-colors",
                    selectedModelId === model.id
                      ? "bg-blue-600 text-white"
                      : "hover:bg-slate-800 text-slate-300"
                  )}
                >
                  <div className="font-medium truncate">{model.name}</div>
                  {model.memo && (
                    <div className="text-xs text-slate-400 truncate">{model.memo}</div>
                  )}
                </button>
              ))}
            </div>
          </>
        )}
      </div>

      {/* Footer */}
      {!collapsed && (
        <div className="p-4 border-t border-slate-700 text-xs text-slate-400">
          <div>共 {models.length} 个模型</div>
        </div>
      )}
    </div>
  );
};
