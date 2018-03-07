FROM 10.10.10.160:8010/maxleap/base:12
MAINTAINER wJia <wjia@maxleap.com>

#----------------------------Copy 项目目录到容器里--------------------------------------------

RUN \
mkdir -p /opt/maxwon_doc
ADD mod-server-1.0-SNAPSHOT-shaded.jar /opt/maxwon_doc

WORKDIR /opt/maxwon_doc

ENTRYPOINT ["java", "-cp", "mod-server-1.0-SNAPSHOT-shaded.jar","as.leap.maxwon.doc.server.DocServer"]