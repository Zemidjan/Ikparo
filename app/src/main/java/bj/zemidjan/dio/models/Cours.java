package bj.zemidjan.dio.models;

/**
 * Created by Oulfath on 19/09/2017.
 */

public class Cours {
    private  long id=0;
    private String titre;
    private String description;
    private String urlCours;
    private boolean gratuit;
    private boolean certification;
    private boolean telechargeable;
    private Domaine domaine;
    private SiteMooc site;
    private String image;
    private String coursPDF;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrlCours() {
        return urlCours;
    }

    public void setUrlCours(String urlCours) {
        this.urlCours = urlCours;
    }

    public boolean isGratuit() {
        return gratuit;
    }

    public void setGratuit(boolean gratuit) {
        this.gratuit = gratuit;
    }

    public boolean isCertification() {
        return certification;
    }

    public void setCertification(boolean certification) {
        this.certification = certification;
    }

    public boolean isTelechargeable() {
        return telechargeable;
    }

    public void setTelechargeable(boolean telechargeable) {
        this.telechargeable = telechargeable;
    }

    public Domaine getDomaine() {
        return domaine;
    }

    public void setDomaine(Domaine domaine) {
        this.domaine = domaine;
    }

    public SiteMooc getSite() {
        return site;
    }

    public void setSite(SiteMooc site) {
        this.site = site;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCoursPDF() {
        return coursPDF;
    }

    public void setCoursPDF(String coursPDF) {
        this.coursPDF = coursPDF;
    }
}
