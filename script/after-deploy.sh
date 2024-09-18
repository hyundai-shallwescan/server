#!/bin/bash
REPOSITORY=/home/api/

cd $REPOSITORY/api_back || { echo "Failed to navigate to $REPOSITORY/api_back"; exit 1; }

echo "> 🔵 Stop & Remove Docker services."
docker compose down || { echo "Failed to stop Docker services"; exit 1; }

echo "> 🟢 Run new Docker services."
docker compose up --build -d || { echo "Failed to start Docker services"; exit 1; }
