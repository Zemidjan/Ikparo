package bj.zemidjan.dio.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import bj.zemidjan.dio.R;
import bj.zemidjan.dio.models.ProfilInspirant;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class AffichageProfilActivity extends AppCompatActivity {


    TextView nom;
    TextView description;
    ImageView image;

    private ArrayList<ProfilInspirant> profils;
    private ProfilInspirant currentProfil;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affichage_profil);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nom = (TextView) findViewById(R.id.nom);
        description = (TextView) findViewById(R.id.description);
        image = (ImageView) findViewById(R.id.image_profil);

        Intent intent = getIntent();
        final String idRecu = intent.getStringExtra("id");

        final Gson gson = new Gson();
        final SharedPreferences preferences = this.getSharedPreferences("educarsenal", 0);
        final SharedPreferences.Editor editor = preferences.edit();
        String jsonProfils = preferences.getString("listProfils"+intent.getStringExtra("domaineId"), null);

        ArrayList<ProfilInspirant> listProfils = null;
        if (jsonProfils != null) {
            listProfils = gson.fromJson(jsonProfils, new TypeToken<ArrayList<ProfilInspirant>>() {
            }.getType());
            final ProfilInspirant profil = listProfils.get(Integer.parseInt(intent.getStringExtra("id")));
            if (profil != null) {
                currentProfil = profil;
                Glide.with(image.getContext()).load(currentProfil.getImage()).into(image);
                nom.setText(currentProfil.getNom());

                description.setText(currentProfil.getDescription());
                ActionBar actionBar = getSupportActionBar();
                actionBar.setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setTitle(currentProfil.getNom());


            }

        }
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

