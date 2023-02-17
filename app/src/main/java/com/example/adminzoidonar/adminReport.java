package com.example.adminzoidonar;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.util.ArrayList;

public class adminReport extends AppCompatActivity {

    private static final int STORAGE_CODE = 1000;

    ImageButton btnHome, btnNext;
    Spinner eventPickSpinner;
    TextView displayTitle, sample;
    Button btnExport;

    TextView totalDonorCount, totalStudents, totalEmployees, totalGuests;
    TextView btAbp, btAbn, btOp, btOn, btBp, btBn, btAp, btAn;
    TextView totalAB, totalA, totalB, totalO;
    TextView totalABVolume, totalAVolume, totalBVolume, totalOVolume;

    DatabaseReference eventsFromDB = FirebaseDatabase.getInstance().getReference().child("Events");
    ArrayList<String> spinnerList = new ArrayList<>();
    ArrayAdapter<String> adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_report);
        btnHome = (ImageButton) findViewById(R.id.backButton);
        btnNext = (ImageButton) findViewById(R.id.btnNext);
        eventPickSpinner = (Spinner) findViewById(R.id.eventPickSpinner);
        btnExport = (Button) findViewById(R.id.btnExport);
        displayTitle = (TextView) findViewById(R.id.displayTitle);
        sample = (TextView) findViewById(R.id.sample);

        totalDonorCount = (TextView) findViewById(R.id.totalDonorCount);
        totalStudents = (TextView) findViewById(R.id.totalStudents);
        totalEmployees = (TextView) findViewById(R.id.totalEmployees);
        totalGuests = (TextView) findViewById(R.id.totalGuests);
        totalAB = (TextView) findViewById(R.id.totalAB);
        totalA = (TextView) findViewById(R.id.totalA);
        totalB = (TextView) findViewById(R.id.totalB);
        totalO = (TextView) findViewById(R.id.totalO);
        totalABVolume = (TextView) findViewById(R.id.totalABVolume);
        totalAVolume = (TextView) findViewById(R.id.totalAVolume);
        totalBVolume = (TextView) findViewById(R.id.totalBVolume);
        totalOVolume = (TextView) findViewById(R.id.totalOVolume);

        btAbp = (TextView) findViewById(R.id.btAbp);
        btAbn = (TextView) findViewById(R.id.btAbn);
        btOp = (TextView) findViewById(R.id.btOp);
        btOn = (TextView) findViewById(R.id.btOn);
        btBp = (TextView) findViewById(R.id.btBp);
        btBn = (TextView) findViewById(R.id.btBn);
        btAp = (TextView) findViewById(R.id.btAp);
        btAn = (TextView) findViewById(R.id.btAn);


        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(adminReport.this, adminMenuDashboard.class));
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCounter();
                startActivity(new Intent(adminReport.this, adminReport2.class));
            }
        });

        eventPickSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                myCounter();
                String selectedItem = eventPickSpinner.getSelectedItem().toString();
                displaySpinnerContent(selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedItem = eventPickSpinner.getSelectedItem().toString();
                displaySpinnerContent(selectedItem);
                String titleOfEvent = displayTitle.getText().toString();
                String ttlDonors = totalDonorCount.getText().toString();
                String ttlStudents = totalStudents.getText().toString();
                String ttlEmployees = totalEmployees.getText().toString();
                String ttlGuests = totalGuests.getText().toString();
                String abp = btAbp.getText().toString();
                String abn = btAbn.getText().toString();
                String ap = btAp.getText().toString();
                String an = btAn.getText().toString();
                String bp = btBp.getText().toString();
                String bn = btBn.getText().toString();
                String op = btOp.getText().toString();
                String on = btOn.getText().toString();
                String ttlAB = totalAB.getText().toString();
                String ttlA = totalA.getText().toString();
                String ttlB = totalB.getText().toString();
                String ttlO = totalO.getText().toString();
                String ttlABVolume = totalABVolume.getText().toString();
                String ttlAVolume = totalAVolume.getText().toString();
                String ttlBVolume = totalBVolume.getText().toString();
                String ttlOVolume = totalOVolume.getText().toString();


                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                            PackageManager.PERMISSION_DENIED) {
                        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
                        requestPermissions(permissions, STORAGE_CODE);
                    } else {
                        exportData(selectedItem, titleOfEvent, ttlDonors, ttlStudents, ttlEmployees, ttlGuests,
                                abp, abn, ap, an, bp, bn, op, on,
                                ttlAB, ttlA, ttlB, ttlO,
                                ttlABVolume, ttlAVolume, ttlBVolume, ttlOVolume);
                    }
                } else {
                    exportData(selectedItem, titleOfEvent, ttlDonors, ttlStudents, ttlEmployees, ttlGuests,
                            abp, abn, ap, an, bp, bn, op, on,
                            ttlAB, ttlA, ttlB, ttlO,
                            ttlABVolume, ttlAVolume, ttlBVolume, ttlOVolume);
                }
            }
        });

        showEventListSpinner();
    }

    public void myCounter() {
        DatabaseReference totalDonorCount = FirebaseDatabase.getInstance().getReference().child("Report");

        for (String k : spinnerList) {
            eventsFromDB.child(k).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    int students = (int) snapshot.child("STUDENT").getChildrenCount();
                    int employees = (int) snapshot.child("EMPLOYEES").getChildrenCount();
                    int guests = (int) snapshot.child("GUEST").getChildrenCount();

                    totalDonorCount.child(k).child("studentCount").setValue(students);
                    totalDonorCount.child(k).child("employeeCount").setValue(employees);
                    totalDonorCount.child(k).child("guestCount").setValue(guests);

                    int sum = students + employees + guests;
                    totalDonorCount.child(k).child("TOTAL").setValue(sum);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            totalDonorCount.child(k).child("Qualified").child("BloodType").
                    addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            int Abp = (int) snapshot.child("AB+").getChildrenCount();
                            int Abn = (int) snapshot.child("AB-").getChildrenCount();
                            int Op = (int) snapshot.child("O+").getChildrenCount();
                            int On = (int) snapshot.child("O-").getChildrenCount();
                            int Bp = (int) snapshot.child("B+").getChildrenCount();
                            int Bn = (int) snapshot.child("B-").getChildrenCount();
                            int Ap = (int) snapshot.child("A+").getChildrenCount();
                            int An = (int) snapshot.child("A-").getChildrenCount();

                            DatabaseReference newRef = totalDonorCount.child(k).child("eachBloodTypeTotalCount");
                            newRef.child("abPositive").setValue(Abp);
                            newRef.child("abNegative").setValue(Abn);
                            newRef.child("oPositive").setValue(Op);
                            newRef.child("oNegative").setValue(On);
                            newRef.child("bPositive").setValue(Bp);
                            newRef.child("bNegative").setValue(Bn);
                            newRef.child("aPositive").setValue(Ap);
                            newRef.child("aNegative").setValue(An);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

        }
    }

    public void showEventListSpinner() {
        adapter = new ArrayAdapter<String>(adminReport.this, android.R.layout.simple_dropdown_item_1line, spinnerList);

        Query q = eventsFromDB;
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot item : snapshot.getChildren()) {
                    String x = (String) item.getKey();
                    spinnerList.add(x);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        eventPickSpinner.setAdapter(adapter);
    }

    public void displaySpinnerContent(String selectedItem) {
        DatabaseReference events = eventsFromDB.child(selectedItem);

        events.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!snapshot.exists()) {
                    Toast.makeText(adminReport.this, "Error", Toast.LENGTH_SHORT).show();
                } else {
                    String eventTitle = snapshot.child("eventTitle").getValue(String.class);
                    displayTitle.setText(eventTitle);
                    displayDataOfSelectedEvent(selectedItem);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void displayDataOfSelectedEvent(String basedEvent) {
        DatabaseReference donorCountByCat = FirebaseDatabase.getInstance().getReference().child("Events");
        DatabaseReference bloodTypeCount = FirebaseDatabase.getInstance().getReference().child("Report");
        DatabaseReference archive = FirebaseDatabase.getInstance().getReference().child("archive");

        donorCountByCat.child(basedEvent).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int students = (int) snapshot.child("STUDENT").getChildrenCount();
                totalStudents.setText(students + "");

                int employees = (int) snapshot.child("EMPLOYEES").getChildrenCount();
                totalEmployees.setText(employees + "");

                int guests = (int) snapshot.child("GUEST").getChildrenCount();
                totalGuests.setText(guests + "");

                int sum = students + employees + guests;
                totalDonorCount.setText(sum + "");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        bloodTypeCount.child(basedEvent).child("Qualified").child("BloodType")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        int Abp = (int) snapshot.child("AB+").getChildrenCount();
                        int Abn = (int) snapshot.child("AB-").getChildrenCount();
                        int Ap = (int) snapshot.child("A+").getChildrenCount();
                        int An = (int) snapshot.child("A-").getChildrenCount();
                        int Bp = (int) snapshot.child("B+").getChildrenCount();
                        int Bn = (int) snapshot.child("B-").getChildrenCount();
                        int Op = (int) snapshot.child("O+").getChildrenCount();
                        int On = (int) snapshot.child("O-").getChildrenCount();

                        int ttlAB = Abp + Abn;
                        int ttlA = Ap + An;
                        int ttlB = Bp + Bn;
                        int ttlO = Op + On;

                        btAbp.setText(Abp + "");
                        btAbn.setText(Abn + "");
                        btOp.setText(Op + "");
                        btOn.setText(On + "");
                        btBp.setText(Bp + "");
                        btBn.setText(Bn + "");
                        btAp.setText(Ap + "");
                        btAn.setText(An + "");

                        totalAB.setText(ttlAB + "");
                        totalA.setText(ttlA + "");
                        totalB.setText(ttlB + "");
                        totalO.setText(ttlO + "");

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        archive.child(basedEvent).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int totalVolumeForAB = 0, totalVolumeForA = 0, totalVolumeForB = 0, totalVolumeForO = 0;

                for (DataSnapshot keyOfChild : snapshot.getChildren()) {
                    String key = keyOfChild.getKey();
                    String donorBT = snapshot.child(key).child("Bloodtype").getValue(String.class);

                    switch (donorBT) {
                        case "AB+":
                        case "AB-":
                            String VolumeForAB = snapshot.child(key).child("Volume").getValue(String.class);
                            int volumeAB = Integer.parseInt(VolumeForAB);
                            totalVolumeForAB += volumeAB;
                            break;
                        case "A+":
                        case "A-":
                            String VolumeForA = snapshot.child(key).child("Volume").getValue(String.class);
                            int volumeA = Integer.parseInt(VolumeForA);
                            totalVolumeForA += volumeA;
                            break;
                        case "B+":
                        case "B-":
                            String VolumeForB = snapshot.child(key).child("Volume").getValue(String.class);
                            int volumeB = Integer.parseInt(VolumeForB);
                            totalVolumeForB += volumeB;
                            break;
                        case "O+":
                        case "O-":
                            String VolumeForO = snapshot.child(key).child("Volume").getValue(String.class);
                            int volumeO = Integer.parseInt(VolumeForO);
                            totalVolumeForO += volumeO;
                            break;
                    }
                }

                totalABVolume.setText(totalVolumeForAB + "mL");
                totalAVolume.setText(totalVolumeForA + "mL");
                totalBVolume.setText(totalVolumeForB + "mL");
                totalOVolume.setText(totalVolumeForO + "mL");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void exportData(String basedEvent, String titleOfEvent,
                            String ttlDonors, String ttlStudents, String ttlEmployees, String ttlGuests,
                            String abp, String abn, String ap, String an,
                            String bp, String bn, String op, String on,
                            String ttlAB, String ttlA, String ttlB, String ttlO,
                            String ttlABVolume, String ttlAVolume, String ttlBVolume, String ttlOVolume) {

        String hold = "Date of the Event : " + basedEvent + "\n" +
                "Title of the Event : " + titleOfEvent + "\n" +
                "Total Number Donors : " + ttlDonors + "\n" +
                "Total Number Students Donated : " + ttlStudents + "\n" +
                "Total Number Employees Donated : " + ttlEmployees + "\n" +
                "Total Number Guests Donated : " + ttlGuests + "\n\n" +
                "Blood Types Donated\n\n" +
                "TYPE AB\n" +
                "Positive : " + abp + "\nNegative : " + abn + "\nVolume : " + ttlABVolume + "\nTotal : " + ttlAB + "\n\n" +
                "TYPE A\n" +
                "Positive : " + ap + "\nNegative : " + an + "\nVolume : " + ttlAVolume + "\nTotal : " + ttlA + "\n\n" +
                "TYPE B\n" +
                "Positive : " + bp + "\nNegative : " + bn + "\nVolume : " + ttlBVolume + "\nTotal : " + ttlB + "\n\n" +
                "TYPE O\n" +
                "Positive : " + op + "\nNegative : " + on + "\nVolume : " + ttlOVolume + "\nTotal : " + ttlO + "\n\n";

        Document mDoc = new Document();
        String mFilePath = this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS) + "/" + basedEvent + ".pdf";

        try {
            PdfWriter.getInstance(mDoc, new FileOutputStream(mFilePath));
            mDoc.open();
            mDoc.addAuthor("ZoiDonar");
            mDoc.add(new Paragraph(hold));
            mDoc.close();

            Toast.makeText(this, "Exported : " + mFilePath, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case STORAGE_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    String selectedItem = eventPickSpinner.getSelectedItem().toString();
                    displaySpinnerContent(selectedItem);
                    String titleOfEvent = displayTitle.getText().toString();
                    String ttlDonors = totalDonorCount.getText().toString();
                    String ttlStudents = totalStudents.getText().toString();
                    String ttlEmployees = totalEmployees.getText().toString();
                    String ttlGuests = totalGuests.getText().toString();
                    String abp = btAbp.getText().toString();
                    String abn = btAbn.getText().toString();
                    String ap = btAp.getText().toString();
                    String an = btAn.getText().toString();
                    String bp = btBp.getText().toString();
                    String bn = btBn.getText().toString();
                    String op = btOp.getText().toString();
                    String on = btOn.getText().toString();
                    String ttlAB = totalAB.getText().toString();
                    String ttlA = totalA.getText().toString();
                    String ttlB = totalB.getText().toString();
                    String ttlO = totalO.getText().toString();
                    String ttlABVolume = totalABVolume.getText().toString();
                    String ttlAVolume = totalAVolume.getText().toString();
                    String ttlBVolume = totalBVolume.getText().toString();
                    String ttlOVolume = totalOVolume.getText().toString();
                    exportData(selectedItem, titleOfEvent, ttlDonors, ttlStudents, ttlEmployees, ttlGuests,
                            abp, abn, ap, an, bp, bn, op, on,
                            ttlAB, ttlA, ttlB, ttlO,
                            ttlABVolume, ttlAVolume, ttlBVolume, ttlOVolume);
                } else {
                    Toast.makeText(this, "Permission Denied...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}