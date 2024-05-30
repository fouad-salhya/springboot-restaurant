package com.products.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.products.entities.FeedbackEntity;

public interface FeedbackRepository extends JpaRepository<FeedbackEntity, Long> {
    
}
