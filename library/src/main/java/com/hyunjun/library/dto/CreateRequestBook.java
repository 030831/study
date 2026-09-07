package com.hyunjun.library.dto;

import com.hyunjun.library.service.BookCreateCommand;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateRequestBook {

    @NotBlank
    private String name;

    @NotBlank
    private String author;

    @NotBlank
    private String isbn;

    public BookCreateCommand toCommand() {
        return new BookCreateCommand(name, author, isbn);
    }
}
