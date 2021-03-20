package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String author;
    @Column(columnDefinition = "nvarchar(MAX)")
    private String description;
    private int edition;
    private int pages;
    private double price;
    @Column(unique = true)
    private String isbn;
    @Temporal(TemporalType.DATE)
    private Date publishDate;
    private String docType;
    @Lob
    private byte[] docData;
    private String coverType;
    @Lob
    private byte[] coverData;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "userId", nullable = false)
    private User user;

    public Book(String title,
                String description,
                String author,
                int edition,
                int pages,
                double price,
                String isbn,
                Date publishDate,
                User user,
                String docType,
                byte[] docData,
                String coverType,
                byte[] coverData){
        this.title = title;
        this.description = description;
        this.author = author;
        this.edition = edition;
        this.pages = pages;
        this.price = price;
        this.isbn = isbn;
        this.publishDate = publishDate;
        this.user = user;
        this.docData = docData;
        this.docType = docType;
        this.coverData = coverData;
        this.coverType = coverType;
    }
}
