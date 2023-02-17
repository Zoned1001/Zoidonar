package com.example.adminzoidonar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ArchiveAdapter extends RecyclerView.Adapter<MyViewHolder> {
    Context c;
    List<String> mdataDonor;
    String bDate;

    public ArchiveAdapter(Context c, List<String> mdataDonor, String bDate) {
        this.c = c;
        this.mdataDonor = mdataDonor;
        this.bDate = bDate;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewgroup, int viewType) {
        View v = LayoutInflater.from(c).inflate(R.layout.itemdonor, viewgroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int i) {

        holder.itemDonorID.setText(mdataDonor.get(i));


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("archive").child(bDate);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String fname = (String) snapshot.child(mdataDonor.get(i)).child("firstName").getValue();
                String lname = (String) snapshot.child(mdataDonor.get(i)).child("lastName").getValue();
                String bloodType = (String) snapshot.child(mdataDonor.get(i)).child("Bloodtype").getValue();
                String bloodVolume = (String) snapshot.child(mdataDonor.get(i)).child("Volume").getValue();

                String fullName = fname + " " + lname;


                holder.itemDonorName.setText(fullName.toUpperCase());
                holder.itemBloodType.setText(bloodType);
                holder.itemBloodVolume.setText(bloodVolume);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return mdataDonor.size();
    }
}
