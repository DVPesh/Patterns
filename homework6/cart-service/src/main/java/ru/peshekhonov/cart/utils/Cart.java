package ru.peshekhonov.cart.utils;

import lombok.Data;
import ru.peshekhonov.api.dto.ProductDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Data
public class Cart {

    private List<CartItem> items;
    private BigDecimal totalCost;

    public Cart() {
        this.items = new ArrayList<>();
    }

    public void add(ProductDto p) {
        if (add(p.getId())) {
            return;
        }
        items.add(new CartItem(p));
        recalculate();
    }

    public void addNewItem(ProductDto p) {
        items.add(new CartItem(p));
        recalculate();
    }

    public boolean add(Long id) {
        for (CartItem item : items) {
            if (item.getProductId().equals(id)) {
                item.incrementQuantity();
                recalculate();
                return true;
            }
        }
        return false;
    }

    public void subtract(ProductDto p) {
        Iterator<CartItem> iterator = items.iterator();
        while (iterator.hasNext()) {
            CartItem item = iterator.next();
            if (item.getProductId().equals(p.getId())) {
                if (item.decrementQuantity() < 1) {
                    iterator.remove();
                }
                recalculate();
                return;
            }
        }
    }

    public void subtract(Long productId) {
        Iterator<CartItem> iterator = items.iterator();
        while (iterator.hasNext()) {
            CartItem item = iterator.next();
            if (item.getProductId().equals(productId)) {
                if (item.decrementQuantity() < 1) {
                    iterator.remove();
                }
                recalculate();
                return;
            }
        }
    }

    public void clear() {
        items.clear();
        totalCost = null;
    }

    public void removeItem(Long productId) {
        if (items.removeIf((item) -> item.getProductId().equals(productId))) {
            recalculate();
        }
    }

    private void recalculate() {
        totalCost = BigDecimal.ZERO;
        items.forEach(i -> totalCost = totalCost.add(i.getCost()));
        if (totalCost.compareTo(BigDecimal.ZERO) == 0) {
            totalCost = null;
        }
    }

    public void merge(Cart cart) {
        boolean merged;
        for (CartItem cartItem : cart.items) {
            merged = false;
            for (CartItem item : items) {
                if (item.getProductId().equals(cartItem.getProductId())) {
                    item.increaseQuantityBy(cartItem.getQuantity());
                    merged = true;
                    break;
                }
            }
            if (!merged) {
                items.add(cartItem);
            }
        }
        recalculate();
        cart.clear();
    }
}
