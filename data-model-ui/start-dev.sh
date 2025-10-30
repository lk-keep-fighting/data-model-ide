#!/bin/bash

# 数据模型设计器 - 前端开发启动脚本

echo "==================================="
echo "数据模型设计器 - 前端开发环境"
echo "==================================="
echo ""

# 检查依赖
if [ ! -d "node_modules" ]; then
  echo "📦 首次运行，正在安装依赖..."
  npm install
  echo ""
fi

# 启动开发服务器
echo "🚀 启动开发服务器..."
echo "前端: http://localhost:3000"
echo "后端API代理: http://localhost:8080"
echo ""
echo "请确保后端服务已启动！"
echo "后端启动命令: cd ../data-model-ide && mvn spring-boot:run"
echo ""

npm run dev
