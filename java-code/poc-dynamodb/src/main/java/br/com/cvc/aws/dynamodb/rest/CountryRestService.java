package br.com.cvc.aws.dynamodb.rest;


import br.com.cvc.aws.dynamodb.rest.dto.Countries;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.model.*;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.*;

/**
 * Created by dgrodrigo on 10/07/16.
 */
@Path("countries")
public class CountryRestService {

    @Inject
    private DynamoDB dynamoDB;
    @Inject
    private AmazonDynamoDBClient client;
    private static String COUNTRY_TABLE = "countries";

    @Path("/save")
    @GET
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response save(){

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://restcountries.eu/rest/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RestCountriesService service = retrofit.create(RestCountriesService.class);

        Call<List<Countries>> countriesCallback = service.getAllCountries();

        try {

            retrofit2.Response<List<Countries>> countriesResponse = countriesCallback.execute();
            List<Countries> countries = countriesResponse.body();

            createTableDynamicDB();
            int i = 0;

            for (Countries country : countries) {
                saveCountry(i++, country);
            }



        } catch (IOException e) {
            e.printStackTrace();
        }

        return Response.ok().entity("Tabela Criada").build();

    }

    private void saveCountry(Integer id, Countries country) {

        Table tableDynamo = dynamoDB.getTable(COUNTRY_TABLE);

        Long population = country.getPopulation();
        String capital = country.getCapital();
        Item item = new Item();

        item = item.withPrimaryKey("id", id, "nativeName", country.getNativeName().toLowerCase());

        if(population != null){
            item = item.with("population", population);
        }

        if(capital != null && !capital.equals("")){
            item = item.with("capital", capital);
        }

        tableDynamo.putItem(item);
    }


    private void createTableDynamicDB() {
        Table table = dynamoDB.getTable(COUNTRY_TABLE);

        if(table == null) {

            Table tableDynamo = dynamoDB.createTable(COUNTRY_TABLE,
                    Arrays.asList(
                            new KeySchemaElement("id", KeyType.HASH),
                            new KeySchemaElement("nativeName", KeyType.RANGE)
                    ),
                    Arrays.asList(
                            new AttributeDefinition("id", ScalarAttributeType.N),
                            new AttributeDefinition("nativeName", ScalarAttributeType.S)
                    ),
                    new ProvisionedThroughput(10L, 10L)
            );
            try {
                tableDynamo.waitForActive();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    @Path("/populations")
    @GET
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response getPopulation(@QueryParam("country") String country){

        //Table tableDynamo = dynamoDB.getTable(COUNTRY_TABLE);

        Map<String, AttributeValue> expressionAttributeValues =
                new HashMap<>();
        expressionAttributeValues.put(":nativeNameQuery", new AttributeValue().withS(country.toLowerCase()));


        ScanRequest scanRequest = new ScanRequest()
                .withTableName(COUNTRY_TABLE)
                .withFilterExpression("nativeName = :nativeNameQuery")
                .withProjectionExpression("population, capital")
                .withExpressionAttributeValues(expressionAttributeValues);

        ScanResult items = client.scan(scanRequest);

        int population = 0;
        String capital = "";
        for (Map<String, AttributeValue> item : items.getItems()){
            population = Integer.valueOf(item.get("population").getN());
            capital = item.get("capital").getS();
        }

        Map<String,Object> retorno = new HashMap<>();
        retorno.put("population", population);
        retorno.put("capital", capital);

        return Response.ok().entity(retorno).build();
    }

}
