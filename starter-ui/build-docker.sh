#!/usr/bin/env bash


if ! mvn clean package; then
    echo "Build failed"
    exit 1
fi

if ! docker build -t eclipse/starter-ui .; then
    echo "Docker build failed"
    exit 1
fi
