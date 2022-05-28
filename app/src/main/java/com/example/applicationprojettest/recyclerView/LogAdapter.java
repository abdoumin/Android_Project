package com.example.applicationprojettest.recyclerView;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.applicationprojettest.MeetingActivity;
import com.example.applicationprojettest.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class LogAdapter extends RecyclerView.Adapter<LogAdapter.ViewHolder> {

    ArrayList<CoordonnesData> myCoordonnes;
    Context context;

    public LogAdapter(ArrayList<CoordonnesData> myCoordonnes, recyclerActivity activity) {
        /*this.myCoordonnes = myCoordonnes;*/
        this.myCoordonnes = myCoordonnes;
        this.context = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_list,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final CoordonnesData mCoordonnes = myCoordonnes.get(position);
        holder.longitude.setText("Longitude : " + mCoordonnes.getLongitude());
        holder.lattitude.setText("Lattitude :" + mCoordonnes.getLattitude());
        holder.date.setText("Date : " +mCoordonnes.getDate());
        holder.btn_maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MeetingActivity.class);
                intent.putExtra("DATE", mCoordonnes.getDate());
                intent.putExtra("LONGITUDE", mCoordonnes.getLongitude());
                intent.putExtra("LATTITUDE", mCoordonnes.getLattitude());
                intent.putExtra("HEALTHSTATUS", mCoordonnes.getHealthStatus());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return myCoordonnes.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView lattitude;
        TextView longitude;
        TextView date;
        Button btn_maps;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            longitude = itemView.findViewById(R.id.longitude);
            lattitude = itemView.findViewById(R.id.lattitude);
            date = itemView.findViewById(R.id.textdate);
            btn_maps = itemView.findViewById(R.id.btn_maps);



        }

    }

}
