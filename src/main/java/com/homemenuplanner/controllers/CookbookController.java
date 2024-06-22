package com.homemenuplanner.controllers;

import com.homemenuplanner.dtos.cookbook.CookbookRequest;
import com.homemenuplanner.dtos.cookbook.CookbookResponse;
import com.homemenuplanner.services.CookbookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cookbooks")
public class CookbookController {

    private final CookbookService cookbookService;

    public CookbookController(CookbookService cookbookService) {
        this.cookbookService = cookbookService;
    }

    @PostMapping("")
    public ResponseEntity<CookbookResponse> addCookbook(@RequestBody CookbookRequest cookbookRequest) {
        CookbookResponse savedCookbook = cookbookService.addCookbook(cookbookRequest);
        return new ResponseEntity<>(savedCookbook, HttpStatus.CREATED);
    }
}
