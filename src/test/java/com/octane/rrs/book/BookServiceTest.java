package com.octane.rrs.book;

import com.octane.rrs.model.UserReadingInterval;
import com.octane.rrs.repository.UserReadingIntervalRepository;
import com.octane.rrs.service.UserReadingIntervalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
    @WithMockUser(username = "bm@bm.com", roles = "ADMIN")
    public void testUpdateIntervals() {
        // Mock data
        UserReadingInterval newUserReadingInterval = UserReadingInterval.builder().startPage(4).endPage(10).build();
        int userId = 456;
        int bookId = 123;
        // Mock repository behavior
        when(repository.findAllByUserIdAndBookIdOrderByStartPage(userId, bookId)).thenReturn(new ArrayList<>());

        int result = userReadingIntervalService.updateIntervals(newUserReadingInterval, bookId, userId);

        // Assert the result
        assertEquals(7, result);

        // Verify method calls
        verify(repository).findAllByUserIdAndBookIdOrderByStartPage(anyInt(), anyInt());

    }

    @Test
    public void testUpdateIntervals_ExistingIntervals() {
        // Mock data
        UserReadingInterval newUserReadingInterval = UserReadingInterval.builder().startPage(4).endPage(10).build();
        int userId = 456;
        int bookId = 123;

        // Mock existing intervals
        List<UserReadingInterval> existingIntervals = new LinkedList<>(Arrays.asList(
                UserReadingInterval.builder().startPage(1).endPage(3).build(),
                UserReadingInterval.builder().startPage(11).endPage(15).build()
        ));
        when(repository.findAllByUserIdAndBookIdOrderByStartPage(userId, bookId)).thenReturn(existingIntervals);

        int result = userReadingIntervalService.updateIntervals(newUserReadingInterval, bookId, userId);

        // Assert the result
        assertEquals(7, result);

        // Verify method calls
        verify(repository).findAllByUserIdAndBookIdOrderByStartPage(anyInt(), anyInt());
    }

    @Test
    public void testUpdateIntervals_OverlapWithExistingIntervals() {
        // Mock data
        UserReadingInterval newUserReadingInterval = UserReadingInterval.builder().startPage(4).endPage(10).build();
        int userId = 456;
        int bookId = 123;

        // Mock existing intervals
        List<UserReadingInterval> existingIntervals = new LinkedList<>(Arrays.asList(
                UserReadingInterval.builder().userIntervalId(1).bookId(bookId).userId(userId).startPage(1).endPage(5).build(),
                UserReadingInterval.builder().userIntervalId(2).bookId(bookId).userId(userId).startPage(8).endPage(12).build()
        ));
        when(repository.findAllByUserIdAndBookIdOrderByStartPage(userId, bookId)).thenReturn(existingIntervals);
        int result = userReadingIntervalService.updateIntervals(newUserReadingInterval, bookId, userId);

        // Assert the result
        assertEquals(2, result);

        // Verify method calls
        verify(repository).findAllByUserIdAndBookIdOrderByStartPage(anyInt(), anyInt());
    }

}
