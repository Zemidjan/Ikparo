package bj.zemidjan.dio.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import bj.zemidjan.dio.R;
import bj.zemidjan.dio.adapter.ForumAdapter;
import bj.zemidjan.dio.models.Forum;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import uk.co.samuelwall.materialtaptargetprompt.MaterialTapTargetPrompt;

public class ForumActivity extends AppCompatActivity {
    RecyclerView mRecyclerView;
    private ArrayList<Forum> forums;
    public ArrayList<Forum> listForums = null;
    public ArrayList<Forum> fora = new ArrayList<Forum>();
    public String textMessage;
    public long id, vue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        final EditText message = (EditText) findViewById(R.id.message);
        id = 100;
        mRecyclerView=(RecyclerView) findViewById(R.id.forum);
        setSupportActionBar(toolbar);
        final Gson gson = new Gson();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Forum de discussion");
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        final SharedPreferences.Editor editor = preferences.edit();

        String jsonForums = preferences.getString("listForas", null);
        if (jsonForums != null) {
            fora = gson.fromJson(jsonForums, new TypeToken<ArrayList<Forum>>() {
            }.getType());

            mRecyclerView.setHasFixedSize(true);


            LinearLayoutManager layoutManager = new LinearLayoutManager(ForumActivity.this);
            mRecyclerView.setLayoutManager(layoutManager);

            ForumAdapter adapter = new ForumAdapter(fora, ForumActivity.this);


            mRecyclerView.setAdapter(adapter);
        }



        vue =  preferences.getLong("vueForum",0);
        if(vue == 0){
            new MaterialTapTargetPrompt.Builder(ForumActivity.this)
                    .setTarget(findViewById(R.id.fab))
                    .setPrimaryText("Démarrer une discussion")
                    .setSecondaryText("Saisissez votre message et Cliquez sur le bouton pour démarrer une discussion avec des abonnés du même domaine que vous")
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
            editor.putLong("vueForum", preferences.getLong("vueForum",0)+1);
            editor.commit();
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textMessage = message.getText().toString().trim();
                if (TextUtils.isEmpty(textMessage)) {
                    message.setError("Veuillez saisir votre message !");
                    return;
                }else{

                    String jsonForums = preferences.getString("listForas", null);
                    if (jsonForums != null) {
                        fora = gson.fromJson(jsonForums, new TypeToken<ArrayList<Forum>>() {
                        }.getType());
                    }


                    Forum forum;
                    forum = new Forum();
                    forum.setId(id+1);
                    forum.setTexte(textMessage);
                    fora.add(forum);
                    String jsonForas = gson.toJson(fora);
                    editor.putString("listForas", jsonForas)
                            .apply();
                    editor.commit();
                    mRecyclerView.setHasFixedSize(true);


                    LinearLayoutManager layoutManager = new LinearLayoutManager(ForumActivity.this);
                    mRecyclerView.setLayoutManager(layoutManager);

                    ForumAdapter adapter = new ForumAdapter(fora, ForumActivity.this);


                    mRecyclerView.setAdapter(adapter);
                    message.setText("");







                }




            }
        });
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
