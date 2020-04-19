package com.example.fuelmonitoring.user.fragments.dailyusage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuelmonitoring.R;
import com.example.fuelmonitoring.user.fragments.wrapperclasses.DailyUsageDataFetcher;

import java.util.ArrayList;

public class DailyUsageAdapter extends RecyclerView.Adapter<DailyUsageAdapter.MyViewHolder> {

    Context context;
    ArrayList<DailyUsageDataFetcher> cardsArrayList;

    public DailyUsageAdapter(Context c, ArrayList<DailyUsageDataFetcher> w){
        context = c;
        cardsArrayList = w;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.daily_usage_data_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.dateFilled.setText(cardsArrayList.get(position).getDate()+"");
        holder.timeFilled.setText(cardsArrayList.get(position).getTime()+" IST");
        holder.amtFilled.setText(cardsArrayList.get(position).getAmt()+" L");
        holder.priceFilled.setText(cardsArrayList.get(position).getPrice()+" Rs/-");
    }

    @Override
    public int getItemCount() {
        return cardsArrayList.size();
    }

    class  MyViewHolder extends  RecyclerView.ViewHolder{

        TextView amtFilled, priceFilled, dateFilled, timeFilled;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            dateFilled = (TextView) itemView.findViewById(R.id.date_filled);
            timeFilled = (TextView) itemView.findViewById(R.id.time_filled);
            amtFilled = (TextView) itemView.findViewById(R.id.amount_filled);
            priceFilled = (TextView) itemView.findViewById(R.id.price_filled);
        }
    }
}
