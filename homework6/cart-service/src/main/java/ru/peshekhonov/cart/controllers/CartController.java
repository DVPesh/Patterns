package ru.peshekhonov.cart.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.peshekhonov.api.dto.CartDto;
import ru.peshekhonov.api.dto.StringResponse;
import ru.peshekhonov.cart.mappers.CartDtoMapper;
import ru.peshekhonov.cart.services.CartService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final CartDtoMapper cartDtoMapper;

    @GetMapping("/new_uuid")
    public StringResponse generateUuid() {
        return new StringResponse(UUID.randomUUID().toString());
    }

    @GetMapping("/{uuid}")
    public CartDto getCurrentCart(@RequestHeader(name = "username", required = false) String username,
                                  @PathVariable String uuid) {
        return cartDtoMapper.map(cartService.getCurrentCart(getCartId(username, uuid)));
    }

    @GetMapping("/{uuid}/add/{productId}")
    public void addProductToCart(@RequestHeader(name = "username", required = false) String username,
                                 @PathVariable String uuid,
                                 @PathVariable Long productId) {
        cartService.addToCart(getCartId(username, uuid), productId);
    }

    @GetMapping("/{uuid}/subtract/{productId}")
    public void subtractProductFromCart(@RequestHeader(name = "username", required = false) String username,
                                        @PathVariable String uuid,
                                        @PathVariable Long productId) {
        cartService.subtractFromCart(getCartId(username, uuid), productId);
    }

    @GetMapping("/{uuid}/remove/{productId}")
    public void removeProductFromCart(@RequestHeader(name = "username", required = false) String username,
                                      @PathVariable String uuid,
                                      @PathVariable Long productId) {
        cartService.removeFromCart(getCartId(username, uuid), productId);
    }

    @GetMapping("/{uuid}/clear")
    public void clear(@RequestHeader(name = "username", required = false) String username,
                      @PathVariable String uuid) {
        cartService.clear(getCartId(username, uuid));
    }

    @GetMapping("/{uuid}/merge")
    public void merge(@RequestHeader(name = "username", required = false) String username,
                      @PathVariable String uuid) {
        cartService.merge(getCartId(username, null), getCartId(null, uuid));
    }

    private String getCartId(String username, String uuid) {
        if (username != null) {
            return username;
        }
        return uuid;
    }
}
