package com.arzaal.arzaal.contact;

/**
 * Created by henry on 4/1/16.
 */
public class Contact {
    private String name;
    private String phone;
    private String facebookName;
    private String email;
    private String gmail;

    public Contact() {

    }

    public Contact(String name, String phone, String facebookName, String email) {
        this.setName(name);
        this.setPhone(phone);
        this.setEmail(email);
        this.setFacebookName(facebookName);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFacebookName() {
        return facebookName;
    }

    public void setFacebookName(String facebookName) {
        this.facebookName = facebookName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }
}
