package com.octane.rrs.model.book;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class AddBookRequest {

    @JsonProperty("book_name")
    @NotBlank
    private String bookName;
    @JsonProperty("num_of_pages")
    @Positive
    private int numOfPages;
}
