package com.example.fuelmonitoring.user.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fuelmonitoring.R;
import com.example.fuelmonitoring.user.fragments.wrapperclasses.DailyUsageDataFetcher;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class weekly_usage extends Fragment {

    private FirebaseAuth firebaseAuth;
    private TextView totalAmtTV,  totalPriceTV;
    private LineChart graphViewAmt,  graphViewPrice;
    private static float amt =0f, price= 0f;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weekly_usage, container, false);

        graphViewAmt = view.findViewById(R.id.weeklyUsageGraphAmt);
        graphViewPrice = view.findViewById(R.id.weeklyUsageGraphPrice);

        totalAmtTV = view.findViewById(R.id.weeklytotalamtTv);
        totalPriceTV = view.findViewById(R.id.weeklytotalpriceTv);

        graphViewAmt.setDragEnabled(true);
        graphViewAmt.setScaleEnabled(true);

        graphViewPrice.setDragEnabled(true);
        graphViewPrice.setScaleEnabled(true);

        getAmtGraph();
        getPriceGraph();

        return view;
    }

    public void getAmtGraph(){
        FirebaseDatabase firebaseDatabase;
        DatabaseReference databaseReference;

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase. getReference().child("UsageDetails").child(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Calendar calender = Calendar.getInstance();
                Calendar calender1 = Calendar.getInstance();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                String td = dateFormat.format(new Date());
                Date todaysDt = new Date(Integer.parseInt(td.substring(0, 4)),
                        Integer.parseInt(td.substring(4, 6))-1,
                        Integer.parseInt(td.substring(6, 8)));
                calender1.setTime(todaysDt);

                graphViewAmt.clear();
                amt = 0f;
                ArrayList<Entry> dataPointsAmt = new ArrayList<>();
                int index;

                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    String d = dataSnapshot1.getKey();
                    Date dt = new Date(Integer.parseInt(d.substring(0, 4)),
                            Integer.parseInt(d.substring(4, 6))-1,
                            Integer.parseInt(d.substring(6, 8)));

                    //Log.d("index dt", dt+"");
                    calender.setTime(dt);

                    if(calender1.get(Calendar.WEEK_OF_YEAR) == calender.get(Calendar.WEEK_OF_YEAR)) {
                        index = calender.get(Calendar.DAY_OF_WEEK);
                        //Log.d("index", index + "");

                        DailyUsageDataFetcher dailyUsageDataFetcher = dataSnapshot1.getValue(DailyUsageDataFetcher.class);

                        dataPointsAmt.add(new Entry(index, Float.parseFloat(dailyUsageDataFetcher.getAmt())));
                        amt += Float.parseFloat(dailyUsageDataFetcher.getAmt());

                        //Log.d("datasnap", dataSnapshot1.toString());
                    }
                }

                LineDataSet lineDataSetAmt = new LineDataSet(dataPointsAmt, "Petrol Amount");
                lineDataSetAmt.setFillAlpha(110);
                lineDataSetAmt.setColor(Color.RED);
                lineDataSetAmt.setLineWidth(3f);
                lineDataSetAmt.setCircleColor(Color.BLUE);
                lineDataSetAmt.setCircleRadius(5f);
                lineDataSetAmt.setValueTextSize(15f);
                lineDataSetAmt.setValueTextColor(Color.RED);

                ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
                lineDataSets.add(lineDataSetAmt);

                LineData lineDataAmt = new LineData(lineDataSets);

                graphViewAmt.setData(lineDataAmt);
                totalAmtTV.setText(amt+" L");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public  void getPriceGraph(){
        FirebaseDatabase firebaseDatabase;
        DatabaseReference databaseReference;

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase. getReference().child("UsageDetails").child(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Calendar calender = Calendar.getInstance();
                Calendar calender1 = Calendar.getInstance();

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                String td = dateFormat.format(new Date());
                Date todaysDt = new Date(Integer.parseInt(td.substring(0, 4)),
                        Integer.parseInt(td.substring(4, 6))-1,
                        Integer.parseInt(td.substring(6, 8)));
                calender1.setTime(todaysDt);

                graphViewPrice.clear();
                price = 0f;
                ArrayList<Entry> dataPointsPrice = new ArrayList<>();
                int index;

                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    String d = dataSnapshot1.getKey();
                    Date dt = new Date(Integer.parseInt(d.substring(0, 4)),
                            Integer.parseInt(d.substring(4, 6))-1,
                            Integer.parseInt(d.substring(6, 8)));

                    //Log.d("index dt", dt+"");
                    calender.setTime(dt);

                    if(calender1.get(Calendar.WEEK_OF_YEAR) == calender.get(Calendar.WEEK_OF_YEAR)) {
                        index = calender.get(Calendar.DAY_OF_WEEK);
                        //Log.d("index", index + "");

                        try {
                            DailyUsageDataFetcher dailyUsageDataFetcher = dataSnapshot1.getValue(DailyUsageDataFetcher.class);

                            price += Float.parseFloat(dailyUsageDataFetcher.getPrice());

                            float x = Float.parseFloat(dailyUsageDataFetcher.getPrice());
                            dataPointsPrice.add(new Entry(index, x));

                            //Log.d("datasnap", dataSnapshot1.toString());
                        } catch (Exception e){}
                    }
                }

                LineDataSet lineDataSetPrice = new LineDataSet(dataPointsPrice, "Petrol Price");
                lineDataSetPrice.setFillAlpha(110);
                lineDataSetPrice.setColor(Color.BLUE);
                lineDataSetPrice.setLineWidth(3f);
                lineDataSetPrice.setCircleColor(Color.RED);
                lineDataSetPrice.setCircleRadius(5f);
                lineDataSetPrice.setValueTextSize(15f);
                lineDataSetPrice.setValueTextColor(Color.BLUE);

                ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
                lineDataSets.add(lineDataSetPrice);

                LineData lineDataAmt = new LineData(lineDataSets);

                graphViewPrice.setData(lineDataAmt);
                totalPriceTV.setText(price+" Rs");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
