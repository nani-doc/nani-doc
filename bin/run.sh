#!/bin/sh
currentPath=$(cd "$(dirname "$0")";pwd)
fileName="nani-doc-1.0.jar"
# echo  "$currentPath"
baseUrl="https://luoye.gitee.io/s.lmm.show/nani-doc/release"
if [ ! -f "$currentPath/$fileName" ];then
  wget -P "$currentPath" "$baseUrl/$fileName"
fi
java -jar "$currentPath/$fileName" -sourcePath "$currentPath" $*