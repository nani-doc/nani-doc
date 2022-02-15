package show.lmm.nanidoc;

import org.junit.jupiter.api.Test;
import show.lmm.nanidoc.core.AppConfig;
import show.lmm.nanidoc.model.DocGenerateArgs;

import java.io.IOException;

//@SpringBootTest
class NaniDocApplicationTests {

//    @Test
    void contextLoads() throws IOException {
        new DocContext().generate(new DocGenerateArgs() {{
            setProjectSourcePath("D:\\project\\rd-team\\dxg-nex-backend");
            setPackageName("com.dataxgroup.nex.controller");
            setSourceJars(new String[]{
                    "D:\\project\\rd-team\\springcloud-commonbase\\build\\libs\\commonBase-2.1.1-sources.jar",
                    "C:\\Users\\DataXgroup\\.gradle\\caches\\modules-2\\files-2.1\\org.springframework\\spring-web\\5.3.13\\b5a502f2450a025b5fe3508457688fd3037d4721\\spring-web-5.3.13-sources.jar"
            });
            setDocOutPath(String.format("%s/docDist/", AppConfig.rootPath));
            setWraperClasses(new String[]{
                    "reactor.core.publisher.Mono"
            });
        }});
    }

}
