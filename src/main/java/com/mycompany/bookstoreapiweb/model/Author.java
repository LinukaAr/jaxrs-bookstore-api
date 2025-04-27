package com.mycompany.bookstoreapiweb.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Author {
    private int id;

    @JsonProperty("fName")
    private String fname;

    @JsonProperty("lName")
    private String lname;

    @JsonProperty("biography")
    private String biography;

    public Author() {
    }

    public Author(int id, String fname, String lname, String biography) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.biography = biography;
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

    public void setFName(String fname) { 
        this.fname = fname; 
    }

    public String getLName() { 
        return lname; 
    }

    public void setLName(String lname) { 
        this.lname = lname; 
    }

    public String getBiography() { 
        return biography; 
    }

    public void setBiography(String biography) { 
        this.biography = biography; 
    }
}
