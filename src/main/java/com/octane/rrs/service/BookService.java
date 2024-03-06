package com.octane.rrs.service;

import com.octane.rrs.exception.CustomException;
import com.octane.rrs.exception.ErrorCode;
import com.octane.rrs.model.UserReadingInterval;
import com.octane.rrs.model.book.AddBookRequest;
import com.octane.rrs.model.book.Book;
import com.octane.rrs.repository.BookRepository;
import com.octane.rrs.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private static final Logger logger = LoggerFactory.getLogger(BookService.class);
    private final BookRepository repository;
    private final UserReadingIntervalService userReadingIntervalService;
    @Value("${num.of.top}")
    private int numOfTop;

    @Transactional
    public Book addBook(AddBookRequest addBookRequest) {
        Book book = constructBook(addBookRequest);
        Book savedBook = saveBook(book);
        logger.debug("createBook()>> savedBook: {}", savedBook);
        return savedBook;
    }

    private Book constructBook(AddBookRequest addBookRequest) {
        Book book =
                Book.builder().bookName(addBookRequest.getBookName()).numOfPages(addBookRequest.getNumOfPages()).build();
        logger.debug("constructBook()>> book: {}", book);
        return book;
    }

    public Book getBookById(int bookId) {
        logger.info("getBookById()>> bookId: {}", bookId);
        Book book =
                repository.findById(bookId).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BOOK));
        logger.debug("getBookById()>> book: {}", book);
        return book;
    }

    public void validateUserReadingInterval(UserReadingInterval newUserReadingInterval) {
        logger.info("validateUserReadingInterval()>> newUserReadingInterval: {}",
                newUserReadingInterval);
        if (newUserReadingInterval.getUserId() != JwtService.getSubject()) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_USER_ID);
        }
        Book book = getBookById(newUserReadingInterval.getBookId());
        if (newUserReadingInterval.getStartPage() > newUserReadingInterval.getEndPage()) {
            throw new CustomException(ErrorCode.BAD_REQUEST_START_PAGE_BIGGER_THAN_END_PAGE);
        }
        if (newUserReadingInterval.getEndPage() > book.getNumOfPages()) {
            throw new CustomException(ErrorCode.BAD_REQUEST_END_PAGE_BIGGER_THAN_BOOK_PAGES);
        }
        newUserReadingInterval.setUserId(JwtService.getSubject());
        newUserReadingInterval.setBookId(book.getBookId());
        logger.debug("validateUserReadingInterval()>> newUserReadingInterval: {}", newUserReadingInterval);
    }

    public boolean submitInterval(UserReadingInterval newUserReadingInterval) {
        int bookId = newUserReadingInterval.getBookId();
        Book book = getBookById(bookId);
        int userId = JwtService.getSubject();
        int newNumOfReadingPages;
//  FIRST APPROACH
//        /** Delete Comment
        newNumOfReadingPages = userReadingIntervalService.updateIntervals
                (newUserReadingInterval, bookId, userId);
//         **/

//  SECOND APPROACH
//        newNumOfReadingPages = userReadingIntervalService._updateIntervals(newUserReadingInterval
//                , bookId,userId);

        logger.debug("submitInterval()>> newNumOfReadingPages: {}", newNumOfReadingPages);
        book.setNumOfReadPages(book.getNumOfReadPages() + newNumOfReadingPages);
        saveBook(book);
        return true;
    }

    private Book saveBook(Book book) {
        logger.debug("saveBook()>> book: {}", book);
        return repository.save(book);
    }


    public List<Book> getTopBooks() {
        logger.info("getTopBooks()>> numOfTop: {}", numOfTop);
        return repository.findAllByOrderByNumOfReadPagesDesc(Limit.of(numOfTop));
    }
}
