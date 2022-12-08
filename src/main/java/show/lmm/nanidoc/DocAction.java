package show.lmm.nanidoc;

import com.alibaba.fastjson2.JSONObject;
import show.lmm.nanidoc.core.Constant;
import show.lmm.nanidoc.model.MethodItem;
import show.lmm.nanidoc.model.PageInfo;
import show.lmm.nanidoc.model.RequestMethod;
import com.thoughtworks.qdox.model.JavaAnnotation;
import com.thoughtworks.qdox.model.JavaClass;
import com.thoughtworks.qdox.model.JavaMethod;
import com.thoughtworks.qdox.model.JavaType;
import com.thoughtworks.qdox.model.impl.DefaultJavaParameterizedType;
import show.lmm.nanidoc.utils.DocUtil;
import show.lmm.nanidoc.utils.FileUtil;
import show.lmm.nanidoc.utils.TypeUtil;
import show.lmm.nanidoc.utils.Util;

import java.util.*;

/**
 * 控制器方法文档
 */
public class DocAction {

    /**
     * 编译文档
     *
     * @param controller:
     * @param action:
     * @param baseUrl      基础url
     * @param docOutPath:
     * @param moduleName:
     * @param classMap:
     * @param wraperClasss 包裹类列表
     * @return com.github.lmm1990.nanidoc.model.PageInfo
     * @since 刘明明/2022-01-13 17:25:24
     **/
    public PageInfo build(JavaClass controller, JavaMethod action, final String baseUrl, String docOutPath, String moduleName, Map<String, JavaClass> classMap, Collection<String> wraperClasss) {
        Map<String, JavaAnnotation> methodAnnotationsList = DocUtil.getAnnotationsList(action.getAnnotations());
        String requestMappingAnnotationName = DocUtil.getFirstJointAnnotation(methodAnnotationsList.keySet(), Constant.requestMappingAnnotationList);
        //跳过非mapping方法
        if (requestMappingAnnotationName.isEmpty()) {
            return null;
        }

        final StringBuilder url = new StringBuilder(baseUrl);
        url.append(methodAnnotationsList.get(requestMappingAnnotationName).getNamedParameter("value").toString().trim());
        //处理返回值
        JavaType baseResponseClass = action.getReturnType();


        final JavaType responseClass = getResponseClass(wraperClasss,baseResponseClass);

        Collection<JavaType> responseJavaTypes = new HashSet<JavaType>(){{
            add(responseClass);
        }};
        final List<JavaType> realResponseJavaTypes = ((DefaultJavaParameterizedType) responseClass).getActualTypeArguments();
        realResponseJavaTypes.forEach((item) -> {
            responseJavaTypes.add(item);
            TypeUtil.getGenericTypes((JavaClass) item).forEach((javaType -> responseJavaTypes.add(javaType)));
        });

        String title = action.getComment();
        String controllerName = Util.firstCharToLowerCase(controller.getName().replace("Controller", ""));
        String relativeDocPath = String.format("%s/%s", moduleName, controllerName);
        FileUtil.notExistsDirectoryCreate(String.format("%s/%s", docOutPath, relativeDocPath));
        relativeDocPath = String.format("%s/%s.json", relativeDocPath, Util.firstCharToLowerCase(action.getName()));
        String finalUrl = url.toString().replaceAll("\"", "").replaceAll("//", "/");
        writeDoc(String.format("%s/%s", docOutPath, relativeDocPath), new MethodItem() {{
            setTitle(title);
            setUrl(finalUrl);
            setRequestParamList(TypeUtil.buildJavaParameters(action, classMap, true));
            setResponseTypeList(TypeUtil.buildTypeScriptInfo(new ArrayList<>(), responseJavaTypes, classMap));
            setRequestMethod(buildRequestMethod(title, action, controller.getName(), ((JavaClass) responseClass), finalUrl));
        }});
        PageInfo pageInfo = new PageInfo() {{
            setName(title);
            setUrl(finalUrl);
            setControllerName(controllerName);
            setControllerTitle(controller.getComment());
        }};
        pageInfo.setPath(relativeDocPath);
        return pageInfo;
    }

    private JavaType getResponseClass(Collection<String> wraperClasss,JavaType baseResponseClass){
        Collection<JavaType> responseJavaTypes = new HashSet<>();
        final List<JavaType> realResponseJavaTypes = ((DefaultJavaParameterizedType) baseResponseClass).getActualTypeArguments();
        realResponseJavaTypes.forEach((item) -> {
            responseJavaTypes.add(item);
            TypeUtil.getGenericTypes((JavaClass) item).forEach((responseJavaTypes::add));
        });
        boolean hasWraperClasss = wraperClasss.contains(baseResponseClass.getBinaryName());
        if (hasWraperClasss) {
            if (!realResponseJavaTypes.isEmpty()) {
                baseResponseClass = realResponseJavaTypes.get(0);
            }
        } else {
            responseJavaTypes.add(baseResponseClass);
            TypeUtil.getGenericTypes((JavaClass) baseResponseClass).forEach((responseJavaTypes::add));
        }
        if(wraperClasss.contains(baseResponseClass.getBinaryName())){
            return getResponseClass(wraperClasss, baseResponseClass);
        }
        return baseResponseClass;
    }

    /**
     * 编译请求方法
     */
    private static RequestMethod buildRequestMethod(String title, JavaMethod action, String className, JavaClass responseClass, String url) {
        final String methodName = action.getName();
        return new RequestMethod() {{
            setTitle(title);
            setRequestMethod(DocUtil.getEntityName(action, true));
            setClassName(className.replace("Controller", ""));
            setResponseGenericClassName(getGenericTypeName((DefaultJavaParameterizedType) responseClass, false));
            setResponseClassName(responseClass.getName());
            setUrl(url);
            setMethodName(methodName);
        }};
    }

    /**
     * 获得泛型类型名称
     *
     * @param javaClass java类
     * @param isArray   是否是数组
     * @return
     */
    private static String getGenericTypeName(DefaultJavaParameterizedType javaClass, boolean isArray) {
        StringBuilder genericType = new StringBuilder();
        boolean currentIsArray = TypeUtil.isArray(javaClass);
        if (!currentIsArray) {
            final String typeName = javaClass.getName();
            genericType.append(TypeUtil.getJavaScriptTypeName(typeName, typeName));
        }
        if (isArray) {
            genericType.append("[]");
        }
        final List<JavaType> realResponseJavaTypes = javaClass.getActualTypeArguments();
        if (realResponseJavaTypes.isEmpty()) {
            return genericType.toString();
        }
        if (!currentIsArray) {
            genericType.append("<");
        }
        List<String> genericItems = new ArrayList<>();
        for (JavaType item : realResponseJavaTypes) {
            isArray = TypeUtil.isArray(javaClass);
            javaClass = (DefaultJavaParameterizedType) item;
            genericItems.add(getGenericTypeName(javaClass, isArray));
        }
        genericType.append(String.join(",", genericItems));
        if (!currentIsArray) {
            genericType.append(">");
        }
        return genericType.toString();
    }

    /**
     * 输出文档
     *
     * @param docOutPath 文档输出路径
     * @param info       文档信息
     */
    private static void writeDoc(String docOutPath, MethodItem info) {
        FileUtil.writeAllText(docOutPath, JSONObject.toJSONString(info));
    }
}
