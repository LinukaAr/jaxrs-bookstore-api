package com.mycompany.bookstoreapiweb.model;

public class Book {
    private int id;
    private int authorId;
    private String title;
    private String isbn;
    private int publicationYear;
    private double price;
    private int stock;

    // Default constructor
    public Book() {
    }

    // Constructor with all fields
    public Book(int id, int authorId, String title, String isbn, int publicationYear, double price, int stock) {
        this.id = id;
        this.authorId = authorId;
        this.title = title;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.price = price;
        this.stock = stock;
    }

    // Getters and Setters

    public int getId() { 
        return id; 
    }

    public void setId(int id) { 
        this.id = id; 
    }

    public int getAuthorId() { 
        return authorId; 
    }

    public void setAuthorId(int authorId) { 
        this.authorId = authorId; 
    }

    public String getTitle() { 
        return title; 
    }

    public void setTitle(String title) { 
        this.title = title; 
    }

    public String getIsbn() { 
        return isbn; 
    }

    public void setIsbn(String isbn) { 
        this.isbn = isbn; 
    }

    public int getPublicationYear() { 
        return publicationYear; 
    }

    public void setPublicationYear(int publicationYear) { 
        this.publicationYear = publicationYear; 
    }

    public double getPrice() { 
        return price; 
    }

    public void setPrice(double price) { 
        this.price = price; 
    }

    public int getStock() { 
        return stock; 
    }

    public void setStock(int stock) { 
        this.stock = stock; 
    }

}
