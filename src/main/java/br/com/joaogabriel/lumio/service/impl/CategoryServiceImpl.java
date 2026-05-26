package br.com.joaogabriel.lumio.service.impl;

import br.com.joaogabriel.lumio.exception.ResourceNotFoundException;
import br.com.joaogabriel.lumio.mapper.CategoryMapper;
import br.com.joaogabriel.lumio.model.dto.request.CategoryCreateRequest;
import br.com.joaogabriel.lumio.model.dto.response.CategoryResponse;
import br.com.joaogabriel.lumio.model.entity.Category;
import br.com.joaogabriel.lumio.repository.CategoryRepository;
import br.com.joaogabriel.lumio.service.CategoryService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ApplicationScoped
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private static final Logger LOG = LoggerFactory.getLogger(CategoryServiceImpl.class);

    public CategoryServiceImpl(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public CategoryResponse findById(UUID id) {
        LOG.info("Finding Category by id: {}", id);
        return Optional.ofNullable(categoryRepository.findById(id))
                .map(this.categoryMapper::toCategoryResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + id));
    }

    @Override
    @Transactional
    public CategoryResponse save(CategoryCreateRequest categoryCreateRequest) {
        LOG.info("Saving Category: {}", categoryCreateRequest);
        Category category = this.categoryMapper.toCategory(categoryCreateRequest);
        this.categoryRepository.persist(category);
        return this.categoryMapper.toCategoryResponse(category);
    }

    @Override
    public List<CategoryResponse> findAll() {
        LOG.info("Finding all Categories");
        return this.categoryRepository.findAll()
                .stream()
                .map(this.categoryMapper::toCategoryResponse)
                .toList();
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        LOG.info("Deleting Category by id: {}", id);
        this.categoryRepository.deleteById(id);
    }
}
