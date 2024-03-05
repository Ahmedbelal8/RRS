package com.octane.rrs.model.book;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Integer bookId;
    @Column(name = "book_name",nullable = false)
    private String bookName;
    @Column(name = "num_of_pages",nullable = false)
    private int numOfPages;
    @Column(name = "num_of_read_pages",nullable = false)
    private int numOfReadPages;
}
