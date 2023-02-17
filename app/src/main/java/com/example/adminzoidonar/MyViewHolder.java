package com.example.adminzoidonar;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {

    TextView itemEventDate,itemEventName,itemEventCount;
    TextView itemDonorID,itemDonorName,itemBloodType,itemBloodVolume;


    public MyViewHolder(@NonNull View item) {
        super(item);

        itemEventDate = item.findViewById(R.id.itemEventDate);
        itemEventName = item.findViewById(R.id.itemEventName);
        itemEventCount = item.findViewById(R.id.itemEventCount);


        itemDonorID = item.findViewById(R.id.itemDonorID);
        itemDonorName = item.findViewById(R.id.itemDonorName);
        itemBloodType = item.findViewById(R.id.itemBloodType);
        itemBloodVolume = item.findViewById(R.id.itemBloodVolume);

    }
}
