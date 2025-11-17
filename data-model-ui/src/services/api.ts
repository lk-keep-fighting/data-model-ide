import axios from 'axios';
import { ApiResult, Database, Table, DataModel, QueryInput, PageResult } from '@/types/api';

const api = axios.create({
  baseURL: '/api',
  timeout: 10000,
});

api.interceptors.response.use(
  (response) => response.data,
  (error) => {
    console.error('API Error:', error);
    return Promise.reject(error);
  }
);

export const databaseApi = {
  getDatabaseList: () => api.get<any, ApiResult<Database[]>>('/ide/database/list'),
  getTableList: (dbId: string) => api.get<any, ApiResult<Table[]>>(`/ide/database/${dbId}/tables`),
};

export const dataModelApi = {
  list: () => api.get<any, ApiResult<DataModel[]>>('/data-model/list'),
  get: (id: string) => api.get<any, ApiResult<DataModel>>(`/data-model/get/${id}`),
  delete: (id: string) => api.delete<any, ApiResult>(`/data-model/delete/${id}`),
  createOrSave: (id: string, data: DataModel) => api.post<any, ApiResult>(`/data-model/createOrSave/${id}`, data),
  autoCreateFromDb: (dbName: string, tableName: string) => 
    api.post<any, ApiResult>(`/data-model/autoCreate/db/${dbName}/table/${tableName}`),
  autoCreateFromTable: (tableName: string) => 
    api.post<any, ApiResult>(`/data-model/autoCreate/table/${tableName}`),
};

export const crudApi = {
  add: (dataModelId: string, data: any) => 
    api.post<any, ApiResult>(`/data-model/crud/${dataModelId}/add`, data),
  batchAdd: (dataModelId: string, data: any[]) => 
    api.post<any, ApiResult>(`/data-model/crud/${dataModelId}/batch-add`, data),
  edit: (dataModelId: string, dataId: string, data: any) => 
    api.put<any, ApiResult>(`/data-model/crud/${dataModelId}/edit/${dataId}`, data),
  get: (dataModelId: string, dataId: string) => 
    api.get<any, ApiResult>(`/data-model/crud/${dataModelId}/get/${dataId}`),
  query: (dataModelId: string, queryInput: QueryInput) => 
    api.post<any, ApiResult>(`/data-model/crud/${dataModelId}/query`, queryInput),
  queryPage: (dataModelId: string, queryInput: QueryInput) => 
    api.post<any, ApiResult<PageResult>>(`/data-model/crud/${dataModelId}/query-page`, queryInput),
  delete: (dataModelId: string, dataId: string) => 
    api.delete<any, ApiResult>(`/data-model/crud/${dataModelId}/delete/${dataId}`),
};
