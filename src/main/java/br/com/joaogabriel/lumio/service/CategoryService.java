package br.com.joaogabriel.lumio.service;

import br.com.joaogabriel.lumio.model.dto.request.CategoryCreateRequest;
import br.com.joaogabriel.lumio.model.dto.response.CategoryResponse;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    CategoryResponse findById(final UUID id);

    CategoryResponse save(final CategoryCreateRequest categoryCreateRequest);

    List<CategoryResponse> findAll();

    void delete(final UUID id);
}
