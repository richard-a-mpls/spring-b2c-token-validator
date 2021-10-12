package com.rca.authorization.azureb2ctokenvalidator.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Wakeup {

    @RequestMapping("/wakeup")
    public ResponseEntity<Void> wakeup() {
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}