package com.example.fuelmonitoring.admin.fragments.wrapperclasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuelmonitoring.R;

import java.util.ArrayList;

public class ContactEmailsAdapter extends RecyclerView.Adapter<ContactEmailsAdapter.MyViewHolder> {
    Context context;
    ArrayList<ContactEmails> contactEmailsList;

    public ContactEmailsAdapter(Context context, ArrayList<ContactEmails> contactEmailsList) {
        this.context = context;
        this.contactEmailsList = contactEmailsList;
    }

    @NonNull
    @Override
    public ContactEmailsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.contact_emails_data_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactEmailsAdapter.MyViewHolder holder, int position) {
        holder.dateTv.setText(contactEmailsList.get(position).getDate());
        holder.timeTv.setText(contactEmailsList.get(position).getTime());
        holder.mailTv.setText(contactEmailsList.get(position).getMail());
    }

    @Override
    public int getItemCount() {
        return contactEmailsList.size();
    }

    class  MyViewHolder extends  RecyclerView.ViewHolder{

        TextView dateTv, timeTv, mailTv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            dateTv = (TextView) itemView.findViewById(R.id.dateTV);
            timeTv = (TextView) itemView.findViewById(R.id.timeTV);
            mailTv = (TextView) itemView.findViewById(R.id.mailTv);
        }
    }
}
