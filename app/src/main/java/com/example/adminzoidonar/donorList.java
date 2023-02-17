package com.example.adminzoidonar;

public class donorList {

    String nameOfDonor, bloodTypeOfDonor, donorDonated;

    public donorList(String nameOfDonor, String bloodTypeOfDonor, String donorDonated) {
        this.nameOfDonor = nameOfDonor;
        this.bloodTypeOfDonor = bloodTypeOfDonor;
        this.donorDonated = donorDonated;
    }

    public String getNameOfDonor() {
        return nameOfDonor;
    }

    public String getBloodTypeOfDonor() {
        return bloodTypeOfDonor;
    }

    public String getDonorDonated() {
        return donorDonated;
    }
}
