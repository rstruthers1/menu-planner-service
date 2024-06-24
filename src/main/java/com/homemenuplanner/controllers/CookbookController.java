package com.homemenuplanner.controllers;

import com.homemenuplanner.dtos.cookbook.CookbookRequest;
import com.homemenuplanner.dtos.cookbook.CookbookResponse;
import com.homemenuplanner.models.Cookbook;
import com.homemenuplanner.services.CookbookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/search")
    public ResponseEntity<Page<CookbookResponse>> searchCookbooksByName(@RequestParam String name, Pageable pageable) {
        Page<Cookbook> cookbooks = cookbookService.searchCookbooksByName(name, pageable);
        Page<CookbookResponse> cookbookResponses = cookbooks.map(cookbook -> new CookbookResponse(cookbook.getId(), cookbook.getName(), cookbook.getImageFileName()));
        return new ResponseEntity<>(cookbookResponses, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CookbookResponse> updateCookbook(@PathVariable Long id, @RequestBody CookbookRequest cookbookRequest) {
        CookbookResponse updatedCookbook = cookbookService.updateCookbook(id, cookbookRequest);
        return new ResponseEntity<>(updatedCookbook, HttpStatus.OK);
    }
}
