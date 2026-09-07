package com.hyunjun.library.service;

import com.hyunjun.library.dto.ResponseBook;
import com.hyunjun.library.entity.Book;
import com.hyunjun.library.exception.BookNotFoundException;
import com.hyunjun.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    public ResponseBook create(String name, String author, String isbn) {
        Book savedBook = bookRepository.save(new Book(name, author, isbn, false));

        return new ResponseBook(savedBook);
    }

    public ResponseBook findBook(Long id) {
        Book findBook = bookRepository.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));

        return new ResponseBook(findBook);
    }

    public List<ResponseBook> findBooks() {
        return bookRepository.findAll()
                .stream()
                .map(ResponseBook::new)
                .toList();
    }
}
