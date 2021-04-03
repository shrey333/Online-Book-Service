package com.example.demo.web.controller;

import com.example.demo.model.Book;
import com.example.demo.model.User;
import com.example.demo.service.BookService;
import com.example.demo.service.UserService;
import com.example.demo.web.dto.BookRegisterDto;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.sql.Date;
import java.util.List;

@Controller
@RequestMapping("/book")
public class BookController {
    private final BookService bookService;
    private final UserService userService;

    @Autowired
    public BookController(@Lazy BookService bookService,
                          @Lazy UserService userService){
        this.bookService = bookService;
        this.userService = userService;
    }

    @GetMapping("/publish")
    public String publish(Model model){
        model.addAttribute("book", new Book());
        return "publish";
    }

    @PostMapping("/publish")
    public String publish(@ModelAttribute("book")Book book,
                          @RequestParam("bookFile") MultipartFile bookFile,
                          @RequestParam("coverFile") MultipartFile coverFile,
                          @AuthenticationPrincipal UserDetails user) throws IOException {
        try{
            User user1 = userService.findUser(user.getUsername());
            book.setPublishDate(Date.valueOf(LocalDate.now()));
            book.setDocData(bookFile.getBytes());
            book.setDocType(bookFile.getContentType());
            book.setCoverData(coverFile.getBytes());
            book.setCoverType(coverFile.getContentType());
            book.setUser(user1);
            book.setAuthor(user1.getDisplayName());
            bookService.save(book);
            return "redirect:/book/publish?success";
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return  "redirect:/book/publish?error";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable String id, Model model){
        Book book = bookService.getBookById(Long.parseLong(id));
        model.addAttribute("book", book);
        return "bookUpdate";
    }

    @PostMapping("/update")
    public String update(@ModelAttribute("book")Book book,
                          @RequestParam("bookFile") MultipartFile bookFile,
                          @RequestParam("coverFile") MultipartFile coverFile) throws IOException {
        try{
            if(!coverFile.isEmpty()){
                book.setCoverData(coverFile.getBytes());
                book.setCoverType(coverFile.getContentType());
            }
            if(!bookFile.isEmpty()){
                book.setDocData(bookFile.getBytes());
                book.setDocType(bookFile.getContentType());
            }
            bookService.update(book);
            return "redirect:/book/update?success";
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return  "redirect:/book/publish?error";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable String id, Model model){
        Book book = bookService.getBookById(Long.parseLong(id));
        model.addAttribute("book", book);
        return "viewPdf";
    }

    @GetMapping("/image/{id}")
    public void showImage(@PathVariable String id,
                               HttpServletResponse response) throws IOException {
        Book book = bookService.getBookById(Long.parseLong(id));
        response.setContentType(book.getCoverType());
        InputStream is = new ByteArrayInputStream(book.getCoverData());
        IOUtils.copy(is, response.getOutputStream());
    }

    @GetMapping("/file/{id}")
    public void showFile(@PathVariable String id,
                         HttpServletResponse response) throws IOException{
        Book book = bookService.getBookById(Long.parseLong(id));
        response.setContentType(book.getDocType());
        response.addHeader("Content-Disposition", "inline; filename=book.pdf");
        InputStream is = new ByteArrayInputStream(book.getDocData());
        IOUtils.copy(is, response.getOutputStream());
    }
}
