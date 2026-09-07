package com.hyunjun.library.controller;

import com.hyunjun.library.dto.CreateRequestBook;
import com.hyunjun.library.dto.ResponseBook;
import com.hyunjun.library.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @PostMapping("/books")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ResponseBook createBook(@Valid @RequestBody CreateRequestBook request) {
        return bookService.create(request.getName(), request.getAuthor(), request.getIsbn());
    }

    @GetMapping("/books/{id}")
    public ResponseBook findBook(@PathVariable("id") Long BookId) {
        return bookService.findBook(BookId);
    }

    @GetMapping("/books")
    public List<ResponseBook> findBooks() {
        return bookService.findBooks();
    }
}
