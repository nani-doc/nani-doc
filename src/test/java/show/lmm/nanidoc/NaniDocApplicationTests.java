package show.lmm.nanidoc;

import org.junit.jupiter.api.Test;
import show.lmm.nanidoc.core.AppConfig;
import show.lmm.nanidoc.model.DocGenerateArgs;

import java.io.IOException;

//@SpringBootTest
class NaniDocApplicationTests {

    @Test
    void contextLoads() throws IOException {
        new DocContext().generate(new DocGenerateArgs() {{
            setProjectSourcePath("D:\\project\\scrm\\scrm.api");
            setPackageName("com.dataxgroup.scrm_api.controller");
            setSourceJars(new String[]{
                    "D:\\project\\scrm\\scrm.api\\libs\\sources\\spring-web-6.0.2-sources.jar"
            });
            setDocOutPath(String.format("%s/docDist/", AppConfig.rootPath));
            setWraperClasses(new String[]{
                    "reactor.core.publisher.Mono",
                    "org.springframework.http.ResponseEntity",
                    "org.springframework.http.HttpEntity"
            });
        }});
    }

}
