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
import bj.zemidjan.dio.models.SuivreCours;

import java.util.ArrayList;

/**
 * Created by Oulfath on 17/09/2017.
 */
public class SuivreCoursAdapter extends RecyclerView.Adapter<SuivreCoursAdapter.Holder>{
    ArrayList<SuivreCours> cours = new ArrayList<SuivreCours>();

    public SuivreCoursAdapter(ArrayList<SuivreCours> cours, Context context){
        if(cours==null){
            this.cours = new ArrayList<SuivreCours>();
        }
        else{
            this.cours = cours;
        }
    }


    public static class Holder extends RecyclerView.ViewHolder  {

        TextView titre;
        TextView suivi;
        ImageView image;

        public Holder(View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.image_cours);
            titre = (TextView)itemView.findViewById(R.id.titre_cours);
            suivi = (TextView)itemView.findViewById(R.id.suivi);
        }


        public void bind(SuivreCours currentCours){
            Glide.with(image.getContext()).load(currentCours.getCours().getImage()).into(image);
            titre.setText(currentCours.getCours().getTitre());
            suivi.setText("Commencé le "+ currentCours.getDateCreated());

        }


    }

    @Override
    public SuivreCoursAdapter.Holder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_suivrecours, parent, false);
        return new SuivreCoursAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int i) {
        SuivreCours currentCours = cours.get(i);
        holder.bind(currentCours);




    }

    @Override
    public long getItemId(int position) {
        return cours.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return cours.size();
    }
}
