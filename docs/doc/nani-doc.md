# NaNi Doc（纳尼文档）

基于java doc注释生成文档，零侵入。

## 快速开始

1. 源码目录启动

```shell
curl -L https://s.lmm.show/nani-doc/run.sh | bash -s -- -package show.lmm.controller -sourceJars "./libs/sources/commonBase-2.1.1-sources.jar,./libs/sources/spring-web-5.3.13-sources.jar" -wraperClasses "reactor.core.publisher.Mono"
```
2. 启动web界面

```shell
docker run -d -p 80:80 -e TZ="Asia/Shanghai" -v /data/docDist:/usr/share/nginx/html/docDist  -e LANG=en_US.UTF-8  lmm1990/nani-doc-web:latest
```

> ### 说明
> web界面启动参数，参考 [NaNi Doc Web](./?nani-doc-web)
 

## 📖 命令行参数

| 参数名           | 是否必填 | 标题           | 描述               | 示例                          |
|---------------|------|--------------|------------------|-----------------------------|
| package       | √    | controller包名 |                  | show.lmm.controller         |
| sourceJars    | ×    | 第三方源码jar包列表  | 多个以英文逗号分割        | c:/a.jar,d:/b.jar           |
| sourcePath    | ×    | 源码目录         | 默认为当前目录          |                             |
| docOutPath    | ×    | 文档输出目录       | 默认为：./distDoc 目录 |                             |
| wraperClasses | ×    | 包裹class列表    | 默认为空，多个以英文逗号分割   | reactor.core.publisher.Mono |

## 💿 调用示例

```shell
java -jar .\nani-doc-1.0.jar -package show.lmm.controller -sourceJars "./libs/sources/commonBase-2.1.1-sources.jar,./libs/sources/spring-web-5.3.13-sources.jar" -wraperClasses "reactor.core.publisher.Mono" -sourcePath "D:\project\testProject"
```
