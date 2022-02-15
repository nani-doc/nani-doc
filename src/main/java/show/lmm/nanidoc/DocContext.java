package show.lmm.nanidoc;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.thoughtworks.qdox.JavaProjectBuilder;
import com.thoughtworks.qdox.directorywalker.DirectoryScanner;
import com.thoughtworks.qdox.directorywalker.SuffixFilter;
import com.thoughtworks.qdox.library.ClassLibraryBuilder;
import com.thoughtworks.qdox.library.SortedClassLibraryBuilder;
import com.thoughtworks.qdox.model.JavaAnnotation;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import show.lmm.nanidoc.core.Constant;
import show.lmm.nanidoc.model.*;
import show.lmm.nanidoc.model.search.DocInfo;
import show.lmm.nanidoc.model.search.SearchIndex;
import show.lmm.nanidoc.utils.DocUtil;
import show.lmm.nanidoc.utils.FileUtil;
import show.lmm.nanidoc.utils.HashMapUtil;
import show.lmm.nanidoc.utils.LoadSourceCoceUtil;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.ansi;

/**
 * 文档context
 */
public class DocContext {

    private DocAction docAction = new DocAction();

    /**
     * 生成文档
     *
     * @param args 文档生成参数
     */
    public void generate(DocGenerateArgs args) {
        final Collection<String> wraperClasss = new HashSet<>();
        for (String item : args.getWraperClasses()) {
            wraperClasss.add(item);
        }

        ClassLibraryBuilder libraryBuilder = new SortedClassLibraryBuilder();
        LoadSourceCoceUtil.loadSourceCode(args.getSourceJars(), new HashSet<>(), new HashSet<>(), libraryBuilder);
        JavaProjectBuilder javaProjectBuilder = new JavaProjectBuilder(libraryBuilder);
        DirectoryScanner scanner = new DirectoryScanner(new File(args.getProjectSourcePath()));
        scanner.addFilter(new SuffixFilter(".java"));
        scanner.scan(currentFile -> {
            if (currentFile.getName().contains("Mapper")) {
                return;
            }
            try {
                javaProjectBuilder.addSource(new BufferedReader(new InputStreamReader(new FileInputStream(currentFile),"utf-8")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Collection<JavaClass> classeList = javaProjectBuilder.getClasses();
        Map<String, JavaClass> classMap = HashMapUtil.init(classeList.size());
        classeList.forEach((item) -> {
            classMap.put(item.getBinaryName(), item);
        });
        System.out.println(ansi().fg(GREEN)
                .a("加载java文件成功").reset());
        System.out.println(ansi().fg(GREEN)
                .a("文档生成中……").reset());

        //模块页面列表 map[模块名称,map[controller名称,list[页面]]]
        Map<String, Map<String, List<PageInfo>>> modulePageMap = new HashMap<>();
        for (JavaClass controller : classeList) {
            if (!controller.getPackageName().startsWith(args.getPackageName())) {
                continue;
            }
            Map<String, JavaAnnotation> classAnnotationsList = DocUtil.getAnnotationsList(controller.getAnnotations());
            String requestMappingAnnotationName = DocUtil.getFirstJointAnnotation(classAnnotationsList.keySet(), Constant.requestMappingAnnotationList);
            //跳过非controller class
            if (requestMappingAnnotationName.isEmpty()) {
                continue;
            }
            String mappingUrl = classAnnotationsList.get(requestMappingAnnotationName).getNamedParameter("value").toString();
            StringBuilder url = new StringBuilder();
            url.append(mappingUrl.replace("\"", ""));
            if (url.toString().endsWith("/")) {
                url.delete(url.length() - 1, url.length());
            }

            String moduleName = getModuleName(url);
            if (!modulePageMap.containsKey(moduleName)) {
                modulePageMap.put(moduleName, new HashMap<>());
            }
            final String baseUrl = url.toString();
            for (JavaMethod action : controller.getMethods()) {
                PageInfo pageInfo = docAction.build(controller, action, baseUrl, args.getDocOutPath(), moduleName, classMap, wraperClasss);
                if (pageInfo == null) {
                    continue;
                }
                if (!modulePageMap.get(moduleName).containsKey(pageInfo.getControllerName())) {
                    modulePageMap.get(moduleName).put(pageInfo.getControllerName(), new ArrayList<>());
                }
                modulePageMap.get(moduleName).get(pageInfo.getControllerName()).add(pageInfo);
            }
        }
        generateSearchIndex(args, modulePageMap);
        List<ModuleInfo> moduleList = generateCatalog(args, modulePageMap);
        ProjectDocInfo projectDocInfo = new ProjectDocInfo() {{
            setProjectName(args.getProjectDocConfig().getProjectName());
            setProjectTitle(args.getProjectDocConfig().getProjectTitle());
            setModules(moduleList);
        }};
        FileUtil.writeAllText(String.format("%s/index.json", args.getDocOutPath()), JSONObject.toJSONString(projectDocInfo));
    }

    /**
     * 生成目录
     *
     * @param args          文档生成参数
     * @param modulePageMap 模块页面列表 map[模块名称,map[controller名称,list[页面]]]
     * @return 目录信息列表
     */
    private List<ModuleInfo> generateCatalog(DocGenerateArgs args, Map<String, Map<String, List<PageInfo>>> modulePageMap) {
        FileUtil.notExistsDirectoryCreate(String.format("%s/catalog", args.getDocOutPath()));
        List<ModuleInfo> moduleList = new ArrayList<>();
        Collection<String> moduleNames = new HashSet<>();
        modulePageMap.forEach((moduleName, controllerInfo) -> {
            List<CataLogInfo> cataLogList = new ArrayList<>();
            controllerInfo.forEach((controllerName, pageList) -> {
                cataLogList.add(new CataLogInfo() {{
                    setControllerName(controllerName);
                    setControllerTitle(pageList.get(0).getControllerTitle());
                    setPageList(pageList);
                }});
            });

            String catalogPath = String.format("catalog/%s.json", moduleName);
            FileUtil.writeAllText(String.format("%s/%s", args.getDocOutPath(), catalogPath), JSONArray.toJSONString(cataLogList));
            controllerInfo.forEach((controllerName, pageList) -> {
                ModuleInfo moduleInfo = new ModuleInfo() {{
                    setName(moduleName);
                    setTitle(args.getProjectDocConfig().getModuleMap().getOrDefault(moduleName, moduleName));
                }};
                if (moduleNames.contains(moduleName)) {
                    return;//continue
                }
                moduleNames.add(moduleName);
                if (moduleName.equals(args.getProjectDocConfig().getDefaultModuleName())) {
                    moduleList.add(0, moduleInfo);
                } else {
                    moduleList.add(moduleInfo);
                }
            });
        });
        return moduleList;
    }

    /**
     * 获得模块名称
     *
     * @param url 请求Url
     * @return 模块名称
     */
    private String getModuleName(StringBuilder url) {
        String moduleName;
        int index = url.lastIndexOf("/");
        if (index > -1) {
            moduleName = url.substring(0, index);
            index = moduleName.lastIndexOf("/");
            if (index > -1) {
                moduleName = moduleName.substring(index + 1);
            }
        } else {
            moduleName = url.toString();
        }
        return moduleName;
    }

    /**
     * 生成搜索索引
     *
     * @param args:          文档生成参数
     * @param modulePageMap: 模块页面列表
     * @since 刘明明/2022-01-30 14:35:39
     **/
    private void generateSearchIndex(DocGenerateArgs args, Map<String, Map<String, List<PageInfo>>> modulePageMap) {
        final String searchIndexPath = String.format("%s/searchIndex", args.getDocOutPath());
        FileUtil.notExistsDirectoryCreate(searchIndexPath);
        List<DocInfo> docs = new ArrayList<>();
        AtomicInteger pageCount = new AtomicInteger();
        AtomicInteger indexFileIndex = new AtomicInteger(1);
        modulePageMap.forEach((moduleName, controllers) -> {
            controllers.forEach((controllerName, actions) -> {
                actions.forEach((item) -> {
                    docs.add(new DocInfo() {{
                        setPath(String.format("/%s", item.getPath().replace(".json", "")));
                        setModuleTitle(args.getProjectDocConfig().getModuleMap().getOrDefault(moduleName, moduleName));
                        setControllerTitle(item.getControllerTitle());
                        setActionTitle(item.getName());
                        setActionUrl(item.getUrl());
                    }});
                    if (docs.size() == args.getSearchIndexPageSize()) {
                        FileUtil.writeAllText(String.format("%s/%d.json", searchIndexPath, indexFileIndex.get()), JSONArray.toJSONString(docs));
                        docs.clear();
                        indexFileIndex.getAndIncrement();
                    }
                    pageCount.getAndIncrement();
                });
            });
        });
        if (!docs.isEmpty()) {
            FileUtil.writeAllText(String.format("%s/%d.json", searchIndexPath, indexFileIndex.get()), JSONArray.toJSONString(docs));
        }
        SearchIndex searchIndex = new SearchIndex(pageCount.get(), args.getSearchIndexPageSize());
        FileUtil.writeAllText(String.format("%s/index.json", searchIndexPath), JSONArray.toJSONString(searchIndex));
    }
}
