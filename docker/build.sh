#!/usr/bin/zsh
mv ../target/*-dependencies.jar ./
docker build -t nekotori/neko-bot-osiris:1.0.3 .
docker push nekotori/neko-bot-osiris:1.0.3