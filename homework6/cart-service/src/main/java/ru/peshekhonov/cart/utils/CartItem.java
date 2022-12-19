package ru.peshekhonov.cart.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.peshekhonov.api.dto.ProductDto;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    private Long productId;
    private String productTitle;
    private int quantity;
    private BigDecimal pricePerProduct;
    private BigDecimal cost;

    public CartItem(ProductDto p) {
        this(p.getId(), p.getTitle(), 1, p.getPrice(), p.getPrice());
    }

    public void incrementQuantity() {
        quantity++;
        cost = cost.add(pricePerProduct);
    }

    public int decrementQuantity() {
        if (--quantity > 0) {
            cost = cost.subtract(pricePerProduct);
        } else {
            quantity = 0;
            cost = BigDecimal.ZERO;
        }
        return quantity;
    }

    public void increaseQuantityBy(int value) {
        if (value > 0) {
            quantity += value;
            cost = pricePerProduct.multiply(BigDecimal.valueOf(quantity));
        }
    }
}
