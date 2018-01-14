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
import bj.zemidjan.dio.models.Cours;

import java.util.ArrayList;

/**
 * Created by Oulfath on 19/09/2017.
 */

public class CoursAdapter extends RecyclerView.Adapter<CoursAdapter.Holder> {
    ArrayList<Cours> cours = new ArrayList<Cours>();
    public CoursAdapter(ArrayList<Cours> cours, Context context){
        if(cours==null){
            this.cours = new ArrayList<Cours>();
        }
        else{
            this.cours = cours;
        }
    }


    public static class Holder extends RecyclerView.ViewHolder  {

        ImageView image;
        ImageView certification;
        ImageView telechargeable;
        TextView  titre;
        TextView  gratuit;

        public Holder(View itemView) {
            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.image_cours);
            telechargeable = (ImageView)itemView.findViewById(R.id.telechargeable);
            certification = (ImageView)itemView.findViewById(R.id.certification);
            titre = (TextView)itemView.findViewById(R.id.titre_cours);
            gratuit = (TextView)itemView.findViewById(R.id.gratuit);
        }


        public void bind(Cours currentCours){
            Glide.with(image.getContext()).load(currentCours.getImage()).into(image);
            titre.setText(currentCours.getTitre());
            if(currentCours.isGratuit()){

            }else{
                gratuit.setText("Payant");
            }
            if(currentCours.isCertification()){

            }else{
                certification.setBackgroundResource(R.color.gris_4);
            }
            if(currentCours.isTelechargeable()){

            }else{
                telechargeable.setBackgroundResource(R.color.gris_3);
            }

        }


    }

    @Override
    public CoursAdapter.Holder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cours, parent, false);
        return new CoursAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(CoursAdapter.Holder holder, final int i) {
        Cours currentCours = cours.get(i);
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
