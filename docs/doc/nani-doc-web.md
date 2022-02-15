# 项目名称
nani doc web（纳尼文档展示页面）

### 使用说明
1. docDist目录结构

```shell
docDist
    ├─projects.json
    ├─projectName1（项目文档目录）
    ├─projectName2（项目文档目录）
```
2. projects.json示例

```json
[
    {
        "projectTitle":"项目1",
        "projectName":"projectName1"
    },
    {
        "projectTitle":"项目2",
        "projectName":"projectName2"
    },
]
```

3. 启动示例

```shell
docker run -d -p 80:80 -e TZ="Asia/Shanghai" -v /data/docDist:/usr/share/nginx/html/docDist  -e LANG=en_US.UTF-8  lmm1990/nani-doc-web:latest
```
