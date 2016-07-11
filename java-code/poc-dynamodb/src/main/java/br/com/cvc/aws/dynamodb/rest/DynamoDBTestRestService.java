package br.com.cvc.aws.dynamodb.rest;

import br.com.cvc.aws.dynamodb.rest.dto.Countries;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.model.*;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dgrodrigo on 10/07/16.
 */
@Path("/dynamodb/teste")
public class DynamoDBTestRestService {


    @Inject
    private DynamoDB dynamoDB;

    @Path("/create/table")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTable() {

        try {
            String table = "TESTE";

            Table tableDynamo = dynamoDB.createTable(table,
                    Arrays.asList(
                            new KeySchemaElement("id", KeyType.HASH),
                            new KeySchemaElement("nome", KeyType.RANGE)
                    ),
                    Arrays.asList(
                            new AttributeDefinition("id", ScalarAttributeType.N),
                            new AttributeDefinition("nome", ScalarAttributeType.S)
                    ),
                    new ProvisionedThroughput(10L, 10L)
            );

            tableDynamo.waitForActive();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return Response.ok().entity("Tabela criada com sucesso").build();
    }

    @Path("/insert")
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response insert() {

        try {

            String table = "CVC";

            Table tableDynamo = dynamoDB.getTable(table);

            tableDynamo.putItem(new Item()
                    .withPrimaryKey("id", 1, "nome", "Rodrigo")
                    .with("telefone", "11980752431")
                    .with("endereco", "Rua Asteca, 51 Santo André"));


        } catch (Exception e) {
            e.printStackTrace();
        }

        return Response.ok().entity("Tabela criada com sucesso").build();
    }

    @Path("/getAll")
    @GET
    @Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
    @Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
    public Response getAll() {

        Table tableDynamo = dynamoDB.getTable("CVC");

        GetItemSpec filtro = new GetItemSpec().withPrimaryKey("id", 1, "nome", "Rodrigo");


        Item item = tableDynamo.getItem(filtro);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://restcountries.eu/rest/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RestCountriesService service = retrofit.create(RestCountriesService.class);

        Call<List<Countries>> callback = service.getAllCountries();
        try{
            retrofit2.Response<List<Countries>> response = callback.execute();
            System.out.println("Total de Cidades:" + response.body().size());


        } catch (IOException e) {
            e.printStackTrace();
        }

        return Response.ok().entity(item.asMap()).build();

    }


}
