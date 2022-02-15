# NaNi Doc（纳尼文档）

基于java doc注释生成文档，零侵入。

## 快速开始

1. 源码目录启动

linux

```shell
curl -L https://s.lmm.show/nani-doc/run.sh | bash -s -- -package show.lmm.controller -sourceJars "./libs/sources/commonBase-2.1.1-sources.jar,./libs/sources/spring-web-5.3.13-sources.jar" -wraperClasses "reactor.core.publisher.Mono"
```
windows

```shell
.\run.bat -package show.lmm.controller -sourceJars "./libs/sources/commonBase-2.1.1-sources.jar,./libs/sources/spring-web-5.3.13-sources.jar" -wraperClasses "reactor.core.publisher.Mono" -sourcePath ".." -docOutPath "../dist/docDist/demo"
```

2. 启动web界面，参考 https://github.com/nani-doc/nani-doc-web

## 📖 文档

- [中国用户](https://nani-doc.gitee.io)
- [海外用户](https://nanidoc.lmm.show)

## 💿 调用示例

```shell
java -jar .\nani-doc-1.0.jar -package show.lmm.controller -sourceJars "./libs/sources/commonBase-2.1.1-sources.jar,./libs/sources/spring-web-5.3.13-sources.jar" -wraperClasses "reactor.core.publisher.Mono" -sourcePath "D:\project\testProject"
```
