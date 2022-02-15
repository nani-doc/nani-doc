package show.lmm.nanidoc.utils;

import com.thoughtworks.qdox.model.*;
import com.thoughtworks.qdox.model.impl.DefaultJavaParameterizedType;
import show.lmm.nanidoc.model.TypeInfo;

import java.util.*;

public class DocUtil {

    /**
     * 获得注解名称列表
     */
    public static Map<String, JavaAnnotation> getAnnotationsList(List<JavaAnnotation> annotationList) {
        Map<String, JavaAnnotation> list = new HashMap<>();
        annotationList.forEach((item) -> {
            list.put(item.getType().getBinaryName(), item);
        });
        return list;
    }

    /**
     * 获得方法参数注解名称列表
     */
    public static Map<String, JavaParameter> getMethodParamsAnnotationsList(List<JavaParameter> parameterList) {
        Map<String, JavaParameter> list = new HashMap<>();
        parameterList.forEach((item) -> {
            item.getAnnotations().forEach((annotation) -> {
                list.put(annotation.getType().getBinaryName(), item);
            });
        });
        return list;
    }


    /**
     * 获得javascript类型
     */
    public static String getJavaScriptType(JavaClass type) {
        String typeName = type.getName();
        if (type.toString().endsWith("[]")) {
            return String.format("%s[]", TypeUtil.getJavaScriptTypeName(typeName, typeName));
        }
        if (typeName.equals("java.util.List") || typeName.equals("List") || typeName.equals("Collection") || typeName.equals("HashSet")) {
            return String.format("%s[]", getJavaScriptType((JavaClass) ((DefaultJavaParameterizedType) type).getActualTypeArguments().get(0)));
        }
        if (typeName.equals("Map")) {
            List<JavaType> typeList = ((DefaultJavaParameterizedType) type).getActualTypeArguments();
            return String.format("{[key: %s]:%s}", getJavaScriptType((JavaClass) typeList.get(0)), getJavaScriptType((JavaClass) typeList.get(1)));
        }
        return TypeUtil.getJavaScriptTypeName(typeName, typeName);
    }

    /**
     * 有关联类的类型列表
     */
    private static final Collection<String> hasRelationTypeList = new HashSet<String>() {{
        add("List");
        add("Collection");
        add("Map");
        add("HashSet");
        add("Date");
    }};

    /**
     * 忽略类型列表
     */
    private static final Collection<String> ignoreTypes = new HashSet<String>() {{
        add("org.slf4j.Logger");
    }};

    /**
     * 是否是忽略类型
     *
     * @param type java类型
     * @return
     */
    public static boolean isIgnoreType(JavaClass type) {
        return ignoreTypes.contains(type.getName());
    }

    /**
     * 添加关联类
     */
    public static void addRelationClass(JavaClass type, List<JavaClass> relationClassList, Map<String, JavaClass> classMap) {
        //跳过忽略类型
        if (isIgnoreType(type)) {
            return;
        }
        List<JavaType> typeList = ((DefaultJavaParameterizedType) type).getActualTypeArguments();
        if (typeList.isEmpty()) {
            if (TypeUtil.getJavaScriptTypeName(type.getName(), "").isEmpty()) {
                return;
            }
            if (!hasRelationTypeList.contains(type.getName())) {
                relationClassList.add(type);
                return;
            }
        }
        for (JavaType item : typeList) {
            if (TypeUtil.isBasicType((JavaClass) item)) {
                continue;
            }
            if (hasRelationTypeList.contains(item.getValue())) {
                typeList = ((DefaultJavaParameterizedType) item).getActualTypeArguments();
                typeList.forEach((typeItem) -> {
                    addRelationClass((JavaClass) typeItem, relationClassList, classMap);
                });
                continue;
            }
            if (!TypeUtil.getJavaScriptTypeName(item.getValue(), "").isEmpty()) {
                continue;
            }
            if (!classMap.containsKey(item.getBinaryName())) {
                continue;
            }
            relationClassList.add(classMap.get(item.getBinaryName()));
        }
    }

    /**
     * 是否存在相同的注解
     *
     * @param compareAnnotationList 源注解列表
     * @param compareAnnotationList 对比注解列表
     */
    public static String getFirstJointAnnotation(Collection<String> sourceAnnotationList, Collection<String> compareAnnotationList) {
        int sourceAnnotationSize = sourceAnnotationList.size();
        int compareAnnotationSize = compareAnnotationList.size();
        Collection<String> contains;
        Collection<String> iterate;
        if (sourceAnnotationSize > compareAnnotationSize) {
            iterate = compareAnnotationList;
            contains = sourceAnnotationList;
        } else {
            iterate = sourceAnnotationList;
            contains = compareAnnotationList;
        }

        for (String item : iterate) {
            if (contains.contains(item)) {
                return item;
            }
        }
        return "";
    }

    /**
     * 获得模型名称
     *
     * @param action          java方法
     * @param isRequestParams 是否是request请求参数
     * @return
     */
    public static String getEntityName(JavaMethod action, boolean isRequestParams) {
        final List<JavaParameter> parameters = action.getParameters();
        if (parameters == null || parameters.isEmpty()) {
            return "";
        }
        if (parameters.size() == 1) {
            return DocUtil.getJavaScriptType(parameters.get(0).getJavaClass());
        }
        final String baseTypeName = String.format("%s%s", action.getName(), isRequestParams ? "Request" : "Response");
        return String.format("%s%s", baseTypeName.substring(0, 1).toUpperCase(Locale.ROOT), baseTypeName.substring(1));
    }
}
