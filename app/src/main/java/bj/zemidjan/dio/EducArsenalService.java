package bj.zemidjan.dio;

import bj.zemidjan.dio.models.Cours;
import bj.zemidjan.dio.models.Domaine;
import bj.zemidjan.dio.models.ProfilInspirant;
import bj.zemidjan.dio.models.SuivreCours;
import bj.zemidjan.dio.models.Utilisateur;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Hnarech on 22/09/2017.
 */

public interface EducArsenalService {

    public static final String ENDPOINT = "http://benintransactions.com/gag_server/web/app_dev.php/";

    @FormUrlEncoded
    @POST("register")
    Call<Utilisateur> inscription(@Field("nom") String nom, @Field("prenoms") String prenom, @Field("email") String mail,@Field("telephone") String telephone, @Field("sexe") String sexe, @Field("password") String password);

    @FormUrlEncoded
    @POST("login")
    Call<Utilisateur> connexion(@Field("email") String mail, @Field("password") String password);

    @GET("liste-domaine")
    Call<ArrayList<Domaine>> listDomaine();

    @GET("liste-cours")
    Call<ArrayList<Cours>> listCours(@Query("domaineId") String domaineId);

    @GET("liste-sousdomaine")
    Call<ArrayList<Domaine>> listSousDomaine(@Query("domaineId") String domaineId);

    @GET("liste-profilinspirant")
    Call<ArrayList<ProfilInspirant>> listProfil(@Query("domaineId") String domaineId);

    @FormUrlEncoded
    @POST("add-suivrecours")
    Call<SuivreCours> addSuivreCours(@Field("coursId") Long coursId, @Field("utilisateurId") Long utilisateurId);


    @GET("liste-suivrecours")
    Call<ArrayList<SuivreCours>> listSuivreCours(@Query("utilisateurId") String utilisateurId);

}
