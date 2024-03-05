package com.octane.rrs.repository;

import com.octane.rrs.model.UserReadingInterval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserReadingIntervalRepository extends JpaRepository<UserReadingInterval,
        Integer> {

    List<UserReadingInterval> findAllByUserIdAndBookIdOrderByStartPage(int userId,int bookId);
}
