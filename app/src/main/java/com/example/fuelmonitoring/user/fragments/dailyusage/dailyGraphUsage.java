package com.example.fuelmonitoring.user.fragments.dailyusage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class dailyGraphUsage extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private TextView totalAmtTV,  totalPriceTV;
    private LineChart graphViewAmt,  graphViewPrice;
    private static float amt =0f, price= 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_graph_usage);

        graphViewAmt = findViewById(R.id.dailyUsageGraphAmt);
        graphViewPrice = findViewById(R.id.dailyUsageGraphPrice);

        totalAmtTV = findViewById(R.id.dailytotalamtTv);
        totalPriceTV = findViewById(R.id.dailytotalpriceTv);

        graphViewAmt.setDragEnabled(true);
        graphViewAmt.setScaleEnabled(true);

        graphViewPrice.setDragEnabled(true);
        graphViewPrice.setScaleEnabled(true);

        getAmtGraph();
        getPriceGraph();

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

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                String todaysDt = dateFormat.format(new Date());

                graphViewAmt.clear();
                amt = 0f;
                ArrayList<Entry> dataPointsAmt = new ArrayList<>();
                float index;

                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){

                    String d = dataSnapshot1.getKey();
                    String dt = d.substring(0, 8);

                    int hour = Integer.parseInt(d.substring(8,   10));
                    int min = Integer.parseInt(d.substring(10,   12));
                    int sec = Integer.parseInt(d.substring(12,   14));

                    float hoursDecimal = (float)  (hour + (min *  0.0167) + (sec *  0.0003));
                    DecimalFormat df = new DecimalFormat("#.##");

                    if(dt.equals(todaysDt)) {
                        index =  Float.parseFloat(df.format(hoursDecimal));

                        //Log.d("index time calc:   ", index+"");

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

    public  void getPriceGraph() {
        FirebaseDatabase firebaseDatabase;
        DatabaseReference databaseReference;

        firebaseAuth = FirebaseAuth.getInstance();

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference().child("UsageDetails").child(firebaseAuth.getUid());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                String todaysDt = dateFormat.format(new Date());

                graphViewPrice.clear();
                price = 0f;
                ArrayList<Entry> dataPointsPrice = new ArrayList<>();
                float index;

                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    String d = dataSnapshot1.getKey();
                    String dt = d.substring(0, 8);

                    int hour = Integer.parseInt(d.substring(8, 10));
                    int min = Integer.parseInt(d.substring(10, 12));
                    int sec = Integer.parseInt(d.substring(12, 14));

                    float hoursDecimal = (float) (hour + (min * 0.0167) + (sec * 0.0003));
                    DecimalFormat df = new DecimalFormat("#.##");

                    if (dt.equals(todaysDt)) {
                        index = Float.parseFloat(df.format(hoursDecimal));

                        //Log.d("index time calc:   ", index+"");

                        DailyUsageDataFetcher dailyUsageDataFetcher = dataSnapshot1.getValue(DailyUsageDataFetcher.class);

                        dataPointsPrice.add(new Entry(index, Float.parseFloat(dailyUsageDataFetcher.getPrice())));
                        price += Float.parseFloat(dailyUsageDataFetcher.getPrice());

                        //Log.d("datasnap", dataSnapshot1.toString());
                    }
                }

                LineDataSet lineDataSetPrice = new LineDataSet(dataPointsPrice, "Petrol Cost");
                lineDataSetPrice.setFillAlpha(110);
                lineDataSetPrice.setColor(Color.RED);
                lineDataSetPrice.setLineWidth(3f);
                lineDataSetPrice.setCircleColor(Color.BLUE);
                lineDataSetPrice.setCircleRadius(5f);
                lineDataSetPrice.setValueTextSize(15f);
                lineDataSetPrice.setValueTextColor(Color.RED);

                ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
                lineDataSets.add(lineDataSetPrice);

                LineData lineDataPrice = new LineData(lineDataSets);

                graphViewPrice.setData(lineDataPrice);
                totalPriceTV.setText(price + " Rs");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
