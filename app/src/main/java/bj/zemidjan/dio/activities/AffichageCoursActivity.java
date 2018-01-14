package bj.zemidjan.dio.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import bj.zemidjan.dio.R;
import bj.zemidjan.dio.models.Cours;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class AffichageCoursActivity extends AppCompatActivity {

    TextView titre;
    TextView description;
    TextView gratuit;
    ImageView certification;
    ImageView telechargeable;
    Button btn_suivre;
    ImageView image;


    private ArrayList<Cours> cours;
    private Cours currentCours;
    public String jsonCours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affichage_cours);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cours");

        titre = (TextView) findViewById(R.id.titre_cours);
        description = (TextView) findViewById(R.id.description_cours);
        btn_suivre = (Button) findViewById(R.id.btn_suivre);
        gratuit = (TextView) findViewById(R.id.gratuit);
        telechargeable = (ImageView) findViewById(R.id.telechargeable);
        certification = (ImageView) findViewById(R.id.certification);
        image = (ImageView) findViewById(R.id.image_cours);

        Intent intent = getIntent();
        final String idRecu = intent.getStringExtra("id");
        final String domaineId = intent.getStringExtra("domaineId");

        final Gson gson = new Gson();
        final SharedPreferences preferences = this.getSharedPreferences("educarsenal", 0);
        final SharedPreferences.Editor editor = preferences.edit();

        if (intent.getStringExtra("rechercher").equals("non")) {
            jsonCours = preferences.getString("listCours" + intent.getStringExtra("domaineId"), null);

        } else {
            jsonCours = preferences.getString("listCoursRecherche", null);
        }


        ArrayList<Cours> listCours = null;
        if (jsonCours != null) {
            listCours = gson.fromJson(jsonCours, new TypeToken<ArrayList<Cours>>() {
            }.getType());
            final Cours cours = listCours.get(Integer.parseInt(intent.getStringExtra("id")));
            if (cours != null) {
                currentCours = cours;
                Glide.with(image.getContext()).load(currentCours.getImage()).into(image);
                titre.setText(currentCours.getTitre());
                if (currentCours.isGratuit()) {

                } else {
                    gratuit.setText("Payant");
                }
                if (currentCours.isCertification()) {

                } else {
                    certification.setBackgroundResource(R.color.gris_3);
                }
                if (currentCours.isCertification()) {
                    certification.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (currentCours.isTelechargeable()) {
                                Intent intent = new Intent(AffichageCoursActivity.this, CommencerCoursActivity.class);
                                intent.putExtra("id", "" + idRecu);
                                intent.putExtra("type", "pdf");
                                intent.putExtra("domaineId", domaineId);
                                intent.putExtra("download", "non");
                                startActivity(intent);
                            } else {

                                Intent intent = new Intent(AffichageCoursActivity.this, CommencerCoursActivity.class);
                                intent.putExtra("id", "" + idRecu);
                                intent.putExtra("type", "url");
                                intent.putExtra("domaineId", domaineId);
                                intent.putExtra("download", "non");
                                startActivity(intent);
                            }
                        }
                    });

                } else {
                    certification.setBackgroundResource(R.color.gris_4);
                    certification.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            LayoutInflater factory = LayoutInflater.from(AffichageCoursActivity.this);
                            final AlertDialog.Builder adb = new AlertDialog.Builder(AffichageCoursActivity.this);
                            adb.setMessage("Ce cours ne propose pas de certification ! ");
                            adb.setTitle("Message");
                            adb.setIcon(R.drawable.ic_assignment_black_24dp);

                            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });


                            adb.show();
                        }
                    });
            }
                    if (currentCours.isTelechargeable()) {
                        telechargeable.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(AffichageCoursActivity.this, CommencerCoursActivity.class);
                                intent.putExtra("id", "" + idRecu);
                                intent.putExtra("type", "pdf");
                                intent.putExtra("domaineId", domaineId);
                                intent.putExtra("download", "oui");
                                startActivity(intent);
                            }
                        });

                    } else {
                        telechargeable.setBackgroundResource(R.color.gris_3);
                    }
                    description.setText(currentCours.getDescription());

                    btn_suivre.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (currentCours.isTelechargeable()) {
                                Intent intent = new Intent(AffichageCoursActivity.this, CommencerCoursActivity.class);
                                intent.putExtra("id", "" + idRecu);
                                intent.putExtra("type", "pdf");
                                intent.putExtra("domaineId", domaineId);
                                intent.putExtra("download", "non");
                                startActivity(intent);
                            } else {

                                Intent intent = new Intent(AffichageCoursActivity.this, CommencerCoursActivity.class);
                                intent.putExtra("id", "" + idRecu);
                                intent.putExtra("type", "url");
                                intent.putExtra("domaineId", domaineId);
                                intent.putExtra("download", "non");
                                startActivity(intent);
                            }
                        }
                    });

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
