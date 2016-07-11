FROM jboss/wildfly

MAINTAINER rodrigoalmeida@ext.cvc.com.br

RUN /opt/jboss/wildfly/bin/add-user.sh admin welcome1 --silent


RUN \
	mkdir ~/.aws

ADD aws/ ~/.aws/


#ENV JAVA_OPTS -agentlib:jdwp=transport=dt_socket,address=127.0.0.1:58356,suspend=y,server=n
ENV JAVA_OPTS  -Xrunjdwp:transport=dt_socket,address=8787,server=y,suspend=n 
ENV AWS_CREDENTIAL_PROFILES_FILE ~/.aws/credentials


EXPOSE 8787


CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]


