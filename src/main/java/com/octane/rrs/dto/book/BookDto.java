package com.octane.rrs.dto.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class BookDto {

    @JsonProperty("book_id")
    private int bookId;
    @JsonProperty("book_name")
    private String bookName;
    @JsonProperty("num_of_pages")
    private int numOfPages;
    @JsonProperty("num_of_read_pages")
    private int numOfReadPages;
}
