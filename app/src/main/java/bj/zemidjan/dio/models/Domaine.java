package bj.zemidjan.dio.models;

/**
 * Created by Oulfath on 17/09/2017.
 */

public class Domaine {
    private  long id=0;
    private String libelle;
    private Domaine parentDomaine;
    private String image;
    private String dateCreated;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public Domaine getParentDomaine() {
        return parentDomaine;
    }

    public void setParentDomaine(Domaine parentDomaine) {
        this.parentDomaine = parentDomaine;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }
}
