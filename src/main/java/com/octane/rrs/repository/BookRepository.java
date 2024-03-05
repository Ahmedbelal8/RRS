package com.octane.rrs.repository;

import com.octane.rrs.model.book.Book;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    List<Book> findAllByOrderByNumOfReadPagesDesc(Limit limit);
}
