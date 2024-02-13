package com.example.omerapp;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
public class HazardAdapter extends RecyclerView.Adapter<HazardAdapter.UserViewHolder>{
    private ArrayList<EnvHazard> EnvHazards;
    private View.OnClickListener mOnItemClickListener;

    public HazardAdapter(ArrayList<EnvHazard> EnvHazards) {
        this.EnvHazards = EnvHazards;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View userView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_user_item,parent,false);
        return new UserViewHolder(userView);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = users.get(position);
        holder.tvFirstName.setText(user.getFirstName());
        holder.tvLastName.setText(user.getLastName());
        holder.tvYear.setText(String.valueOf(user.getYearOfBirth()));
        holder.ivUser.setImageResource(holder.tvFirstName.getResources().getIdentifier(user.getImg(),"drawable",holder.tvFirstName.getContext().getOpPackageName()));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    //Assign itemClickListener to your local View.OnClickListener variable
    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView tvFirstName;
        public TextView tvLastName;
        public TextView tvYear;
        public ImageView ivUser;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFirstName = (TextView) itemView.findViewById(R.id.tvFirstName);
            tvLastName = (TextView) itemView.findViewById(R.id.tvLastName);
            tvYear = (TextView) itemView.findViewById(R.id.tvYear);
            ivUser = (ImageView) itemView.findViewById(R.id.ivUser);


            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }


    }
}
