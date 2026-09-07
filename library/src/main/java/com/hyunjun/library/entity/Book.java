package com.hyunjun.library.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * Member 1 ── N Loan 1 ── N LoanItem N ── 1 Book
 */
@Entity
@Getter
public class Book {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String author;

    @Column(unique = true, nullable = false)
    private String isbn;

    @Column(nullable = false)
    private boolean isUsed;

    @OneToMany(mappedBy = "book")
    private List<LoanItem> loanItems = new ArrayList<>();

    public Book(String name, String author, String isbn, boolean isUsed) {
        this.name = name;
        this.author = author;
        this.isbn = isbn;
        this.isUsed = isUsed;
    }

    protected Book() {

    }
}
