package com.example.demo.web.dto;

import com.example.demo.model.User;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Data
@NoArgsConstructor
public class BookRegisterDto {
    private String title;
    private String description;
    private int edition;
    private int pages;
    private double price;
    private String isbn;
    private Date publishDate;
    private String docType;
    private byte[] docData;
    private String coverType;
    private byte[] coverData;
    private User user;
}
