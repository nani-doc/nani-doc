package show.lmm.nanidoc.model;

import com.alibaba.fastjson.JSONObject;
import lombok.Getter;
import lombok.Setter;
import show.lmm.nanidoc.core.DocException;
import show.lmm.nanidoc.utils.FileUtil;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;

/**
 * 文档生成参数
 *
 * @author 刘明明
 * @date 2022-01-30 11:59
 */
public class DocGenerateArgs {

    /**
     * 项目源码目录
     */
    @Getter
    private String projectSourcePath;

    /**
     * 包名
     */
    @Getter
    @Setter
    private String packageName;

    /**
     * jar源码列表，支持目录、文件
     */
    @Getter
    private String[] sourceJars;

    /**
     * 文档输出目录
     */
    @Getter
    private String docOutPath;

    /**
     * 包裹类列表
     */
    @Getter
    @Setter
    private String[] wraperClasses;

    /**
     * 项目文档配置
     */
    @Getter
    private ProjectDocConfig projectDocConfig;

    /**
     * 搜索索引每页数量
     */
    @Getter
    @Setter
    private int searchIndexPageSize = 10;

    /**
     * 设置项目源码目录
     *
     * @param projectSourcePath: 项目源码目录
     * @since 刘明明/2022-01-30 12:10:57
     **/
    public void setProjectSourcePath(String projectSourcePath) {
        projectSourcePath = getCanonicalPath(projectSourcePath);
        String projectDocConfigPath = String.format("%s/nani-doc.json", projectSourcePath);
        if (!FileUtil.exists(projectDocConfigPath, false)) {
            throw new DocException("文档配置文件：nani-doc.json 不存在");
        }
        try {
            projectDocConfig = JSONObject.parseObject(FileUtil.readAllText(projectDocConfigPath), ProjectDocConfig.class);
        } catch (Exception e) {
            throw new DocException("文档配置文件：nani-doc.json 配置信息有误");
        }
        if (projectDocConfig == null) {
            throw new DocException("文档配置文件：nani-doc.json 配置信息有误");
        }
        this.projectSourcePath = projectSourcePath;
    }

    /**
     * 设置文档输出目录
     *
     * @param docOutPath:
     * @since 刘明明/2022-01-30 12:05:43
     **/
    public void setDocOutPath(String docOutPath) {
        docOutPath = getCanonicalPath(docOutPath);
        if (docOutPath.endsWith("/")) {
            docOutPath = docOutPath.substring(0, docOutPath.length() - 1);
        }
        this.docOutPath = docOutPath;
    }

    /**
     * 设置jar源码列表，支持目录、文件
     *
     * @param sourceJars: jar源码列表
     * @since 刘明明/2022-02-07 15:55:50
     **/
    public void setSourceJars(String[] sourceJars) {
        Collection<String> paths = new HashSet<>();
        String path;
        for (String item : sourceJars) {
            path = getCanonicalPath(item);
            if(!path.isEmpty()){
                paths.add(path);
            }
        }
        String[] finalSourceJars = new String[paths.size()];
        paths.toArray(finalSourceJars);
        this.sourceJars = finalSourceJars;
    }

    /**
     * 获得绝对路径
     *
     * @param path: 路径
     * @return java.lang.String
     * @since 刘明明/2022-02-07 15:59:17
     **/
    private String getCanonicalPath(String path){
        if(path==null||path.isEmpty()){
            return "";
        }
        try{
            return new File(path).getCanonicalPath();
        }catch (Exception e){
            throw new RuntimeException(String.format("path：%s not found",path));
        }
    }
}
