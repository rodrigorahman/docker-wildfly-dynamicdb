FROM ubuntu

MAINTAINER rodrigoalmeida@ext.cvc.com.br

#INSTALL JAVA
RUN \
	apt-get update && \
	apt-get install -y wget && \
	apt-get install -y  software-properties-common && \
    add-apt-repository ppa:webupd8team/java -y && \
    apt-get update && \
    echo oracle-java7-installer shared/accepted-oracle-license-v1-1 select true | /usr/bin/debconf-set-selections && \
    apt-get install -y oracle-java8-installer && \
  	rm -rf /var/lib/apt/lists/* && \
  	rm -rf /var/cache/oracle-jdk8-installer

ENV JAVA_HOME /usr/lib/jvm/java-8-oracle

# INSTALL AWS DYNAMODB
RUN \
    mkdir /data && \
	mkdir /u01/ && mkdir /u01/aws && \
	cd /u01/aws && \
	wget http://dynamodb-local.s3-website-us-west-2.amazonaws.com/dynamodb_local_latest.tar.gz && \
	tar -xvzf dynamodb_local_latest.tar.gz && \
	apt-get clean && \
	rm -rf dynamodb_local_latest.tar.gz

CMD ["java", "-Djava.library.path=/u01/aws/DynamoDBLocal_lib", "-jar", "/u01/aws/DynamoDBLocal.jar", "-sharedDb", "-dbPath", "/data"]