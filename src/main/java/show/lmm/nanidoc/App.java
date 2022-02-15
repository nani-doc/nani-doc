package show.lmm.nanidoc;

import show.lmm.nanidoc.core.AppConfig;
import show.lmm.nanidoc.core.Constant;
import show.lmm.nanidoc.model.ArgInfo;
import show.lmm.nanidoc.model.DocGenerateArgs;
import show.lmm.nanidoc.utils.ManifestUtil;

import java.util.HashMap;
import java.util.Map;

import static org.fusesource.jansi.Ansi.Color.GREEN;
import static org.fusesource.jansi.Ansi.Color.RED;
import static org.fusesource.jansi.Ansi.ansi;

/**
 * 纳尼文档
 *
 * @author 刘明明
 * @date 2022-01-21 15:23
 */
public class App {

    public static void main(String[] args) {
        printVersion();
        long startTime = System.currentTimeMillis();
        Map<String, String> inputArgs = parseArgs(args);
        if (inputArgs.containsKey(Constant.helpArgKey)) {
            showHelp(inputArgs.get(Constant.helpArgKey));
            return;
        }
        if (!verificationArgs(inputArgs)) {
            return;
        }
        final String projectSourcePath = inputArgs.getOrDefault("sourcePath", AppConfig.rootPath);
        final String packageName = inputArgs.get("package");
        final String[] sourceJars = inputArgs.getOrDefault("sourceJars", "").split(",");
        final String docOutPath = inputArgs.getOrDefault("docOutPath", String.format("%s/docDist/", AppConfig.rootPath));
        final String[] wraperClasses = inputArgs.getOrDefault("wraperClasses", "").split(",");
        new DocContext().generate(new DocGenerateArgs() {{
            setProjectSourcePath(projectSourcePath);
            setPackageName(packageName);
            setSourceJars(sourceJars);
            setDocOutPath(docOutPath);
            setWraperClasses(wraperClasses);
        }});
        System.out.println(ansi().fg(GREEN)
                .a("生成文档成功，用时：")
                .a(System.currentTimeMillis() - startTime)
                .a("毫秒").reset());
    }

    /**
     * 打印版本号
     *
     * @since 刘明明/2022-02-07 14:31:37
     **/
    private static void printVersion() {
        StringBuilder txt = new StringBuilder("\r\n");
        txt.append("  ████     ██           ████     ██ ██           ███████     ███████     ██████\r\n");
        txt.append("  ██ ██    ██           ██ ██    ██              ██    ██   ██     ██   ██    ██\r\n");
        txt.append("  ██  ██   ██  ██████   ██  ██   ██ ██           ██     ██ ██       ██ ██      \r\n");
        txt.append("  ██   ██  ██       ██  ██   ██  ██ ██   █████   ██     ██ ██       ██ ██      \r\n");
        txt.append("  ██    ██ ██  ███████  ██    ██ ██ ██           ██     ██ ██       ██ ██      \r\n");
        txt.append("  ██     ████  ██   ██  ██     ████ ██           ██    ██   ██     ██   ██    ██\r\n");
        txt.append("  ██      ███  ████████ ██      ███ ██           ███████     ███████     ██████\r\n");
        System.out.println(txt);
        System.out.print(ansi().fg(GREEN).a(":: NaNi Doc ::").reset());
        String version = ManifestUtil.getValue("NaNi-Doc-Version");
        System.out.print(String.format("                                                      (v%s)\r\n",version));
        System.out.println(ansi().fg(GREEN).a(":: https://gitee.com/nani-doc/nani-doc"));
        System.out.println(ansi().fg(GREEN).a(":: https://github.com/nani-doc/nani-doc\r\n").reset());
    }

    /**
     * 验证输入参数
     *
     * @param inputArgs 输入参数
     * @return
     */
    private static boolean verificationArgs(Map<String, String> inputArgs) {
        for (ArgInfo item : Constant.args) {
            String value = inputArgs.getOrDefault(item.getArgName(), "");
            if (item.isRequired() && (value == null || value.isEmpty())) {
                System.out.println(ansi().fg(RED)
                        .a("参数：-")
                        .a(item.getArgName())
                        .a(" ")
                        .a(item.getTitle())
                        .a("不能为空").reset());
                return false;
            }
        }
        return true;
    }

    /**
     * 格式化输入参数
     *
     * @param args 输入参数
     * @return
     */
    private static Map<String, String> parseArgs(String[] args) {
        Map<String, String> inputArgs = new HashMap<>();
        String item;
        String k = "";
        for (int i = 0, length = args.length; i < length; i++) {
            item = args[i];
            if (item.equals(Constant.helpArgKey)) {
                k = item;
                inputArgs.put(item, "");
            } else if (item.startsWith("-")) {
                k = item.substring(1);
            } else if (!k.isEmpty()) {
                inputArgs.put(k, item);
                k = "";
            }
        }
        return inputArgs;
    }

    /**
     * 显示帮助信息
     *
     * @param value 命令名称
     */
    private static void showHelp(String value) {
        StringBuilder helpStr = new StringBuilder();
        if (!value.isEmpty()) {
            int argIndex = Constant.argKeys.getOrDefault(value, -1);
            if (argIndex < 0) {
                helpStr.append("暂不支持此命令");
                System.out.println(helpStr);
                return;
            }
            ArgInfo argInfo = Constant.args.get(argIndex);
            helpStr.append(argInfo.getTitle());
            if (argInfo.isRequired()) {
                helpStr.append("【必填】");
            }
            helpStr.append("\r\n");
            if (!argInfo.getDescription().isEmpty()) {
                helpStr.append(argInfo.getDescription());
            }
            System.out.println(helpStr);
            return;
        }
        helpStr.append("有关某个命令的详细信息，请键入 --help 命令名\r\n");
        Constant.args.forEach((item) -> {
            helpStr.append(item.getArgName());
            for (int i = 0, length = Constant.maxArgKeyLength + 10 - item.getArgName().length(); i < length; i++) {
                helpStr.append(" ");
            }
            helpStr.append(item.getTitle());
            if (item.isRequired()) {
                helpStr.append("【必填】");
            }
            helpStr.append("\r\n");
        });
        System.out.println(helpStr);
    }
}
