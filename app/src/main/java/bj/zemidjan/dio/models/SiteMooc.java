package bj.zemidjan.dio.models;

/**
 * Created by Oulfath on 19/09/2017.
 */

public class SiteMooc {
    private  long id=0;
    private String nom;
    private String url;
    private String logo;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}
