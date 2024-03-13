package com.octane.rrs.service;

import com.octane.rrs.model.UserReadingInterval;
import com.octane.rrs.repository.UserReadingIntervalRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserReadingIntervalService {
    private static final Logger logger = LoggerFactory.getLogger(UserReadingIntervalService.class);
    private final UserReadingIntervalRepository repository;


    private List<UserReadingInterval> getUserReadingIntervalsByBookIdAndUserId(int bookId,
                                                                               int userId) {
        logger.info("getUserReadingIntervalsByBookIdAndUserId()>> bookId: {}, userId: {}",
                bookId, userId);
        List<UserReadingInterval> userReadingIntervals =
                repository.findAllByUserIdAndBookIdOrderByStartPage(userId,
                        bookId);
        logger.debug("getUserReadingIntervalsByBookIdAndUserId()>> userReadingIntervals: {}", userReadingIntervals);
        return userReadingIntervals;
    }

    public int updateIntervals(UserReadingInterval newUserReadingInterval, int bookId, int userId) {
        logger.info("updateIntervals()>> newUserReadingInterval: {}, bookId: {}, userId: {}",
                newUserReadingInterval, bookId, userId);
        int numOfPages =
                newUserReadingInterval.getEndPage() - newUserReadingInterval.getStartPage() + 1;
        logger.debug("updateIntervals()>> numOfPages: {}", numOfPages);
        List<UserReadingInterval> userReadingIntervals =
                getUserReadingIntervalsByBookIdAndUserId(bookId, userId);
        if (userReadingIntervals.isEmpty()) {
            saveUserReadingInterval(newUserReadingInterval);
            return numOfPages;
        }
        boolean addNewInterval = true;
        List<UserReadingInterval> deletedUserReadingInterval = new ArrayList<>();
        for (UserReadingInterval item : userReadingIntervals) {
            if (newUserReadingInterval.getStartPage() < item.getStartPage()) {
                if (newUserReadingInterval.getEndPage() < item.getStartPage()) {
                    break;
                } else if (newUserReadingInterval.getEndPage() > item.getEndPage()) {
                    deletedUserReadingInterval.add(item);
                    numOfPages -= item.getEndPage() - item.getStartPage() + 1;
                } else {
                    numOfPages -= newUserReadingInterval.getEndPage() - item.getStartPage() + 1;
                    item.setStartPage(newUserReadingInterval.getStartPage());
                    addNewInterval = false;
                    break;
                }
            } else if (newUserReadingInterval.getStartPage() <= item.getEndPage()) {
                if (newUserReadingInterval.getEndPage() <= item.getEndPage()) {
                    numOfPages = 0;
                    addNewInterval = false;
                    break;
                } else {
                    deletedUserReadingInterval.add(item);
                    numOfPages -= item.getEndPage() - newUserReadingInterval.getStartPage() + 1;
                    newUserReadingInterval.setStartPage(item.getStartPage());
                }
            }
        }
        if (addNewInterval) {
            userReadingIntervals.add(newUserReadingInterval);
        }
        deleteUserReadingIntervals(deletedUserReadingInterval);
        userReadingIntervals.removeAll(deletedUserReadingInterval);
        saveUserReadingIntervals(userReadingIntervals);

        return numOfPages;
    }

    public int _updateIntervals(UserReadingInterval newUserReadingInterval, int bookId, int userId) {
        logger.info("_updateIntervals()>> newUserReadingInterval: {}, bookId: {}, userId: {}",
                newUserReadingInterval, bookId, userId);
        List<UserReadingInterval> userReadingIntervals =
                getUserReadingIntervalsByBookIdAndUserId(bookId, userId);

        if (userReadingIntervals.isEmpty()) {
            saveUserReadingInterval(newUserReadingInterval);
            return newUserReadingInterval.getEndPage() - newUserReadingInterval.getStartPage() + 1;
        }

        int oldRead = 0, newRead = 0;
        int numOfIntervals = userReadingIntervals.size();
        int indexOfNewInterval = numOfIntervals;
        for (int i = 0; i < numOfIntervals; i++) {
            oldRead += userReadingIntervals.get(i).getEndPage() - userReadingIntervals.get(i).getStartPage() + 1;
            if (newUserReadingInterval.getStartPage() < userReadingIntervals.get(i).getStartPage()
                    && indexOfNewInterval == numOfIntervals) {
                indexOfNewInterval = i;
            }
        }
        userReadingIntervals.add(indexOfNewInterval, newUserReadingInterval);
        logger.debug("_updateIntervals()>> indexOfNewInterval: {}, userReadingIntervals size: {}",
                indexOfNewInterval, userReadingIntervals.size());
        List<UserReadingInterval> newUserReadingIntervals = new ArrayList<>();
        int start = userReadingIntervals.get(0).getStartPage();
        int end = userReadingIntervals.get(0).getEndPage();
        for (int i = 1; i < numOfIntervals + 1; i++) {
            if (userReadingIntervals.get(i).getStartPage() > end + 1) {
                newUserReadingIntervals.add(new UserReadingInterval(null, bookId, userId, start,
                        end));
                newRead += end - start + 1;
                start = userReadingIntervals.get(i).getStartPage();
                end = userReadingIntervals.get(i).getEndPage();
            } else {
                end = Math.max(end, userReadingIntervals.get(i).getEndPage());
            }
        }
        newRead += end - start + 1;
        newRead -= oldRead;
        newUserReadingIntervals.add(new UserReadingInterval(null, bookId, userId, start, end));
        deleteUserReadingIntervals(userReadingIntervals);
        saveUserReadingIntervals(newUserReadingIntervals);
        return newRead;
    }

    private void saveUserReadingInterval(UserReadingInterval userReadingInterval) {
        logger.debug("saveUserReadingInterval()>> userReadingInterval: {}", userReadingInterval);
        repository.save(userReadingInterval);
    }

    private void deleteUserReadingIntervals(List<UserReadingInterval> userReadingIntervals) {
        logger.debug("deleteUserReadingIntervals()>> userReadingIntervals: {}", userReadingIntervals);
        repository.deleteAll(userReadingIntervals);
    }

    private void saveUserReadingIntervals(List<UserReadingInterval> userReadingIntervals) {
        logger.debug("saveUserReadingIntervals()>> userReadingIntervals: {}", userReadingIntervals);
        repository.saveAll(userReadingIntervals);
    }
}
