package br.com.cvc.aws.dynamodb.rest;

import br.com.cvc.aws.dynamodb.rest.dto.Countries;
import retrofit2.Call;
import retrofit2.http.GET;

import java.util.List;

/**
 * Created by dgrodrigo on 10/07/16.
 */
public interface RestCountriesService {

    @GET("all")
    Call<List<Countries>> getAllCountries();

}
