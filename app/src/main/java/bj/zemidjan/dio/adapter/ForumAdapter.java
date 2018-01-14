package bj.zemidjan.dio.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import bj.zemidjan.dio.R;
import bj.zemidjan.dio.models.Forum;

import java.util.ArrayList;

/**
 * Created by Oulfath on 06/10/2017.
 */

public class ForumAdapter extends RecyclerView.Adapter<ForumAdapter.Holder>{
    ArrayList<Forum> forums = new ArrayList<Forum>();
    public ForumAdapter(ArrayList<Forum> forums, Context context){
        if(forums==null){
            this.forums = new ArrayList<Forum>();
        }
        else{
            this.forums = forums;
        }
    }


    public static class Holder extends RecyclerView.ViewHolder  {
        TextView textemessage;
        public Holder(View itemView) {
            super(itemView);
            textemessage = (TextView)itemView.findViewById(R.id.message);
        }


        public void bind(Forum currentForum){
            textemessage.setText(currentForum.getTexte());
        }


    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int position) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_forum, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, final int i) {
        Forum currentForum = forums.get(i);
        holder.bind(currentForum);




    }

    @Override
    public long getItemId(int position) {
        return forums.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return forums.size();
    }
}
