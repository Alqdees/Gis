package com.example.gis_2.adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gis_2.R;
import com.example.gis_2.UpdateGisActivity;
import com.example.gis_2.db.DBHelper;
import com.example.gis_2.modle.Gis;

import java.util.ArrayList;

public class GisAdapter extends RecyclerView.Adapter<GisAdapter.gisVH>{


  static ArrayList<Gis> gis;
  Context context;




    public GisAdapter(ArrayList<Gis> gis, Context context) {
        this.gis = gis;
        this.context = context;

    }


    @NonNull
    @Override
    public gisVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.raw_gis, parent, false);
        return new gisVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull gisVH holder, int position) {


        final Gis g = gis.get(position);
        holder.tvname.setText(g.getFeedername() + " _ " + g.getTransID());
        holder.tvfeedername.setText(g.getFeedername());
        holder.tvsubname.setText(g.getSubstationname());
        holder.tvtransid.setText(g.getTransID());
        holder.tvgps.setText(g.getGps());
        holder.tvtranskva.setText(g.getCapacity());
        holder.tvtransclass.setText(g.getClasses());
        holder.tvtranscondition.setText(g.getCondition());
        holder.tvtransmanufacture.setText(g.getManufacture());
        holder.tvserialnumber.setText(g.getSerialnumber());
        holder.tvremark.setText(g.getRemark());

        holder.cardupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, g.getFeedername() + " Will be Updated " , Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(context, UpdateGisActivity.class);
                intent.putExtra("GIS", g);
                context.startActivity(intent);
            }
        });

        holder.carddelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, g.getFeedername() + " Will be Deleted " , Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Confirmation!!!!");
                builder.setMessage("Are You Sure to Delete " + g.getFeedername() + "?");
                builder.setIcon(android.R.drawable.ic_menu_delete);
                builder.setCancelable(false);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        DBHelper dbHelper = new DBHelper(context);

                        int result = dbHelper.deletegis(g.getId());

                        if (result > 0){
                            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
                            gis.remove(g);
                            notifyDataSetChanged();
                        }else{
                            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                builder.setNegativeButton("NO", null);
                builder.show();

            }
        });




    }

    @Override
    public int getItemCount() {
        return gis.size();
    }

    class gisVH extends RecyclerView.ViewHolder {

        TextView tvname, tvfeedername, tvsubname, tvtransid, tvgps, tvtranskva, tvtransclass, tvtranscondition, tvtransmanufacture, tvserialnumber, tvremark;
        CardView cardupdate, carddelete;


        public gisVH(@NonNull View v) {
            super(v);

            tvname = v.findViewById(R.id.tvname);
            tvfeedername = v.findViewById(R.id.tvfeedername);
            tvsubname = v.findViewById(R.id.tvsubname);
            tvtransid = v.findViewById(R.id.tvtransid);
            tvgps = v.findViewById(R.id.tvgps);
            tvtranskva = v.findViewById(R.id.tvtranskva);
            tvtransclass = v.findViewById(R.id.tvtransclass);
            tvtranscondition = v.findViewById(R.id.tvtranscondition);
            tvtransmanufacture = v.findViewById(R.id.tvtransmanufacture);
            tvserialnumber = v.findViewById(R.id.tvserialnumber);
            tvremark = v.findViewById(R.id.tvremark);

            cardupdate = v.findViewById(R.id.cardupdate);
            carddelete = v.findViewById(R.id.carddelete);


        }
    }
}
