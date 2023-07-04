package com.yagatalk.services;

import com.yagatalk.repositories.ContextRepository;
import org.springframework.stereotype.Service;

@Service
public class ContextService {
    private final ContextRepository contextRepository;

    public ContextService(ContextRepository contextRepository) {
        this.contextRepository = contextRepository;
    }


}
