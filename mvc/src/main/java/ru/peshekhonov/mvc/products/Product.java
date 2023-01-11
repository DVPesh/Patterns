package ru.peshekhonov.mvc.products;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "значение не может быть пустым")
    @Column(nullable = false, unique = true)
    private String title;

    @Positive(message = "значение должно быть больше нуля")
    @NotNull(message = "значение должно быть задано")
    @Digits(integer = 6, fraction = 2, message = "неверное значение")
    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal cost;

    @Future(message = "товар с истёкшим сроком годности")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column
    private LocalDate expirationDate;

    @Email(message = "неверный Email")
    @Column
    private String supplierEmail;

    @Min(value = 1, message = "товар должен быть в наличии")
    @Column(nullable = false)
    private long quantity;

    public Product(String title, BigDecimal cost, LocalDate expirationDate, String supplierEmail, long quantity) {
        this.title = title;
        this.cost = cost;
        this.expirationDate = expirationDate;
        this.supplierEmail = supplierEmail;
        this.quantity = quantity;
    }
}
