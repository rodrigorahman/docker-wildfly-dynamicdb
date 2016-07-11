package br.com.cvc.aws.dynamodb.rest.dto;

/**
 * Created by dgrodrigo on 10/07/16.
 */
public class Countries {

    private String name;
    private String nativeName;
    private String capital;
    private Long population;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNativeName() {
        return nativeName;
    }

    public void setNativeName(String nativeName) {
        this.nativeName = nativeName;
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public Long getPopulation() {
        return population;
    }

    public void setPopulation(Long population) {
        this.population = population;
    }
}
