set currentPath=%~dp0
javac -encoding utf-8 %currentPath%Downloader.java
java -classpath %currentPath%; Downloader %currentPath%
::java -jar .\nani-doc-1.0.jar -package show.lmm.controller -sourceJars "./libs/sources/commonBase-2.1.1-sources.jar,./libs/sources/spring-web-5.3.13-sources.jar" -wraperClasses "reactor.core.publisher.Mono" -sourcePath "D:\project\testProject"
java -jar %currentPath%nani-doc-latest.jar %*