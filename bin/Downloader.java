import java.net.*;
import java.io.*;
import java.nio.channels.*;
import java.util.Properties;

public class Downloader {

    /**
     * nani doc jar
     */
    private static final String DOWNLOAD_URL =
            "https://luoye.gitee.io/s.lmm.show/nani-doc/release/nani-doc-1.1.jar";

    private static final String NANI_DOC_JAR_FILE_NAME = "./nani-doc-1.1.jar";

    public static void main(String[] args) {
        File file = new File(NANI_DOC_JAR_FILE_NAME);
        if(file.exists()){
            file.delete();
        }
        System.out.println("- Downloader started");
        System.out.println("- Downloading from：" + DOWNLOAD_URL);
        try {
            downloadFileFromURL(DOWNLOAD_URL, args[0]+NANI_DOC_JAR_FILE_NAME);
            System.out.println("Done");
            System.exit(0);
        } catch (Throwable e) {
            System.out.println("- Error downloading");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void downloadFileFromURL(String urlString, String path) throws Exception {
        URL website = new URL(urlString);
        ReadableByteChannel rbc;
        rbc = Channels.newChannel(website.openStream());
        FileOutputStream fos = new FileOutputStream(new File(path));
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
    }

}