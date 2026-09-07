package com.hyunjun.library.dto;

import com.hyunjun.library.entity.Book;
import lombok.Getter;

@Getter
public class ResponseBook {

    private Long id;
    private String name;
    private String author;
    private String isbn;
    private boolean isUsed;

    public ResponseBook(Long id, String name, String author, String isbn, boolean isUsed) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.isbn = isbn;
        this.isUsed = isUsed;
    }

    public ResponseBook(Book book) {
        this.id = book.getId();
        this.name = book.getName();
        this.author = book.getAuthor();
        this.isbn = book.getIsbn();
        this.isUsed = book.isUsed();
    }
}
