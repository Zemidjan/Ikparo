package bj.zemidjan.dio.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import bj.zemidjan.dio.R;
import bj.zemidjan.dio.models.Domaine;

import java.util.ArrayList;

/**
 * Created by Oulfath on 21/09/2017.
 */

public class SousDomaineAdapter extends  RecyclerView.Adapter<SousDomaineAdapter.Holder>  {
    ArrayList<Domaine> domaines = new ArrayList<Domaine>();

    public SousDomaineAdapter(ArrayList<Domaine> domaines, Context context){
        if(domaines==null){
            this.domaines = new ArrayList<Domaine>();
        }
        else{
            this.domaines = domaines;
        }
    }


    public static class Holder extends RecyclerView.ViewHolder  {

        TextView  libelle;

        public Holder(View itemView) {
            super(itemView);
            libelle = (TextView)itemView.findViewById(R.id.libelle);
        }


        public void bind(Domaine currentDomaine){
            libelle.setText(currentDomaine.getLibelle());

        }

    }

    @Override
    public SousDomaineAdapter.Holder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sousdomaine, parent, false);
        return new SousDomaineAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(SousDomaineAdapter.Holder holder, final int i) {
        Domaine currentDomaine = domaines.get(i);
        holder.bind(currentDomaine);




    }

    @Override
    public long getItemId(int position) {
        return domaines.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return domaines.size();
    }
}
