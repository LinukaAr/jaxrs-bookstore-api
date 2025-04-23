package com.mycompany.bookstoreapiweb.model;

public class Customer {
    private int id;
    private String fname;
    private String lname;
    private String email;
    private String password;

    public Customer() {
    }

    public Customer(int id, String fname, String lname, String email, String password) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.password = password;
    }

    // Getters and Setters

    public int getId() { 
        return id; 
    }

    public void setId(int id) { 
        this.id = id; 
    }

    public String getFName() { 
        return fname; 
    }

    public void setFName(String name) { 
        this.fname = name; 
    }

    public String getLName() { 
        return lname; 
    }

    public void setLName(String lname) { 
        this.lname = lname; 
    }

    public String getEmail() { 
        return email; 
    }

    public void setEmail(String email) { 
        this.email = email; 
    }

    public String getPassword() { 
        return password; 
    }

    public void setPassword(String password) { 
        this.password = password; 
    }
}
