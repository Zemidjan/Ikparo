package bj.zemidjan.dio.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import bj.zemidjan.dio.R;
import bj.zemidjan.dio.models.Cours;
import bj.zemidjan.dio.models.SuivreCours;
import bj.zemidjan.dio.utils.MyAppWebViewClient;
import bj.zemidjan.dio.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.View.GONE;

public class CommencerCoursActivity extends AppCompatActivity {

    private WebView mWebView;
    private Cours currentCours;
    CoordinatorLayout coordinatorLayoutAction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commencer_cours);

        coordinatorLayoutAction = (CoordinatorLayout) findViewById(R.id.coordinatorLayoutCours);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final ProgressBar loading = (ProgressBar) findViewById(R.id.loading);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);



        mWebView = (WebView) findViewById(R.id.activity_main_webview);

        loading.setVisibility(View.VISIBLE);
        mWebView.setVisibility(GONE);
// Déclare mWebView à activity_main (le layout)

        // Configure la webview pour l'utilisation du javascript
        WebSettings webSettings = mWebView.getSettings();
        jsEnabled(webSettings);

        // Permet l'ouverture des fenêtres
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);

        // Autorise le stockage DOM (Document Object Model)
        webSettings.setDomStorageEnabled(true);

        Intent intent = getIntent();
        final String idRecu = intent.getStringExtra("id");
        final String type = intent.getStringExtra("type");
        final Gson gson = new Gson();
        final SharedPreferences preferences = getSharedPreferences("educarsenal", 0);
        final SharedPreferences.Editor editor = preferences.edit();
        String jsonCours = preferences.getString("listCours"+intent.getStringExtra("domaineId"), null);
        final Long userId = preferences.getLong("id",0);
        ArrayList<Cours> listCours = null;
        if (jsonCours != null) {
           listCours = gson.fromJson(jsonCours, new TypeToken<ArrayList<Cours>>() {
            }.getType());
            final Cours cours = listCours.get(Integer.parseInt(intent.getStringExtra("id")));
            if (cours != null) {

                currentCours = cours;

                Call<SuivreCours> addSuivreCours = Utils.retrofitBuilder().addSuivreCours(currentCours.getId(), userId);
                addSuivreCours.enqueue(new Callback<SuivreCours>() {
                    @Override
                    public void onResponse(Call<SuivreCours> call, Response<SuivreCours> response) {

                        if (response.code() == 206){

                            Toast.makeText(CommencerCoursActivity.this,"Ce cours a été ajouté dans vos cours suivis !", Toast.LENGTH_LONG).show();

                        }else{


                        }
                    }

                    @Override
                    public void onFailure(Call<SuivreCours> call, Throwable t) {


                    }
                });


                getSupportActionBar().setTitle(currentCours.getSite().getUrl());
                // Charge l'url
                if(type.equals("url")){
                    mWebView.loadUrl(currentCours.getUrlCours());
                }else{

                    //shouldOverrideUrlLoading(mWebView,currentCours.getCoursPDF());
                    //mWebView.loadUrl(currentCours.getCoursPDF());
                    if(intent.getStringExtra("download").equals("oui")){
                        shouldOverrideUrlLoading(mWebView,currentCours.getCoursPDF());
                    }else{
                        mWebView.loadUrl("http://docs.google.com/gview?embedded=true&url="
                                + currentCours.getCoursPDF());
                    }

                }


            }
        }/*
		 * Les instructions ci-dessous permettent de forcer l'application
		 * à ouvrir les Url directement dans l'application et non dans
		 * un navigateur externe. MyAppWebViewClient() est la fonction
		 * contenue dans le fichier MyAppWebViewClient.java .
		 */

        mWebView.setWebViewClient(new MyAppWebViewClient() {
            @Override
            // Fonction qui permet l'affichage de la page lorsque tout est chargé (événement onPageFinished)

            public void onPageFinished(WebView view, String url) {
                findViewById(R.id.activity_main_webview).setVisibility(View.VISIBLE);
                loading.setVisibility(GONE);
            }
        });
    }
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        String cleanUrl = url;
        if (url.contains("?")) {
            // remove the query string
            cleanUrl = url.substring(0,url.indexOf("?"));
        }

        if (cleanUrl.endsWith("pdf")) {
            try {
                Uri uriUrl = Uri.parse(cleanUrl);
                Intent intentUrl = new Intent(Intent.ACTION_VIEW, uriUrl);
                startActivity(intentUrl);
                return true;

            } catch (Exception e) {
                System.out.println(e);
                Toast.makeText(this,"Aucune appplication de lecture de PDF trouvée", Toast.LENGTH_LONG).show();
            }
        }

        return false;
    }
    public static void jsEnabled(WebSettings ws){
        ws.setJavaScriptEnabled(true);
    }

    /*
     * Fonction qui permet de revenir à la page précédente
     * au lieu de quitter l'application lorsque le bouton
     * revenir en arrière est appuyé.
     */
    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) {
            mWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
