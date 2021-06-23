package com.jamesngyz.demo.salarymanagement.user;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;

@RestController
public class UserController {

    @PostMapping(path = "/users/upload", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<String> uploadUsers(@RequestParam("file") MultipartFile file) {

        return ResponseEntity.created(URI.create("")).body("");
    }

}
