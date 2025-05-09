package model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Customer {
    private String id;
    private String name;
    private String phoneNumber;
    private Date birthDate;
    private String address;

    public Customer(String id, String name, String phoneNumber, Date birthDate, String address) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.birthDate = birthDate;
        this.address = address;
    }

    // In thông tin
    public void print() {
        System.out.println(id + " \n" + name + " \n" + phoneNumber + " \n" + birthDate + " \n" + address + "\n");
    }
    
    public String getBirthDateStr(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = sdf.format(birthDate); 
        return formattedDate;
    }

    // Getters
    public String getId(){return id;}
    public String getName() { return name; }
    public String getPhoneNumber() { return phoneNumber; }
    public Date getBirthDate() { return birthDate; }
    public String getAddress() { return address; }
}
