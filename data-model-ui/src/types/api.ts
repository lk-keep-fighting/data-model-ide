export interface ApiResult<T = any> {
  code: number;
  msg?: string;
  data?: T;
  debug?: any;
  error?: {
    code: number;
    msg: string;
    detail: any;
  };
}

export interface Database {
  [key: string]: any;
}

export interface Table {
  [key: string]: any;
}

export interface DataModel {
  id: string;
  name: string;
  type?: string;
  group?: string;
  memo?: string;
  mainTable?: string;
  version?: string;
  columns?: DataModelColumn[];
  conditions?: any[];
  joins?: any[];
  tableMap?: Record<string, any>;
  columnMap?: Record<string, any>;
}

export interface DataModelColumn {
  column: string;
}

export interface QueryInput {
  conditions?: any[];
  orderBy?: string[];
  page?: number;
  pageSize?: number;
}

export interface PageResult<T = any> {
  list: T[];
  total: number;
  page: number;
  pageSize: number;
}
