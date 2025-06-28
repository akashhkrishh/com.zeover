#!/bin/bash

set -e  # Exit on any error

echo "🔧 Checking for Docker Compose installation..."
if ! command -v docker-compose &> /dev/null; then
    echo "Docker Compose could not be found. Please install it first."
    exit 1
fi

echo "🔍 Checking for Docker installation..."
if ! command -v docker &> /dev/null; then
    echo "Docker could not be found. Please install it first."
    exit 1
fi

echo "📦 Pulling latest Docker images..."
docker-compose pull

if [ $? -ne 0 ]; then
    echo "Failed to pull Docker images. Please check your Docker setup."
    exit 1
fi

echo "🔄 Removing old Docker images..."
docker images -q | xargs docker rmi -f || true
if [ $? -ne 0 ]; then
    echo "Failed to remove old Docker images. Continuing with the build."
fi

# docker-compose down --remove-orphans; docker-compose build; docker-compose up -d


echo "🧹 Cleaning up previous containers..."
docker-compose down --remove-orphans


echo "🐳 Building Docker images with Docker Compose..."
docker-compose build

echo "🚀 Starting containers..."
docker-compose up -d

echo "✅ App is running! Check with: docker-compose ps"
