package com.octane.rrs.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class UserReadingIntervalDto {

    @JsonProperty("book_id")
    @PositiveOrZero
    private int bookId;
    @JsonProperty("user_id")
    @PositiveOrZero
    private int userId;
    @JsonProperty("start_page")
    @Positive
    private int startPage;
    @JsonProperty("end_page")
    @Positive
    private int endPage;
}
