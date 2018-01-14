package bj.zemidjan.dio.models;

/**
 * Created by Oulfath on 06/10/2017.
 */

public class Forum {
    private  long id=0;
    private String Texte;

    public void setTexte(String texte) {
        Texte = texte;
    }

    public long getId() {
        return id;
    }

    public String getTexte() {
        return Texte;
    }

    public void setId(long id) {
        this.id = id;
    }
}
