package bj.zemidjan.dio.activities;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import bj.zemidjan.dio.R;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class HelpActivity extends AppCompatActivity {

    public long vue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Button btn = (Button) findViewById(R.id.btn_action1);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Besoin d'aide ?");


        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();

        vue =  preferences.getLong("vueHelp",0);
        if(vue == 0){
            new MaterialTapTargetPrompt.Builder(HelpActivity.this)
                    .setTarget(findViewById(R.id.btn_action1))
                    .setPrimaryText("Nous joindre")
                    .setSecondaryText("Cliquez sur ce bouton et Contactez pour votre problème")
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
            editor.putLong("vueHelp", preferences.getLong("vueHelp",0)+1);
            editor.commit();
        }


        final Item[] items = {
                new Item("Envoyez nous un SMS", R.drawable.ic_assignment_black_24dp),
                new Item("Appelez nous", R.drawable.ic_call_black_24dp),
                new Item("Envoyer nous un mail", R.drawable.ic_email_black_24dp),
        };

        final ListAdapter adapter = new ArrayAdapter<Item>(
                HelpActivity.this,
                android.R.layout.select_dialog_item,
                android.R.id.text1,
                items){
            public View getView(int position, View convertView, ViewGroup parent) {
                //Use super class to create the View
                View v = super.getView(position, convertView, parent);
                TextView tv = (TextView)v.findViewById(android.R.id.text1);
                tv.setTextSize(15);
                tv.setPadding(80,0,0,0);

                //Put the image on the TextView
                tv.setCompoundDrawablesWithIntrinsicBounds(items[position].icon, 0, 0, 0);

                //Add margin between image and text (support various screen densities)
                int dp5 = (int) (25 * getResources().getDisplayMetrics().density + 0.5f);
                tv.setCompoundDrawablePadding(dp5);

                return v;
            }
        };

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(HelpActivity.this)
                        .setTitle("Contactez nous")
                        .setAdapter(adapter, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                if(item == 0){
                                    Intent sms = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:+22996326754"));
                                    sms.putExtra("sms_body", "Bonjour, EducArsenal");
                                    startActivity(sms);

                                }else if(item == 1){
                                    Intent appel = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:+22996326754"));
                                    startActivity(appel);



                                }else if(item == 2){
                                    Intent intent = new Intent(Intent.ACTION_SENDTO)
                                            .setData(new Uri.Builder().scheme("mailto").build())
                                            .putExtra(Intent.EXTRA_EMAIL, new String[]{"" })
                                            .putExtra(Intent.EXTRA_SUBJECT, "Probème sur EducAsenal")
                                            .putExtra(Intent.EXTRA_TEXT, "Bonjour EducArsenal ")
                                            ;

                                    ComponentName emailApp = intent.resolveActivity(HelpActivity.this.getPackageManager());
                                    ComponentName unsupportedAction = ComponentName.unflattenFromString("com.android.fallback/.Fallback");
                                    if (emailApp != null && !emailApp.equals(unsupportedAction))
                                        try {
                                            Intent chooser = Intent.createChooser(intent, "Send email with");
                                            startActivity(chooser);
                                            return;
                                        }
                                        catch (ActivityNotFoundException ignored) {
                                        }

                                    Toast
                                            .makeText(HelpActivity.this, "Impossible de trouver une application et un compte de messagerie", Toast.LENGTH_LONG)
                                            .show();



                                }
                            }
                        }).show();
            }
        });

    }

    public static class Item{
        public final String text;
        public final int icon;
        public Item(String text, Integer icon) {
            this.text = text;
            this.icon = icon;
        }
        @Override
        public String toString() {
            return text;
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
