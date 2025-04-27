package com.mycompany.bookstoreapiweb.dao;

import com.mycompany.bookstoreapiweb.model.Book;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class BookDAO {
    private static final Map<Integer, Book> books = new ConcurrentHashMap<>();
    private static final AtomicInteger bookId = new AtomicInteger(1);

    public Book addBook(Book book) {
        book.setId(bookId.getAndIncrement());
        books.put(book.getId(), book);
        return book;
    }

    public Collection<Book> getAllBooks() {
        return books.values();
    }

    public Book getBookById(int id) {
        return books.get(id);
    }

    public Book updateBook(int id, Book book) {
        book.setId(id);
        books.put(id, book);
        return book;
    }

    public boolean deleteBook(int id) {
        return books.remove(id) != null;
    }

    public boolean bookExists(int id) {
        return books.containsKey(id);
    }

    public void updateBookStock(int bookId, int newStock) {
        Book book = books.get(bookId);
        if (book != null) {
            book.setStock(newStock); // Update the stock
        } else {
            throw new IllegalArgumentException("Book with ID " + bookId + " does not exist.");
        }
    }
}