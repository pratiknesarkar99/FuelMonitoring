package com.example.fuelmonitoring.admin.fragments.viewusers;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fuelmonitoring.R;
import com.example.fuelmonitoring.admin.fragments.wrapperclasses.ViewUsers;

import java.util.ArrayList;

public class ViewUsersAdapter extends RecyclerView.Adapter<ViewUsersAdapter.MyViewHolder> {

    Context context;
    ArrayList<ViewUsers> usersList;

    public ViewUsersAdapter(Context context, ArrayList<ViewUsers> usersList) {
        this.context = context;
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public ViewUsersAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.view_users_data_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final String uid = usersList.get(position).getUid();

        holder.fnameTv.setText(usersList.get(position).getFname());
        holder.lnameTv.setText(usersList.get(position).getLname());
        holder.mailTv.setText(usersList.get(position).getMail());

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MoreUserDetails.class);
                intent.putExtra("uid", uid);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    class  MyViewHolder extends  RecyclerView.ViewHolder{

        TextView fnameTv, lnameTv, mailTv;
        Button btn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            fnameTv = (TextView) itemView.findViewById(R.id.fnameTV);
            lnameTv = (TextView) itemView.findViewById(R.id.lnameTV);
            mailTv = (TextView) itemView.findViewById(R.id.mailTV);
            btn = (Button) itemView.findViewById(R.id.viewMoreBtn);
        }
    }
}
