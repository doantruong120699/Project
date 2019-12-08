package com.example.hello;

public class Student {
    int id;
    String name;
    String address;
    String phone_number;

    public Student(int id, String name, String address, String phone_number) {
        super();
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone_number = phone_number;
    }

    public Student(String name, String address, String phone_number) {
        super();
        this.name = name;
        this.address = address;
        this.phone_number = phone_number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}

