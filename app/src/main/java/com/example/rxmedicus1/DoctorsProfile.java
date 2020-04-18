package com.example.rxmedicus1;

public class DoctorsProfile {
    public String status;
    public String doctorsname;
    public String doctorsnumber;
    public String residenciaddress;
    public String cliniaddress;
    public String doctorsexperience;
    public String doctorsspecialisation;

    public DoctorsProfile(){}

    public DoctorsProfile(String status, String doctorsname, String doctorsnumber, String residenciaddress, String cliniaddress, String doctorsexperience, String doctorsspecialisation) {
        this.status = status;
        this.doctorsname = doctorsname;
        this.doctorsnumber = doctorsnumber;
        this.residenciaddress = residenciaddress;
        this.cliniaddress = cliniaddress;
        this.doctorsexperience = doctorsexperience;
        this.doctorsspecialisation = doctorsspecialisation;
    }

    public String getDoctorsname() {
        return doctorsname;
    }

    public void setDoctorsname(String doctorsname) {
        this.doctorsname = doctorsname;
    }

    public String getDoctorsnumber() {
        return doctorsnumber;
    }

    public void setDoctorsnumber(String doctorsnumber) {
        this.doctorsnumber = doctorsnumber;
    }

    public String getResidenciaddress() {
        return residenciaddress;
    }

    public void setResidenciaddress(String residenciaddress) {
        this.residenciaddress = residenciaddress;
    }

    public String getCliniaddress() {
        return cliniaddress;
    }

    public void setCliniaddress(String cliniaddress) {
        this.cliniaddress = cliniaddress;
    }

    public String getDoctorsexperience() {
        return doctorsexperience;
    }

    public void setDoctorsexperience(String doctorsexperience) {
        this.doctorsexperience = doctorsexperience;
    }

    public String getDoctorsspecialisation() {
        return doctorsspecialisation;
    }

    public void setDoctorsspecialisation(String doctorsspecialisation) {
        this.doctorsspecialisation = doctorsspecialisation;
    }
}
