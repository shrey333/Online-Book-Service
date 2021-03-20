package com.example.demo.service;

import com.example.demo.model.Book;
import com.example.demo.model.User;
import com.example.demo.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(@Lazy BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }

    @Override
    public Book save(Book book) {
        try{
            return bookRepository.save(book);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Book> getBooksByUser(User user){
        return bookRepository.findByUser(user);
    }

    @Override
    public List<Book> getBooksByTitle(String title){
        return  bookRepository.findByTitle(title);
    }

    @Override
    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book getBookById(long id){
        return bookRepository.getOne(id);
    }

    @Override
    public Book update(Book book) {
        try{
            Book tempBook = getBookById(book.getId());
            book.setPublishDate(tempBook.getPublishDate());
            book.setUser(tempBook.getUser());
            book.setAuthor(tempBook.getAuthor());
            if(book.getCoverData() == null){
                book.setCoverData(tempBook.getCoverData());
                book.setCoverType(tempBook.getCoverType());
            }
            if(book.getDocData() == null){
                book.setDocData(tempBook.getDocData());
                book.setDocType(tempBook.getDocType());
            }
            return bookRepository.save(book);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
