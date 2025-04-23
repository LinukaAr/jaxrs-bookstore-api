package com.mycompany.bookstoreapiweb.dao;

import com.mycompany.bookstoreapiweb.model.Author;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class AuthorDAO {
    private static final Map<Integer, Author> authors = new ConcurrentHashMap<>();
    private static final AtomicInteger authorId = new AtomicInteger(1);

    public Author addAuthor(Author author) {
        author.setId(authorId.getAndIncrement());
        authors.put(author.getId(), author);
        return author;
    }

    public Collection<Author> getAllAuthors() {
        return authors.values();
    }

    public Author getAuthorById(int id) {
        return authors.get(id);
    }

    public Author updateAuthor(int id, Author author) {
        author.setId(id);
        authors.put(id, author);
        return author;
    }

    public boolean deleteAuthor(int id) {
        return authors.remove(id) != null;
    }

    public boolean authorExists(int id) {
        return authors.containsKey(id);
    }
}