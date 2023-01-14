package ru.peshekhonov.mvc.products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = """
            select * from products p
            where (:title is null or p.title like :title)
            and (:min is null or p.cost >= :min)
            and (:max is null or p.cost <= :max)
                """, nativeQuery = true)
    List<Product> productsByFilter(String title, BigDecimal min, BigDecimal max);
}
