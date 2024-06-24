package com.homemenuplanner.services;

import com.homemenuplanner.dtos.cookbook.CookbookRequest;
import com.homemenuplanner.dtos.cookbook.CookbookResponse;
import com.homemenuplanner.exceptions.ResourceNotFoundException;
import com.homemenuplanner.models.Cookbook;
import com.homemenuplanner.repositories.CookbookRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CookbookService {
    private final CookbookRepository cookbookRepository;

    public CookbookService(CookbookRepository cookbookRepository) {
        this.cookbookRepository = cookbookRepository;
    }

    public CookbookResponse addCookbook(CookbookRequest cookbookRequest) {
        Cookbook cookbook = new Cookbook();
        return saveCookbookAndMapResponse(cookbookRequest, cookbook);
    }

    public Page<Cookbook> searchCookbooksByName(String name, Pageable pageable) {
        return cookbookRepository.findByNameContainingIgnoreCase(name, pageable);
    }

    public CookbookResponse updateCookbook(Long id, CookbookRequest cookbookRequest) {
        Cookbook cookbook = cookbookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cookbook not found with id " + id));
        return saveCookbookAndMapResponse(cookbookRequest, cookbook);
    }

    private CookbookResponse saveCookbookAndMapResponse(CookbookRequest cookbookRequest, Cookbook cookbook) {
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
