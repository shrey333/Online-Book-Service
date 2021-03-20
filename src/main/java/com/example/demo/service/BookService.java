package com.example.demo.service;

import com.example.demo.model.Book;
import com.example.demo.model.User;
import com.example.demo.web.dto.BookRegisterDto;

import java.util.List;

public interface BookService {
    Book save(Book book);
    List<Book> getBooks();
    Book getBookById(long id);
    Book update(Book book);
    List<Book> getBooksByUser(User user);
    List<Book> getBooksByTitle(String title);
}
