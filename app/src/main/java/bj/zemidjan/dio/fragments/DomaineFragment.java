package bj.zemidjan.dio.fragments;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;

/**
 * Created by Oulfath on 11/09/2017.
 */


import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import bj.zemidjan.dio.R;
import bj.zemidjan.dio.adapter.DomaineAdapter;
import bj.zemidjan.dio.models.Domaine;
import bj.zemidjan.dio.utils.RecyclerItemClickListener;
import bj.zemidjan.dio.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DomaineFragment extends Fragment  {
    public View rootView;
    RecyclerView mRecyclerView;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<Domaine> domaines;
    public ArrayList<Domaine> listDomaines = null;
    public long  vue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView=inflater.inflate(R.layout.fragment_domaine,container,false);

        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);

        loadList(rootView);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadList(rootView);
            }
        });




        return rootView;

        /*mRecyclerView=(RecyclerView) rootView.findViewById(R.id.list);
        final ArrayList<Domaine>  listes = DomaineFragment.getRawData();

        mRecyclerView.setHasFixedSize(true);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,1);
        mRecyclerView.setLayoutManager(layoutManager);

        DomaineAdapter adapter=  new DomaineAdapter(listes,container.getContext());

        mRecyclerView.setAdapter(adapter);

        return  rootView;*/
    }


    private void loadList(final View rootView) {
        mRecyclerView=(RecyclerView) rootView.findViewById(R.id.list);
        final LinearLayout block_loading = (LinearLayout) rootView.findViewById(R.id.block_loading);
        final ProgressBar loading = (ProgressBar) rootView.findViewById(R.id.loading);
        final LinearLayout root = (LinearLayout) rootView.findViewById(R.id.fragment_root);
        final TextView message_loading = (TextView) rootView.findViewById(R.id.messageLoading);
        final SharedPreferences preferences = getActivity().getSharedPreferences("educarsenal", 0);
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putString("fragment", "domaine")
                .apply();
        editor.commit();
        final Gson gson = new Gson();
        final String search = preferences.getString("recherche","");
        if(search.equals("")) {

            String jsonDomaine = preferences.getString("listDomaines", null);
            if (jsonDomaine != null) {
                listDomaines = gson.fromJson(jsonDomaine, new TypeToken<ArrayList<Domaine>>() {
                }.getType());
                domaines = listDomaines;
                mRecyclerView.setHasFixedSize(true);
                StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, 1);
                mRecyclerView.setLayoutManager(layoutManager);
                DomaineAdapter adapter = new DomaineAdapter(domaines, getContext());
                mRecyclerView.setAdapter(adapter);
            }
            block_loading.setVisibility(View.GONE);
            mSwipeRefreshLayout.setRefreshing(true);

            Call<ArrayList<Domaine>> list = Utils.retrofitBuilder().listDomaine();
            list.enqueue(new Callback<ArrayList<Domaine>>() {
                @Override
                public void onResponse(Call<ArrayList<Domaine>> call, Response<ArrayList<Domaine>> response) {
                    if (response.code() == 206) {
                        final ArrayList<Domaine> data = response.body();
                        domaines = data;
                        if (data.isEmpty()) {
                            mRecyclerView.setVisibility(View.GONE);
                            block_loading.setVisibility(View.VISIBLE);
                            loading.setVisibility(View.GONE);
                            message_loading.setVisibility(View.VISIBLE);
                            message_loading.setText("Aucun domaine enrégistré  pour le moment!");
                            mSwipeRefreshLayout.setRefreshing(false);
                        } else {
                            String jsonDomaines = gson.toJson(data);
                            editor.putString("listDomaines", jsonDomaines)
                                    .apply();
                            editor.commit();
                            mRecyclerView.setHasFixedSize(true);
                            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, 1);
                            mRecyclerView.setLayoutManager(layoutManager);
                            DomaineAdapter adapter = new DomaineAdapter(domaines, getContext());
                            mRecyclerView.setAdapter(adapter);
                            root.setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            block_loading.setVisibility(View.GONE);
                            message_loading.setVisibility(View.GONE);
                            mSwipeRefreshLayout.setRefreshing(false);
                            return;
                        }
                    } else {
                        block_loading.setVisibility(View.GONE);
                        message_loading.setVisibility(View.GONE);
                        Snackbar.make(rootView, "Echec, Vérifiez la connexion", Snackbar.LENGTH_INDEFINITE)
                                .setAction("Réessayer", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        loadList(rootView);
                                    }
                                }).show();
                        mSwipeRefreshLayout.setRefreshing(false);
                        return;
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Domaine>> call, Throwable t) {
                    block_loading.setVisibility(View.GONE);

                    message_loading.setVisibility(View.GONE);
                    Snackbar.make(rootView, "Connexion Instable", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Réessayer", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    loadList(rootView);
                                }
                            }).show();
                    mSwipeRefreshLayout.setRefreshing(false);
                    return;
                }
            });
            mRecyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(rootView.getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            position += 1;
                            CoursFragment fragment = new CoursFragment();
                            Bundle args = new Bundle();
                            args.putString("domaineId", "" + position);
                            args.putString("domaine", domaines.get(position - 1).getLibelle());



                            getFragmentManager().beginTransaction()
                                    .replace(R.id.mainframe, fragment.newInstance(args))
                                    .commit();


                        }
                    })
            );
        }else{
            String jsonDomaine = preferences.getString("listDomaines", null);
            if (jsonDomaine != null) {
                listDomaines = gson.fromJson(jsonDomaine, new TypeToken<ArrayList<Domaine>>() {
                }.getType());

                //jsonDomaines = gson.fromJson(jsonDomaines, new TypeToken<ArrayList<Domaine>>() {
                //}.getType());
                final ArrayList<Domaine> resultDomaine = new ArrayList<Domaine>();
                final ArrayList<Domaine> resultSearchDomaine = listDomaines;
                int resultSearchDomaineLength = resultSearchDomaine.size();

                for (int i = 0; i < resultSearchDomaineLength; i++) {
                    if ((resultSearchDomaine.get(i).getLibelle().toLowerCase().contains(search.toLowerCase())) ) {
                        resultDomaine.add(resultSearchDomaine.get(i));

                    }
                }
                mRecyclerView.setHasFixedSize(true);
                StaggeredGridLayoutManager gaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
                mRecyclerView.setLayoutManager(gaggeredGridLayoutManager);
                DomaineAdapter adapter = new DomaineAdapter(resultDomaine, getContext());
                mRecyclerView.setAdapter(adapter);
                String jsonDomaines = gson.toJson(resultDomaine);
                editor.putString("listDomainesRecherche", jsonDomaines)
                        .apply();
                editor.commit();
                editor.putString("recherche","");
                editor.commit();
                loading.setVisibility(View.GONE);


                mRecyclerView.addOnItemTouchListener(
                        new RecyclerItemClickListener(rootView.getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                position += 1;
                                CoursFragment fragment = new CoursFragment();
                                Bundle args = new Bundle();
                                args.putString("domaineId", "" + position);
                                args.putString("domaine", resultDomaine.get(position - 1).getLibelle());



                                getFragmentManager().beginTransaction()
                                        .replace(R.id.mainframe, fragment.newInstance(args))
                                        .commit();


                            }
                        })
                );
            }





        }

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = (Toolbar)view.findViewById(R.id.toolbar);


        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout)((AppCompatActivity)getActivity()).findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle((AppCompatActivity)getActivity(), drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("EducArsenal");


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }






    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.accueil, menu);
       /* final SharedPreferences preferences = getActivity().getSharedPreferences("educarsenal", 0);
        final SharedPreferences.Editor editor = preferences.edit();
        vue =  preferences.getLong(" vueListDomaine",0);
        if(vue == 0) {
            new MaterialTapTargetPrompt.Builder(getActivity())
                    .setTarget(getActivity().findViewById(R.id.menu_search))
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
        final MenuItem searchItem = menu.findItem(R.id.menu_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                final SharedPreferences preferences = getActivity().getSharedPreferences("educarsenal", 0);
                final SharedPreferences.Editor editor = preferences.edit();
                editor.putString("recherche",query);
                editor.commit();
                loadList(rootView);



                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                final SharedPreferences preferences = getActivity().getSharedPreferences("educarsenal", 0);
                final SharedPreferences.Editor editor = preferences.edit();
                editor.putString("recherche",s);
                editor.commit();
                loadList(rootView);


                return false;
            }
        });

    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }


}



