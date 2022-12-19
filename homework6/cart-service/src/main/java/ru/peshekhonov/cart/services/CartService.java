package ru.peshekhonov.cart.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import ru.peshekhonov.api.dto.ProductDto;
import ru.peshekhonov.cart.integrations.ProductServiceIntegration;
import ru.peshekhonov.cart.utils.Cart;

import java.util.function.Consumer;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ProductServiceIntegration productService;
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${cart-service.cart-prefix}")
    private String cartPrefix;

    public Cart getCurrentCart(String cartId) {
        String id = getCartKey(cartId);
        if (Boolean.FALSE.equals(redisTemplate.hasKey(id))) {
            redisTemplate.opsForValue().set(id, new Cart());
        }
        return (Cart) redisTemplate.opsForValue().get(id);
    }

    private void saveCart(String cartId, Cart cart) {
        redisTemplate.opsForValue().set(getCartKey(cartId), cart);
    }

    public void addToCart(String cartId, Long productId) {
        Cart cart = getCurrentCart(cartId);
        if (cart.add(productId)) {
            saveCart(cartId, cart);
            return;
        }
        ProductDto p = productService.findById(productId);
        cart.addNewItem(p);
        saveCart(cartId, cart);
    }

    public void subtractFromCart(String cartId, Long productId) {
        execute(cartId, cart -> cart.subtract(productId));
    }

    public void removeFromCart(String cartId, Long productId) {
        execute(cartId, cart -> cart.removeItem(productId));
    }

    public void clear(String cartId) {
        execute(cartId, Cart::clear);
    }

    private void execute(String cartId, Consumer<Cart> operation) {
        Cart cart = getCurrentCart(cartId);
        operation.accept(cart);
        saveCart(cartId, cart);
    }

    public void merge(String userCartId, String guestCartId) {
        Cart userCart = getCurrentCart(userCartId);
        execute(guestCartId, userCart::merge);
        saveCart(userCartId, userCart);
    }

    private String getCartKey(String cartId) {
        return cartPrefix + cartId;
    }
}
