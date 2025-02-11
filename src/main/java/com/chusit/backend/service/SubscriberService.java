package com.chusit.backend.service;

import com.chusit.backend.repository.SubscriberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubscriberService {

    @Autowired
    private SubscriberRepository subscriberRepository;

    /**
     * Checks if a subscriber exists by ID.
     * @param id the ID of the subscriber.
     * @return true if the subscriber exists, false otherwise.
     */
    public boolean existsById(Long id) {
        return subscriberRepository.existsById(id);
    }
}