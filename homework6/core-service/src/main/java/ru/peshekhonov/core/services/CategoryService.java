package ru.peshekhonov.core.services;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import ru.peshekhonov.core.entities.Category;
import ru.peshekhonov.core.repositories.CategoryRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "categoryCache")
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Cacheable(key = "#title")
    public Optional<Category> findByTitle(String title) {
        return categoryRepository.findByTitle(title);
    }

    @Cacheable
    public List<String> findAllTitles() {
        return categoryRepository.findAll().stream().map(Category::getTitle).toList();
    }
}
