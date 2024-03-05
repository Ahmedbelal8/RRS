package com.octane.rrs.book;

import com.octane.rrs.model.UserReadingInterval;
import com.octane.rrs.repository.UserReadingIntervalRepository;
import com.octane.rrs.service.UserReadingIntervalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class BookServiceTest {

    @Mock
    private UserReadingIntervalRepository repository;
    @InjectMocks
    private UserReadingIntervalService userReadingIntervalService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testUpdateIntervals() {
        // Mock data
        UserReadingInterval newUserReadingInterval = UserReadingInterval.builder().startPage(4).endPage(10).build();
        int userId = 456;
        int bookId = 123;


        // Mock repository behavior
        when(repository.findAllByUserIdAndBookIdOrderByStartPage(userId, bookId)).thenReturn(new ArrayList<>());

        int result = userReadingIntervalService.updateIntervals(newUserReadingInterval, bookId, userId);

        // Assert the result
        assertEquals(6, result);

        // Verify method calls
        verify(repository).findAllByUserIdAndBookIdOrderByStartPage(anyInt(), anyInt());

    }
}
