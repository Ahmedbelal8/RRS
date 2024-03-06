package com.octane.rrs.controller;

import com.octane.rrs.dto.ApiResponseDto;
import com.octane.rrs.dto.UserReadingIntervalDto;
import com.octane.rrs.enums.StatusCode;
import com.octane.rrs.mapper.MapperHelper;
import com.octane.rrs.model.UserReadingInterval;
import com.octane.rrs.model.book.AddBookRequest;
import com.octane.rrs.model.book.Book;
import com.octane.rrs.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
public class BookController {
    private static final Logger logger = LoggerFactory.getLogger(BookController.class);
    private final BookService bookService;
    private final MapperHelper<UserReadingIntervalDto, UserReadingInterval> userReadingIntervalMapperHelper;

    @PostMapping("/add")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Book addBook(@Valid @RequestBody AddBookRequest addBookRequest) {
        logger.info("addBook()>> addBookRequest: {}", addBookRequest);
        return bookService.addBook(addBookRequest);
    }


    @PostMapping("/submit-interval")
    public ResponseEntity<ApiResponseDto> submitInterval(@Valid @RequestBody UserReadingIntervalDto userReadingIntervalDto) {
        logger.info("submitInterval()>> userReadingIntervalDto: {}", userReadingIntervalDto);
        UserReadingInterval userReadingInterval = userReadingIntervalMapperHelper.map(userReadingIntervalDto, UserReadingInterval.class);
        bookService.validateUserReadingInterval(userReadingInterval);
        boolean statusCode = bookService.submitInterval(userReadingInterval);
        return ResponseEntity.ok(new ApiResponseDto(StatusCode.getStatusCode(statusCode)));
    }

    @GetMapping("/top-books")
    public List<Book> getTopBooks() {
        logger.info("getTopBooks()");
        return bookService.getTopBooks();
    }
}
