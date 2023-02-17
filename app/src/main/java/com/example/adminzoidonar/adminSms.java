package com.example.adminzoidonar;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class adminSms extends Fragment {

    TextInputEditText eventDate, eventTime, eventAddress;
    Button btnSend;


    DatabaseReference phoneNumbersFromDB = FirebaseDatabase.getInstance().getReference().child("users");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_admin_sms, container, false);

        eventDate = view.findViewById(R.id.eventDate);
        eventTime = view.findViewById(R.id.eventTime);
        eventAddress = view.findViewById(R.id.eventAddress);
        btnSend = view.findViewById(R.id.btnSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.SEND_SMS)
                        == PackageManager.PERMISSION_GRANTED) {

                    sendMessage();
                } else {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.SEND_SMS},
                            100);
                }
            }
        });
        return view;
    }

    public void sendMessage() {
        String eventdate = eventDate.getText().toString().trim();
        String eventtime = eventTime.getText().toString().toUpperCase().trim();
        String eventaddress = eventAddress.getText().toString().trim();

        String txtsms = "Hello JPCeans!" +
                "\n\nDonating Blood is an act of solidarity. " +
                "Join the effort on " + eventdate + ", " + eventtime + " at " + eventaddress.toUpperCase() + "\n\n" +
                "Follow SJP2CD Clinic on Facebook for more info";

        if (eventdate.isEmpty() || eventtime.isEmpty() || eventaddress.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill all fields.", Toast.LENGTH_SHORT).show();
        } else {
            phoneNumbersFromDB.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot phoneN : snapshot.getChildren()) {
                        String p = "0" + phoneN.child("phoneNumber").getValue(String.class);

                        SmsManager smsM = SmsManager.getDefault();
                        smsM.sendTextMessage(p, null, txtsms, null, null);

                        //Toast.makeText(getActivity(), p, Toast.LENGTH_SHORT).show();

                    }

                    Toast.makeText(getActivity(), "SMS Successful.\nSending may take a minute", Toast.LENGTH_LONG).show();
                    System.exit(0);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


    }


}