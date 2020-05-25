package com.example.fuelmonitoring.admin.fragments.viewuserfeedback;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuelmonitoring.R;
import com.example.fuelmonitoring.admin.fragments.viewusers.MoreUserDetails;
import com.example.fuelmonitoring.admin.fragments.wrapperclasses.ViewUserFeedback;
import com.example.fuelmonitoring.admin.fragments.wrapperclasses.ViewUsers;

import java.util.ArrayList;

public class ViewUserFeedbackAdapter extends RecyclerView.Adapter<ViewUserFeedbackAdapter.MyViewHolder> {

    Context context;
    ArrayList<ViewUserFeedback> usersFeedbackList;

    public ViewUserFeedbackAdapter(Context context, ArrayList<ViewUserFeedback> usersFeedbackList) {
        this.context = context;
        this.usersFeedbackList = usersFeedbackList;
    }

    @NonNull
    @Override
    public ViewUserFeedbackAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.view_user_feedback_data_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final String uid = usersFeedbackList.get(position).getUid();

        holder.dateSubmittedTV.setText(usersFeedbackList.get(position).getDate());
        //Log.d("feedback date: ", usersFeedbackList.get(position).getDate());
        holder.timeSubmittedTV.setText(usersFeedbackList.get(position).getTime());
        //Log.d("feedback time: ", usersFeedbackList.get(position).getTime());
        holder.mailSubmittedTV.setText(usersFeedbackList.get(position).getMail());

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MoreDetailedFeedback.class);
                intent.putExtra("uid", uid);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersFeedbackList.size();
    }

    class  MyViewHolder extends  RecyclerView.ViewHolder{

        TextView dateSubmittedTV, timeSubmittedTV, mailSubmittedTV;
        Button btn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            dateSubmittedTV = (TextView) itemView.findViewById(R.id.dateSubmittedTV);
            timeSubmittedTV = (TextView) itemView.findViewById(R.id.timeSubmittedTV);
            mailSubmittedTV = (TextView) itemView.findViewById(R.id.mailSubmittedTV);
            btn = (Button) itemView.findViewById(R.id.viewMoreDetailsBtn);
        }
    }
}
