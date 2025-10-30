import React from 'react';
import { DataModel } from '@/types/api';
import { RefreshCw } from 'lucide-react';
import { Button } from '@/components/ui/button';

interface HeaderProps {
  model?: DataModel | null;
  onRefresh?: () => void;
}

export const Header: React.FC<HeaderProps> = ({ model, onRefresh }) => {
  return (
    <div className="h-16 bg-white border-b border-gray-200 flex items-center justify-between px-6">
      <div>
        {model ? (
          <div>
            <h1 className="text-xl font-bold text-gray-900">{model.name}</h1>
            <p className="text-sm text-gray-500">
              {model.memo || `数据模型: ${model.id}`}
            </p>
          </div>
        ) : (
          <h1 className="text-xl font-bold text-gray-900">数据模型管理</h1>
        )}
      </div>
      
      {onRefresh && (
        <Button onClick={onRefresh} variant="outline" size="sm">
          <RefreshCw className="w-4 h-4 mr-2" />
          刷新
        </Button>
      )}
    </div>
  );
};
