package bj.zemidjan.dio.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import bj.zemidjan.dio.R;
import bj.zemidjan.dio.models.Domaine;

import java.util.ArrayList;

/**
 * Created by Oulfath on 17/09/2017.
 */
public class DomaineAdapter extends RecyclerView.Adapter<DomaineAdapter.Holder>{
    ArrayList<Domaine> domaines = new ArrayList<Domaine>();

    public DomaineAdapter(ArrayList<Domaine> domaines, Context context){
        if(domaines==null){
            this.domaines = new ArrayList<Domaine>();
        }
        else{
            this.domaines = domaines;
        }
    }


    public static class Holder extends RecyclerView.ViewHolder  {

        TextView libelle;
        ImageView image;

        public Holder(View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.image);
            libelle = (TextView)itemView.findViewById(R.id.libelle);
        }


        public void bind(Domaine currentDomaine){
            Glide.with(image.getContext()).load(currentDomaine.getImage()).into(image);
            libelle.setText(currentDomaine.getLibelle());

        }


    }

    @Override
    public DomaineAdapter.Holder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_domaine, parent, false);
        return new DomaineAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int i) {
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
