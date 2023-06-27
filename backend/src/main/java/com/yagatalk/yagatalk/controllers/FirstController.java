package com.yagatalk.yagatalk.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FirstController {

    @GetMapping("/health")
    public ResponseEntity getHealth(){
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
