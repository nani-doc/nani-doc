package show.lmm.nanidoc.utils;

import show.lmm.nanidoc.model.ModelInfo;
import show.lmm.nanidoc.model.TypeInfo;
import com.thoughtworks.qdox.model.*;
import com.thoughtworks.qdox.model.impl.DefaultJavaParameterizedType;

import java.util.*;

/**
 * 类型工具类
 *
 * @author 刘明明
 * @date 2022-01-14 13:53
 */
public class TypeUtil {

    private static final Collection<String> arrayTypes = new HashSet<String>() {{
        add("java.util.List");
        add("List");
        add("Collection");
    }};

    private static Collection<String> basicTypes = new HashSet<String>() {{
        add("Byte");
        add("Character");
        add("String");
        add("Short");
        add("Integer");
        add("Float");
        add("Double");
        add("Boolean");
        add("long");
        add("Long");
    }};

    /**
     * js类型
     */
    private static Map<String, String> javaScriptTypeMap = new HashMap<String, String>() {{
        put("String", "string");
        put("int", "number");
        put("long", "number");
        put("Long", "number");
        put("Double", "number");
        put("Integer", "number");
        put("Boolean", "boolean");
        put("boolean", "boolean");
        put("float", "number");
        put("Date", "string");
        put("java.util.Date", "string");
        put("LocalDateTime", "string");
        put("java.time.LocalDateTime", "string");
    }};

    /**
     * 获得js类型
     *
     * @param javaTypeName    java类型名称
     * @param defaultTypeName 默认类型名称
     * @return
     */
    public static String getJavaScriptTypeName(String javaTypeName, String defaultTypeName) {
        return javaScriptTypeMap.getOrDefault(javaTypeName, defaultTypeName);
    }

    /**
     * 判断是否是基础类型
     *
     * @param type: 类型
     * @return boolean
     * @since 刘明明/2022-01-14 14:00:07
     **/
    public static boolean isBasicType(JavaClass type) {
        if (type.isPrimitive()) {
            return true;
        }
        return basicTypes.contains(type.getName());
    }

    /**
     * 判断是否是数组
     *
     * @param type 类型
     * @return
     */
    public static boolean isArray(JavaClass type) {
        return isArray(type.getName());
    }

    /**
     * 判断是否是数组
     *
     * @param typeName 类型名称
     * @return
     */
    public static boolean isArray(String typeName) {
        return arrayTypes.contains(typeName);
    }

    /**
     * 获得泛型类型列表
     *
     * @param type java类型
     * @return
     */
    public static List<JavaType> getGenericTypes(JavaClass type) {
        List<JavaType> types = new ArrayList<>();
        ((DefaultJavaParameterizedType) type).getActualTypeArguments().forEach((item) -> {
            types.add(item);
            getGenericTypes((JavaClass) item);
        });
        return types;
    }

    /**
     * 获得类型名称
     * @param type java类型
     * @return
     */
    public static String getTypeName(JavaClass type){
        List<String> params = new ArrayList<>();
        type.getTypeParameters().forEach(item->params.add(item.getName()));
        StringBuilder typeName = new StringBuilder();
        typeName.append(type.getName());
        if(!params.isEmpty()){
            typeName.append("<");
            typeName.append(String.join(",",params));
            typeName.append(">");
        }
        return typeName.toString();
    }

    /**
     * 编译参数
     *
     * @param action          java方法
     * @param classMap        加载的类列表
     * @param isRequestParams 是否是request请求参数
     * @return
     */
    public static List<TypeInfo> buildJavaParameters(JavaMethod action, Map<String, JavaClass> classMap, boolean isRequestParams) {
        final List<JavaParameter> parameters = action.getParameters();
        List<TypeInfo> typeInfos = new ArrayList<>();
        if (parameters == null || parameters.isEmpty()) {
            return typeInfos;
        }
        if (parameters.size() > 1) {
            final String typeName = DocUtil.getEntityName(action, isRequestParams);
            Collection<String> models = new HashSet<String>() {{
                add(typeName);
            }};
            List<TypeInfo> fields = new ArrayList<>();
            final Map<String, String> paramComments = getParamComments(action.getTags());
            parameters.forEach(item -> {
                fields.add(new TypeInfo() {{
                    setType(DocUtil.getJavaScriptType((JavaClass) item.getType()));
                    setTitle(paramComments.getOrDefault(item.getName(), ""));
                    setName(item.getName());
                }});
                if (!isBasicType(item.getJavaClass())) {
                    typeInfos.addAll(buildTypeScriptInfo(models, item.getJavaClass(), classMap));
                }
            });

            typeInfos.add(new TypeInfo() {{
                setTitle(String.format("%s%s", action.getComment(), isRequestParams ? "request" : "response"));
                setName(typeName);
                setFieldList(fields);
            }});
            return typeInfos;
        }
        return buildTypeScriptInfo(new ArrayList<>(), parameters.get(0).getJavaClass(), classMap);
    }

    /**
     * 查询参数注释列表
     *
     * @param tags 注释列表
     * @return
     */
    private static Map<String, String> getParamComments(List<DocletTag> tags) {
        Map<String, String> paramComments = new HashMap<>();
        tags.forEach((item) -> {
            if (!item.getName().equals("param")) {
                return;//continue
            }
            final String comment = item.getValue();
            final int index = comment.indexOf(" ");
            paramComments.put(comment.substring(0, index), comment.substring(index).trim());
        });
        return paramComments;
    }

    /**
     * model编译成typescript
     */
    public static List<TypeInfo> buildTypeScriptInfo(Collection<String> modelList, Collection<JavaType> requests, Map<String, JavaClass> classMap) {
        List<TypeInfo> typeInfos = new ArrayList<>();
        requests.forEach(item -> {
            final JavaClass itemClass = (JavaClass) item;
            if(!isArray(itemClass)) {
                typeInfos.addAll(buildTypeScriptInfo(modelList, itemClass, classMap));
            }
        });
        return typeInfos;
    }

    /**
     * model编译成typescript
     */
    public static List<TypeInfo> buildTypeScriptInfo(Collection<String> models, JavaClass request, Map<String, JavaClass> classMap) {
        if (!TypeUtil.getJavaScriptTypeName(request.getName(), "").isEmpty()) {
            return new ArrayList<>();
        }
        if (models.contains(request.getSimpleName())) {
            return new ArrayList<>();
        }
        models.add(request.getSimpleName());
        ModelInfo info = new ModelInfo() {{
            setTitle(request.getComment());
            setName(TypeUtil.getTypeName(request));
        }};
        List<JavaClass> relationClassList = new ArrayList<>();
        JavaClass supperClass = request.getSuperJavaClass();
        //继承class字段
        if (supperClass != null && !supperClass.getName().equals("java.lang.Object")) {
            supperClass.getFields().forEach((item) -> {
                if (item.getName().equals("instance")) {
                    return;//continue
                }
                //跳过忽略类型
                if (DocUtil.isIgnoreType(item.getType())) {
                    return;
                }
                if (!isBasicType(item.getType())) {
                    //添加相关类型
                    DocUtil.addRelationClass(item.getType(), relationClassList, classMap);
                }
                info.getFieldList().add(new TypeInfo() {{
                    setTitle(item.getComment());
                    setName(item.getName());
                    setType(DocUtil.getJavaScriptType(item.getType()));
                }});
            });
        }
        request.getFields().forEach((item) -> {
            //跳过忽略类型
            if (DocUtil.isIgnoreType(item.getType())) {
                return;
            }
            if (!isBasicType(item.getType())) {
                //添加相关类型
                DocUtil.addRelationClass(item.getType(), relationClassList, classMap);
            }
            info.getFieldList().add(new TypeInfo() {{
                setTitle(item.getComment());
                setName(item.getName());
                setType(DocUtil.getJavaScriptType(item.getType()));
            }});
        });
        List<TypeInfo> typeInfoList = new ArrayList<>();
        typeInfoList.add(new TypeInfo() {{
            setTitle(info.getTitle());
            setName(info.getName());
            setFieldList(new ArrayList<TypeInfo>() {{
                info.getFieldList().forEach((item) -> {
                    add(new TypeInfo() {{
                        setTitle(item.getTitle());
                        setName(item.getName());
                        setType(item.getType());
                    }});
                });
            }});
        }});
        relationClassList.remove(null);
        relationClassList.forEach((item) -> {
            typeInfoList.addAll(buildTypeScriptInfo(models, item, classMap));
        });
        return typeInfoList;
    }

}
