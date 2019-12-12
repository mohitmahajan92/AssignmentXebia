package com.example.assignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private ArrayList<Model> arrayList = null;
    private Context context;

    public Adapter(Context context, ArrayList<Model> hlm) {
        this.arrayList = hlm;
        this.context = context;

    }

    @Override
    public Adapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_adapter, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {

        viewHolder.txtTitle.setText(arrayList.get(i).strTitle);
        viewHolder.txtAbstract.setText(arrayList.get(i).strAbstract);
        viewHolder.txtDate.setText(arrayList.get(i).strDate);
        viewHolder.txtSource.setText(arrayList.get(i).strSource);


        for (int k = 0; k < arrayList.size(); k++) {
            for (int j = 0; j < arrayList.get(k).arrayListMedia.size(); j++) {
                if (arrayList.get(k).arrayListMedia.get(j).mediaMetaDataModels.size() > 0) {
                    for (int l = 0; l < arrayList.get(k).arrayListMedia.get(j).mediaMetaDataModels.size(); l++) {


                        try {

                            if (arrayList.get(k).arrayListMedia.get(j).mediaMetaDataModels.get(j).strImage.equals("") || arrayList.get(k).arrayListMedia.get(j).mediaMetaDataModels.get(l).strImage == null)
                                Picasso.get().load(arrayList.get(k).arrayListMedia.get(j).mediaMetaDataModels.get(k).strImage).into(viewHolder.civ_User);
//                           else
//                               viewHolder.civ_User.setVisibility(View.GONE);

                            Picasso.get().load(arrayList.get(k).arrayListMedia.get(j).mediaMetaDataModels.get(l).strImage).into(viewHolder.civ_User);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }


                }
            }
        }




    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtTitle, txtAbstract, txtDate, txtSource;
        private CircularImageView civ_User;

        public ViewHolder(View view) {
            super(view);
            txtTitle = view.findViewById(R.id.txtTitle);
            txtAbstract = view.findViewById(R.id.txtAbstract);
            txtDate = view.findViewById(R.id.txtDate);
            txtSource = view.findViewById(R.id.txtSource);
            civ_User = view.findViewById(R.id.civ_User);

        }
    }


}

