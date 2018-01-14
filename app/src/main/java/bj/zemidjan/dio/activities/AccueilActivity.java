package bj.zemidjan.dio.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import bj.zemidjan.dio.R;
import bj.zemidjan.dio.fragments.DomaineFragment;
import bj.zemidjan.dio.fragments.SuivreCoursFragment;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class AccueilActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String nom, prenom, email;
    public long  vue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accueil);
         MaterialTapTargetPrompt mPrompt;


        if (findViewById(R.id.mainframe) != null) {
            final DomaineFragment listFragment = new DomaineFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.mainframe, listFragment).commit();
        }


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
       // setSupportActionBar(toolbar);



        final SharedPreferences preferences = getSharedPreferences("educarsenal", 0);
        SharedPreferences.Editor editor = preferences.edit();
        nom = preferences.getString("nom","");
        prenom = preferences.getString("prenoms","");
        email = preferences.getString("email","");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);


        TextView mailUser = (TextView)header.findViewById(R.id.mailUser);
        TextView nameUser = (TextView)header.findViewById(R.id.nameUser);
        mailUser.setText(email);
        nameUser.setText(prenom + " " + nom);

        vue =  preferences.getLong("AccueilActivity",0);
        if(vue == 0){
            new MaterialTapTargetPrompt.Builder(AccueilActivity.this)
                    .setTarget(findViewById(R.id.menu_search))
                    .setPrimaryText("Boutton Rechercher")
                    .setSecondaryText("Cliquez Sur la loupe pour faire une recherche ( domaines, cours, etc ) selon la liste affichée !")
                    .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener()
                    {
                        @Override
                        public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state)
                        {
                            if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED)
                            {

                            }
                        }
                    })
                    .show();
            editor.putLong("AccueilActivity", preferences.getLong("AccueilActivity",0)+1);
            editor.commit();
        }


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            final SharedPreferences preferences = getSharedPreferences("educarsenal", 0);
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            } else {
                if(preferences.getString("fragment","").equals("domaine")){
                    LayoutInflater factory = LayoutInflater.from(this);
                    final AlertDialog.Builder adb = new AlertDialog.Builder(this);
                    adb.setMessage("Voulez vous vraiment quitter  l'application ?");
                    adb.setTitle("Fermeture de l'application");
                    adb.setIcon(R.drawable.ic_power_settings_new_black_24dp);

                    adb.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            AccueilActivity.super.onBackPressed();
                        } });

                    adb.setNegativeButton("NON", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        } });
                    adb.show();
                }else{
                    final DomaineFragment listFragment = new DomaineFragment();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.mainframe, listFragment).commit();
                }

            }





        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.accueil, menu);

       /* final SharedPreferences preferences = getSharedPreferences("educarsenal", 0);
        final SharedPreferences.Editor editor = preferences.edit();
        vue =  preferences.getLong(" vueListDomaine",0);
        if(vue == 0) {
            new MaterialTapTargetPrompt.Builder(this)
                    .setTarget(findViewById(R.id.menu_search))
                    .setPrimaryText("Rechercher un domaine")
                    .setSecondaryText("Cliquez sur la loupe et rechercher votre domaine d'activité")
                    .setPromptStateChangeListener(new MaterialTapTargetPrompt.PromptStateChangeListener() {
                        @Override
                        public void onPromptStateChanged(MaterialTapTargetPrompt prompt, int state) {
                            if (state == MaterialTapTargetPrompt.STATE_FOCAL_PRESSED) {

                            }
                        }
                    })
                    .show();
            editor.putLong(" vueListDomaine", preferences.getLong("vueListDomaine", 0) + 1);
            editor.commit();
        }*/

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            LayoutInflater factory = LayoutInflater.from(AccueilActivity.this);
            final View alertDialogView = factory.inflate(R.layout.apropos_activity, null);

            AlertDialog.Builder adb = new AlertDialog.Builder(this);

            adb.setView(alertDialogView);

            final AlertDialog dialog = adb.create();
            dialog.show();
            return true;
        }



        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_domaine) {
            startActivity(new Intent(AccueilActivity.this, AccueilActivity.class));
            finish();

        } else if (id == R.id.nav_cours) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.mainframe,new SuivreCoursFragment())
                    .commit();
        } else if (id == R.id.nav_forum) {
            Intent intentHome = new Intent(AccueilActivity.this, ForumActivity.class);
            startActivity(intentHome);
            return true;

        } else if (id == R.id.nav_share) {
            Intent share = new Intent(android.content.Intent.ACTION_SEND);
            share.setType("text/plain");

            share.putExtra(Intent.EXTRA_SUBJECT, "APP");
            share.putExtra(Intent.EXTRA_TEXT, "Découvrez EducArsenal et Devenez expert dans votre domaine grace à notre sélection de cours en ligne." +
                    "Veuillez vous rendre sur le google play store pour télécharger");

            startActivity(Intent.createChooser(share, "Choisir une application pour le partage"));
            return true;

        }else if (id == R.id.nav_deconnexion) {
            LayoutInflater factory = LayoutInflater.from(this);
            final AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setMessage("Voulez vous vraiment vous déconnecter de l'application ?");
            adb.setTitle("Déconnexion");
            adb.setIcon(R.drawable.ic_power_settings_new_black_24dp);

            adb.setPositiveButton("OUI", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(AccueilActivity.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putLong("id", 0);
                    editor.putString("nom", "");
                    editor.putString("prenoms", "");
                    editor.putString("email", "");
                    editor.putString("telephone", "");
                    editor.putString("sexe", "");
                    editor.commit();
                    finish();
                    startActivity(new Intent(AccueilActivity.this,  ConnexionActivity.class).putExtra("action","1"));
                } });

            adb.setNegativeButton("NON", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                } });
            adb.show();

        }else if (id == R.id.nav_help) {
            Intent intentHome = new Intent(AccueilActivity.this, HelpActivity.class);
            startActivity(intentHome);
            return true;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }





}
