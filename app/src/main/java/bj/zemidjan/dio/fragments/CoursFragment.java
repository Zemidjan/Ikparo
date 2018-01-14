package bj.zemidjan.dio.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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

import bj.zemidjan.dio.activities.AffichageCoursActivity;
import bj.zemidjan.dio.R;
import bj.zemidjan.dio.activities.AffichageProfilActivity;
import bj.zemidjan.dio.adapter.CoursAdapter;
import bj.zemidjan.dio.adapter.ProfilAdapter;
import bj.zemidjan.dio.adapter.SousDomaineAdapter;
import bj.zemidjan.dio.models.Cours;
import bj.zemidjan.dio.models.Domaine;
import bj.zemidjan.dio.models.ProfilInspirant;
import bj.zemidjan.dio.utils.RecyclerItemClickListener;
import bj.zemidjan.dio.utils.Utils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Oulfath on 19/09/2017.
 */

public class CoursFragment extends Fragment {
    public View rootView;
    RecyclerView recyclerViewCours, recyclerViewProfil, recyclerViewDomaine;
    public SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<Domaine> domaines;
    public ArrayList<Domaine> listDomaines = null;
    private ArrayList<Cours> cours;
    public ArrayList<Cours> listCours = null;
    private ArrayList<ProfilInspirant> profils;
    public ArrayList<ProfilInspirant> listProfils = null;
    public String domaineLibelle;
    public String domaineId;
    public long vue;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView=inflater.inflate(R.layout.fragment_cours,container,false);
        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);
        final SharedPreferences preferences = getActivity().getSharedPreferences("educarsenal", 0);
        Bundle args = getArguments();
        domaineId = args.getString("domaineId");
        domaineLibelle = args.getString("domaine");


        loadListDomaine(rootView, domaineId);
        loadListProfil(rootView, domaineId);
        loadListCours(rootView, domaineId);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadListDomaine(rootView, domaineId);
                loadListProfil(rootView, domaineId);
                loadListCours(rootView, domaineId);
            }
        });

        return rootView;
    }

    public static CoursFragment newInstance(Bundle b) {
        CoursFragment fragment = new CoursFragment();
        fragment.setArguments(b);

        return fragment;
    }
    private void loadListDomaine(final View rootView, final String domaineId) {
        recyclerViewDomaine=(RecyclerView) rootView.findViewById(R.id.list_sousdomaine);
        final LinearLayout block_loading = (LinearLayout) rootView.findViewById(R.id.block_loading);
        final ProgressBar loading = (ProgressBar) rootView.findViewById(R.id.loading);
        final LinearLayout root = (LinearLayout) rootView.findViewById(R.id.fragment_root);
        final TextView message_loading = (TextView) rootView.findViewById(R.id.messageLoading);
        final SharedPreferences preferences = getActivity().getSharedPreferences("educarsenal", 0);
        final SharedPreferences.Editor editor = preferences.edit();
        final Gson gson = new Gson();

        String jsonSousDomaines = preferences.getString("listSousDomaines"+domaineId, null);
        if (jsonSousDomaines != null) {
            listDomaines = gson.fromJson(jsonSousDomaines, new TypeToken<ArrayList<Domaine>>() {
            }.getType());
            domaines = listDomaines;
            recyclerViewDomaine.setHasFixedSize(true);
            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.HORIZONTAL, false);

            recyclerViewDomaine.setLayoutManager(layoutManager);
            SousDomaineAdapter adapter = new SousDomaineAdapter(domaines, getContext());
            recyclerViewDomaine.setAdapter(adapter);
        }
        block_loading.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(true);

        Call<ArrayList<Domaine>> listD = Utils.retrofitBuilder().listSousDomaine(domaineId);
        listD.enqueue(new Callback<ArrayList<Domaine>>() {
            @Override
            public void onResponse(Call<ArrayList<Domaine>> call, Response<ArrayList<Domaine>> response) {
                if (response.code() == 206) {
                    final ArrayList<Domaine> data = response.body();
                    domaines = data;
                    if (data.isEmpty()) {
                        recyclerViewDomaine.setVisibility(View.GONE);
                        block_loading.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(false);
                    } else {
                        String jsonSousDomaines = gson.toJson(data);
                        editor.putString("listSousDomaines"+domaineId, jsonSousDomaines)
                                .apply();
                        editor.commit();
                        recyclerViewDomaine.setHasFixedSize(true);
                        LinearLayoutManager layoutManager
                                = new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.HORIZONTAL, false);
                        recyclerViewDomaine.setLayoutManager(layoutManager);
                        SousDomaineAdapter adapter = new SousDomaineAdapter(domaines, getContext());
                        recyclerViewDomaine.setAdapter(adapter);
                        root.setVisibility(View.VISIBLE);
                        recyclerViewDomaine.setVisibility(View.VISIBLE);
                        block_loading.setVisibility(View.GONE);
                        message_loading.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(false);
                        return;
                    }
                } else {
                    block_loading.setVisibility(View.GONE);
                    message_loading.setVisibility(View.GONE);

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
                                loadListDomaine(rootView, domaineId);
                            }
                        }).show();
                mSwipeRefreshLayout.setRefreshing(false);
                return;
            }
        });
        recyclerViewDomaine.addOnItemTouchListener(
                new RecyclerItemClickListener(rootView.getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {

                        CoursFragment fragment = new CoursFragment();
                        Bundle args = new Bundle();
                        args.putString("domaineId", ""+domaines.get(position).getId());
                        args.putString("domaine", domaines.get(position).getLibelle());


                        getFragmentManager().beginTransaction()
                                .replace(R.id.mainframe,fragment.newInstance(args))
                                .commit();


                    }
                })
        );

    }




    private void loadListProfil(final View rootView, final String domaineId) {
        recyclerViewProfil=(RecyclerView) rootView.findViewById(R.id.list_profil);
        final LinearLayout block_loading = (LinearLayout) rootView.findViewById(R.id.block_loading);
        final ProgressBar loading = (ProgressBar) rootView.findViewById(R.id.loading);
        final LinearLayout block_profil = (LinearLayout) rootView.findViewById(R.id.root_profil);
        block_profil.setVisibility(View.GONE);
        final LinearLayout root = (LinearLayout) rootView.findViewById(R.id.fragment_root);
        final TextView message_loading = (TextView) rootView.findViewById(R.id.messageLoading);
        final SharedPreferences preferences = getActivity().getSharedPreferences("educarsenal", 0);
        final SharedPreferences.Editor editor = preferences.edit();
        final Gson gson = new Gson();

        String jsonProfils = preferences.getString("listProfils"+domaineId, null);
        if (jsonProfils != null) {
            listProfils = gson.fromJson(jsonProfils, new TypeToken<ArrayList<ProfilInspirant>>() {
            }.getType());
            profils = listProfils;
            recyclerViewProfil.setHasFixedSize(true);
            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.HORIZONTAL, false);

            recyclerViewProfil.setLayoutManager(layoutManager);
            ProfilAdapter adapter = new ProfilAdapter(profils, getContext());
            recyclerViewProfil.setAdapter(adapter);
            block_profil.setVisibility(View.VISIBLE);
        }
        block_loading.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(true);

        Call<ArrayList<ProfilInspirant>> listP = Utils.retrofitBuilder().listProfil(domaineId);
        listP.enqueue(new Callback<ArrayList<ProfilInspirant>>() {
            @Override
            public void onResponse(Call<ArrayList<ProfilInspirant>> call, Response<ArrayList<ProfilInspirant>> response) {
                if (response.code() == 206) {
                    final ArrayList<ProfilInspirant> data = response.body();
                    profils = data;
                    if (data.isEmpty()) {
                        recyclerViewProfil.setVisibility(View.GONE);
                        block_loading.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(false);
                        block_profil.setVisibility(View.GONE);
                    } else {
                        String jsonProfils = gson.toJson(data);
                        editor.putString("listProfils"+domaineId, jsonProfils)
                                .apply();
                        editor.commit();
                        recyclerViewProfil.setHasFixedSize(true);
                        LinearLayoutManager layoutManager
                                = new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.HORIZONTAL, false);
                        recyclerViewProfil.setLayoutManager(layoutManager);
                        ProfilAdapter adapter = new ProfilAdapter(profils, getContext());
                        recyclerViewProfil.setAdapter(adapter);
                        root.setVisibility(View.VISIBLE);
                        recyclerViewProfil.setVisibility(View.VISIBLE);
                        block_loading.setVisibility(View.GONE);
                        message_loading.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(false);
                        block_profil.setVisibility(View.VISIBLE);
                        return;
                    }
                } else {
                    block_loading.setVisibility(View.GONE);
                    message_loading.setVisibility(View.GONE);

                    mSwipeRefreshLayout.setRefreshing(false);
                    block_profil.setVisibility(View.GONE);
                    return;
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ProfilInspirant>> call, Throwable t) {
                block_loading.setVisibility(View.GONE);
                message_loading.setVisibility(View.GONE);
                Snackbar.make(rootView, "Connexion Instable", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Réessayer", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadListProfil(rootView, domaineId);
                            }
                        }).show();
                mSwipeRefreshLayout.setRefreshing(false);
                block_profil.setVisibility(View.GONE);
                return;
            }
        });

        recyclerViewProfil.addOnItemTouchListener(
                new RecyclerItemClickListener(rootView.getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent = new Intent(view.getContext(), AffichageProfilActivity.class);
                        //position = position+1;
                        intent.putExtra("id",""+position);
                        intent.putExtra("domaineId",""+domaineId);
                        rootView.getContext().startActivity(intent);
                    }
                })
        );

    }





    private void loadListCours(final View rootView, final String domaineId) {
        recyclerViewCours=(RecyclerView) rootView.findViewById(R.id.list_cours);
        final LinearLayout block_loading = (LinearLayout) rootView.findViewById(R.id.block_loading);
        final ProgressBar loading = (ProgressBar) rootView.findViewById(R.id.loading);
        final LinearLayout root = (LinearLayout) rootView.findViewById(R.id.fragment_root);
        final TextView message_loading = (TextView) rootView.findViewById(R.id.messageLoading);
        final SharedPreferences preferences = getActivity().getSharedPreferences("educarsenal", 0);
        final SharedPreferences.Editor editor = preferences.edit();
        editor.putString("fragment", "cours")
                .apply();
        editor.commit();
        final Gson gson = new Gson();

        final String search = preferences.getString("rechercheCours","");
        if(search.equals("")) {

        String jsonCours = preferences.getString("listCours"+domaineId, null);
        if (jsonCours != null) {
            listCours = gson.fromJson(jsonCours, new TypeToken<ArrayList<Cours>>() {
            }.getType());
            cours = listCours;
            recyclerViewCours.setHasFixedSize(true);
            LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext());
            recyclerViewCours.setLayoutManager(layoutManager);
            CoursAdapter adapter = new CoursAdapter(cours, getContext());
            recyclerViewCours.setAdapter(adapter);
        }
        block_loading.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(true);

        Call<ArrayList<Cours>> listC = Utils.retrofitBuilder().listCours(domaineId);
        listC.enqueue(new Callback<ArrayList<Cours>>() {
            @Override
            public void onResponse(Call<ArrayList<Cours>> call, Response<ArrayList<Cours>> response) {
                if (response.code() == 206) {
                    final ArrayList<Cours> data = response.body();
                    cours = data;
                    if (data.isEmpty()) {
                        recyclerViewCours.setVisibility(View.GONE);
                        block_loading.setVisibility(View.VISIBLE);
                        message_loading.setVisibility(View.VISIBLE);
                        loading.setVisibility(View.GONE);
                        message_loading.setText("Aucun cours enrégistré  pour ce domaine!");
                        mSwipeRefreshLayout.setRefreshing(false);
                    } else {
                        String jsonCours = gson.toJson(data);
                        editor.putString("listCours"+domaineId, jsonCours)
                                .apply();
                        editor.commit();
                        recyclerViewCours.setHasFixedSize(true);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext());
                        recyclerViewCours.setLayoutManager(layoutManager);
                        CoursAdapter adapter = new CoursAdapter(cours, getContext());
                        recyclerViewCours.setAdapter(adapter);
                        root.setVisibility(View.VISIBLE);
                        recyclerViewCours.setVisibility(View.VISIBLE);
                        block_loading.setVisibility(View.GONE);
                        message_loading.setVisibility(View.GONE);
                        mSwipeRefreshLayout.setRefreshing(false);
                        return;
                    }
                } else {
                    block_loading.setVisibility(View.GONE);
                    message_loading.setVisibility(View.GONE);

                    mSwipeRefreshLayout.setRefreshing(false);
                    return;
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Cours>> call, Throwable t) {
                block_loading.setVisibility(View.GONE);
                message_loading.setVisibility(View.GONE);
                Snackbar.make(rootView, "Connexion Instable", Snackbar.LENGTH_INDEFINITE)
                        .setAction("Réessayer", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                loadListCours(rootView, domaineId);
                            }
                        }).show();
                mSwipeRefreshLayout.setRefreshing(false);
                return;
            }
        });

        recyclerViewCours.addOnItemTouchListener(
                new RecyclerItemClickListener(rootView.getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        Intent intent = new Intent(view.getContext(), AffichageCoursActivity.class);
                        //position = position+1;
                        intent.putExtra("id",""+position);
                        intent.putExtra("domaineId",""+domaineId);
                        intent.putExtra("rechercher","non");
                        rootView.getContext().startActivity(intent);
                    }
                })
        );


        }else{

            String jsonCours = preferences.getString("listCours"+domaineId, null);
            if (jsonCours != null) {
                listCours = gson.fromJson(jsonCours, new TypeToken<ArrayList<Cours>>() {
                }.getType());

                //jsonDomaines = gson.fromJson(jsonDomaines, new TypeToken<ArrayList<Domaine>>() {
                //}.getType());
                final ArrayList<Cours> resultCours = new ArrayList<Cours>();
                final ArrayList<Cours> resultSearchCours = listCours;
                int resultSearchCoursLength = resultSearchCours.size();

                for (int i = 0; i < resultSearchCoursLength; i++) {
                    if ((resultSearchCours.get(i).getTitre().toLowerCase().contains(search.toLowerCase()))  ||
                            (resultSearchCours.get(i).getDescription().toLowerCase().contains(search.toLowerCase())) ||
                            (resultSearchCours.get(i).getDomaine().getLibelle().toLowerCase().contains(search.toLowerCase()))
                            ) {
                        resultCours.add(resultSearchCours.get(i));

                    }
                }
                recyclerViewCours.setHasFixedSize(true);
                LinearLayoutManager gaggeredGridLayoutManager = new LinearLayoutManager(rootView.getContext());
                recyclerViewCours.setLayoutManager(gaggeredGridLayoutManager);
                CoursAdapter adapter = new CoursAdapter(resultCours, getContext());
                recyclerViewCours.setAdapter(adapter);
                jsonCours = gson.toJson(resultCours);
                editor.putString("listCoursRecherche", jsonCours)
                        .apply();
                editor.commit();
                editor.putString("rechercheCours","");
                editor.commit();
                loading.setVisibility(View.GONE);

                recyclerViewCours.addOnItemTouchListener(
                        new RecyclerItemClickListener(rootView.getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                            @Override public void onItemClick(View view, int position) {
                                Intent intent = new Intent(view.getContext(), AffichageCoursActivity.class);
                                //position = position+1;
                                intent.putExtra("id",""+position);
                                intent.putExtra("domaineId",""+domaineId);
                                intent.putExtra("rechercher","oui");
                                rootView.getContext().startActivity(intent);
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

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(domaineLibelle);
        

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.accueil, menu);

        final MenuItem searchItem = menu.findItem(R.id.menu_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                final SharedPreferences preferences = getActivity().getSharedPreferences("educarsenal", 0);
                final SharedPreferences.Editor editor = preferences.edit();
                editor.putString("rechercheCours",query);
                editor.commit();
                loadListCours(rootView,domaineId);



                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                final SharedPreferences preferences = getActivity().getSharedPreferences("educarsenal", 0);
                final SharedPreferences.Editor editor = preferences.edit();
                editor.putString("rechercheCours",s);
                editor.commit();
                loadListCours(rootView,domaineId);


                return false;
            }
        });

    }





    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

}
