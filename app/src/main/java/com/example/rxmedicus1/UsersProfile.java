package com.example.rxmedicus1;


 public class UsersProfile {

    public String username;
    public String age;
    public String gender;
    public String address;
    public String dateofbirth;
    public String income;
    public String number;
    public String bloodgroup;


    public UsersProfile(){}

    public UsersProfile(String username, String age,String gender,String address,String dateofbirth, String income,String number,String bloodgroup) {
        this.username = username;
        this.age = age;
       this.gender = gender;
       this.address = address;
       this.dateofbirth = dateofbirth;
       this.income = income;
       this.number = number;
       this.bloodgroup = bloodgroup;
    }

    public String getUsername() {
       return username;
    }

    public void setUsername(String username) {
       this.username = username;
    }

    public String getAge() {
       return age;
    }

    public void setAge(String age) {
       this.age = age;
    }

    public String getGender() {
       return gender;
    }

    public void setGender(String gender) {
       this.gender = gender;
    }

    public String getAddress() {
       return address;
    }

    public void setAddress(String address) {
       this.address = address;
    }

    public String getDateofbirth() {
       return dateofbirth;
    }

    public void setDateofbirth(String dateofbirth) {
       this.dateofbirth = dateofbirth;
    }

    public String getIncome() {
       return income;
    }

    public void setIncome(String income) {
       this.income = income;
    }

    public String getNumber() {
       return number;
    }

    public void setNumber(String number) {
       this.number = number;
    }

    public String getBloodgroup() {
       return bloodgroup;
    }

    public void setBloodgroup(String bloodgroup) {
       this.bloodgroup = bloodgroup;
    }
 }