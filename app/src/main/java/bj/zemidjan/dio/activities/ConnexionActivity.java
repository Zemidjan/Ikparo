package bj.zemidjan.dio.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import bj.zemidjan.dio.R;
import bj.zemidjan.dio.models.Utilisateur;
import bj.zemidjan.dio.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Oulfath on 28/08/2017.
 */
public class ConnexionActivity extends AppCompatActivity{

    private Button btnAction1, btnAction2;
    private EditText nom, prenom, email,password, telephone;
    private ProgressBar progressBar;
    private Spinner sexe;
    LinearLayout blockName, block, blockImage;
    CoordinatorLayout coordinatorLayoutAction;
    private Intent intent;
    public String textNom,textPrenom,textEmail,textPassword,textTelephone, textSexe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connexion);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        intent = getIntent();
        final String action = intent.getStringExtra("action");

        btnAction1 = (Button) findViewById(R.id.btn_action1);
        btnAction2 = (Button) findViewById(R.id.btn_action2);
        blockName = (LinearLayout) findViewById(R.id.block_name);
        blockImage = (LinearLayout) findViewById(R.id.block_image);
        block = (LinearLayout) findViewById(R.id.block);
        nom = (EditText) findViewById(R.id.nom);
        prenom = (EditText) findViewById(R.id.prenom);
        email = (EditText) findViewById(R.id.email);
        telephone = (EditText) findViewById(R.id.telephone);
        password = (EditText) findViewById(R.id.password);
        coordinatorLayoutAction = (CoordinatorLayout) findViewById(R.id.coordinatorLayoutConnect);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        sexe = (Spinner) findViewById(R.id.sexe);


        if (action.equals("1")) {
            btnAction1.setText(R.string.connecter);
            btnAction2.setText(R.string.inscription);
            blockName.setVisibility(View.GONE);
            blockImage.setVisibility(View.GONE);
            block.setVisibility(View.GONE);
            btnAction2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    startActivity(new Intent(v.getContext(), ConnexionActivity.class).putExtra("action", "2"));
                }
            });

        } else {
            btnAction1.setText(R.string.sinscrire);
            btnAction2.setText(R.string.connexion);
            blockName.setVisibility(View.VISIBLE);
            blockImage.setVisibility(View.GONE);
            block.setVisibility(View.VISIBLE);
            btnAction2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    startActivity(new Intent(v.getContext(), ConnexionActivity.class).putExtra("action", "1"));
                }
            });
        }
        List<String> typesexe = new ArrayList<String>();
        typesexe.add("Féminin");
        typesexe.add("Maxculin");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, typesexe);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sexe.setAdapter(dataAdapter);

        btnAction1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textEmail = email.getText().toString().trim();
                textPassword = password.getText().toString().trim();
                textNom = nom.getText().toString().trim();
                textPrenom = prenom.getText().toString().trim();
                textTelephone = telephone.getText().toString().trim();
                if (TextUtils.isEmpty(textEmail)) {
                    email.setError("Veuillez entrer votre email !");
                    return;
                }

                if( Utils.isValiEmail(textEmail) == false){
                    email.setError("Veuillez entrez un email correct !");
                    return;
                }
                if (TextUtils.isEmpty(textPassword)) {
                    password.setError("Veuillez entrer votre mot de passe !");
                    return;
                }

                if (password.length() > 8) {
                    password.setError("Le mot de passe doit etre au maximum de 8 caractères !");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                if (action.equals("2")) {

                    if (TextUtils.isEmpty(textNom)) {
                        nom.setError("Veuillez entrez votre nom !");
                        return;
                    }
                    if (TextUtils.isEmpty(textPrenom)) {
                        nom.setError("Veuillez entrez votre prénom !");
                        return;
                    }
                    if (TextUtils.isEmpty(textTelephone)) {
                        nom.setError("Veuillez entrez votre télephone !");
                        return;
                    }

                    textSexe = sexe.getSelectedItem().toString();

                    Call<Utilisateur> inscription = Utils.retrofitBuilder().inscription(textNom, textPrenom, textEmail, textTelephone, textSexe, textPassword);
                    inscription.enqueue(new Callback<Utilisateur>() {
                        @Override
                        public void onResponse(Call<Utilisateur> call, Response<Utilisateur> response) {

                            if (response.code() == 206){
                                progressBar.setVisibility(View.GONE);
                                final SharedPreferences preferences = getSharedPreferences("educarsenal", 0);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putLong("id", response.body().getId());
                                editor.putString("nom", response.body().getNom());
                                editor.putString("prenoms", response.body().getPrenom());
                                editor.putString("email", response.body().getEmail());
                                editor.putString("telephone", response.body().getTelephone());
                                editor.putString("sexe", response.body().getSexe());
                                editor.commit();
                                startActivity(new Intent(ConnexionActivity.this, AccueilActivity.class));
                                finish();
                            }else{
                                Snackbar.make(coordinatorLayoutAction,  "Erreur, Vérifier la connexion Internet", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                return;
                            }
                        }

                        @Override
                        public void onFailure(Call<Utilisateur> call, Throwable t) {
                            Snackbar.make(coordinatorLayoutAction,  R.string.register_failed , Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            return;
                        }
                    });
                }else{
                    Call<Utilisateur> connexion = Utils.retrofitBuilder().connexion(textEmail, textPassword);
                    connexion.enqueue(new Callback<Utilisateur>() {
                        @Override
                        public void onResponse(Call<Utilisateur> call, Response<Utilisateur> response) {

                            if (response.code() == 206){
                                progressBar.setVisibility(View.GONE);
                                final SharedPreferences preferences = getSharedPreferences("educarsenal", 0);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putLong("id", response.body().getId());
                                editor.putString("nom", response.body().getNom());
                                editor.putString("prenoms", response.body().getPrenom());
                                editor.putString("email", response.body().getEmail());
                                editor.putString("telephone", response.body().getTelephone());
                                editor.putString("sexe", response.body().getSexe());
                                editor.commit();
                                startActivity(new Intent(ConnexionActivity.this, AccueilActivity.class));
                                finish();
                            }else{
                                Snackbar.make(coordinatorLayoutAction,  "Erreur, Vérifier la connexion Internet", Snackbar.LENGTH_LONG)
                                        .setAction("Action", null).show();
                                return;
                            }
                        }

                        @Override
                        public void onFailure(Call<Utilisateur> call, Throwable t) {
                            Snackbar.make(coordinatorLayoutAction, R.string.connexion_failed , Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            return;
                        }
                    });
                }



            }
        });






    }
        }

