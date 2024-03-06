package com.octane.rrs.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UserReadingIntervalDto {

    @JsonProperty("book_id")
    @Positive
    private int bookId;
    @JsonProperty("user_id")
    @Positive
    private int userId;
    @JsonProperty("start_page")
    @Positive
    private int startPage;
    @JsonProperty("end_page")
    @Positive
    private int endPage;
}
