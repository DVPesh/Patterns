package ru.peshekhonov.core;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import ru.peshekhonov.core.entities.Category;
import ru.peshekhonov.core.repositories.CategoryRepository;

import java.util.Collections;
import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void findAllCategoriesTest() {
        List<Category> categories = categoryRepository.findAll();
        Assertions.assertThat(categories)
                .isNotNull()
                .isNotEmpty()
                .hasSize(3)
                .element(2)
                .matches(c -> c.getTitle().equals("детские товары") && c.getId() == 3);
    }

    @Test
    void addCategoryTest() {
        Category category = new Category();
        category.setTitle("инструменты");
        category.setProducts(Collections.emptyList());
        entityManager.persistAndFlush(category);

        List<Category> categories = categoryRepository.findAll();
        Assertions.assertThat(categories)
                .isNotNull()
                .isNotEmpty()
                .hasSize(4)
                .element(3)
                .matches(c -> c.getTitle().equals("инструменты") && c.getId() == 4
                        , "id=4, title=\"инструменты\"");
    }
}
