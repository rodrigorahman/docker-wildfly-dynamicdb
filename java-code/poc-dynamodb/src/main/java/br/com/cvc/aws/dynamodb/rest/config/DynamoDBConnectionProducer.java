package br.com.cvc.aws.dynamodb.rest.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;

import javax.enterprise.inject.Produces;

/**
 * Created by dgrodrigo on 10/07/16.
 */
public class DynamoDBConnectionProducer {


    @Produces
    public DynamoDB getConnection() {
        AWSCredentials credentials = new BasicAWSCredentials("FAKE", "FAKE");
        return new DynamoDB(new AmazonDynamoDBClient(credentials).withEndpoint("http://dynamoDB:8000"));
    }


    @Produces
    public AmazonDynamoDBClient getConnectionClient() {
        return new AmazonDynamoDBClient(new ProfileCredentialsProvider()).withEndpoint("http://dynamoDB:8000");
    }

}
