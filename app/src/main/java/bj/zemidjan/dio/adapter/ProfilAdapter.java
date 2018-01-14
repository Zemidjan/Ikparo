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
import bj.zemidjan.dio.models.ProfilInspirant;

import java.util.ArrayList;

/**
 * Created by Oulfath on 21/09/2017.
 */

public class ProfilAdapter extends  RecyclerView.Adapter<ProfilAdapter.Holder>  {
    ArrayList<ProfilInspirant> profils = new ArrayList<ProfilInspirant>();

    public ProfilAdapter(ArrayList<ProfilInspirant> profils, Context context){
        if(profils==null){
            this.profils = new ArrayList<ProfilInspirant>();
        }
        else{
            this.profils = profils;
        }
    }


    public static class Holder extends RecyclerView.ViewHolder  {

        ImageView image;
        TextView  nom;
        TextView  description;

        public Holder(View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.image_profil);
            nom = (TextView)itemView.findViewById(R.id.nom);
            description = (TextView)itemView.findViewById(R.id.description);
        }


        public void bind(ProfilInspirant currentProfil){
            Glide.with(image.getContext()).load(currentProfil.getImage()).into(image);
            nom.setText(currentProfil.getNom());
            description.setText(currentProfil.getDescription());

        }

    }

    @Override
    public ProfilAdapter.Holder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profil, parent, false);
        return new ProfilAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(ProfilAdapter.Holder holder, final int i) {
        ProfilInspirant currentProfil = profils.get(i);
        holder.bind(currentProfil);




    }

    @Override
    public long getItemId(int position) {
        return profils.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return profils.size();
    }
}
