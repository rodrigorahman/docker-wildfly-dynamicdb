##Docker utilizando Wildfly 10 e DynamoDB da Amazon

Criado duas imagens uma para o wildfly e outra para o dynamicDB com esse projeto você pode rodar um teste local no projeto dynamodb utilizando wildfly e JAVA 8


##Imagem Wildfly

**Rodar a build para montar a image**

```
docker build -t jboss/wildfly-custom wildfly-10/
```

##Imagem DynamoDB

```
docker build -t aws-dynamodb aws-dynamodb/
```


##Projeto JAVA

No projeto java temos 2 serviços REST para popular e buscar as cidades:


**Salvando os dados de pais**
Todo o serviço foi baseado no rest: https://restcountries.eu/rest/v1/all

```
http://localhost:8080/poc-dynamodb/rest/countries/save
```


**Buscando a capital e o total de população de um pais**

```
http://localhost:8080/poc-dynamodb/rest/countries/populations?country=Brasil
```
