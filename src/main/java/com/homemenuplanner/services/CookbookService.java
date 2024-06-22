package com.homemenuplanner.services;

import com.homemenuplanner.dtos.cookbook.CookbookRequest;
import com.homemenuplanner.dtos.cookbook.CookbookResponse;
import com.homemenuplanner.models.Cookbook;
import com.homemenuplanner.repositories.CookbookRepository;
import org.springframework.stereotype.Service;

@Service
public class CookbookService {
    private final CookbookRepository cookbookRepository;

    public CookbookService(CookbookRepository cookbookRepository) {
        this.cookbookRepository = cookbookRepository;
    }

    public CookbookResponse addCookbook(CookbookRequest cookbookRequest) {
        Cookbook cookbook = new Cookbook();
        cookbook.setName(cookbookRequest.getName());
        cookbook.setImageFileName(cookbookRequest.getImageFileName());

        Cookbook savedCookbook = cookbookRepository.save(cookbook);

        CookbookResponse cookbookResponse = new CookbookResponse();
        cookbookResponse.setId(savedCookbook.getId());
        cookbookResponse.setName(savedCookbook.getName());
        cookbookResponse.setImageFileName(savedCookbook.getImageFileName());

        return cookbookResponse;
    }
}
