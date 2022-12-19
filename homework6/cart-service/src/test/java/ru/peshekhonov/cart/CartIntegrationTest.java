package ru.peshekhonov.cart;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.peshekhonov.api.dto.CartDto;
import ru.peshekhonov.api.dto.ProductDto;
import ru.peshekhonov.cart.integrations.ProductServiceIntegration;
import ru.peshekhonov.cart.services.CartService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Objects;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CartIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private CartService cartService;
    @MockBean
    private ProductServiceIntegration productService;
    private static String uuid;
    private static String username;

    @BeforeAll
    public static void init() {
        uuid = UUID.randomUUID().toString();
        username = "Oleg";
    }

    @BeforeEach
    public void setUp() {
        ProductDto productWithId2 = new ProductDto();
        productWithId2.setId(2L);
        productWithId2.setTitle("бананы");
        productWithId2.setPrice(BigDecimal.valueOf(98.35));
        productWithId2.setCategoryTitle("пищевые продукты");

        ProductDto productWithId5 = new ProductDto();
        productWithId5.setId(5L);
        productWithId5.setTitle("кукла");
        productWithId5.setPrice(BigDecimal.valueOf(1500));
        productWithId5.setCategoryTitle("детские товары");

        Mockito.when(productService.findById(2L)).thenReturn(productWithId2);
        Mockito.when(productService.findById(5L)).thenReturn(productWithId5);

        restTemplate.getRestTemplate().setInterceptors(
                Collections.singletonList((request, body, execution) -> {
                    request.getHeaders()
                            .add("username", username);
                    return execution.execute(request, body);
                }));
    }

    @Test
    @Order(1)
    public void cartControllerGetEmptyCartTest() {
        cartService.getCurrentCart(getCartId(username, uuid)).clear();

        ResponseEntity<CartDto> entity = restTemplate.getForEntity("/api/v1/cart/" + uuid, CartDto.class);
        Assertions.assertSame(entity.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(0, Objects.requireNonNull(entity.getBody()).getItems().size());
        Assertions.assertNull(Objects.requireNonNull(entity.getBody()).getTotalCost());
    }

    @Test
    @Order(2)
    public void cartControllerAddProductToCartTest() {
        ResponseEntity<CartDto> entity;
        HttpStatus status;

        status = restTemplate.getForEntity("/api/v1/cart/" + uuid + "/add/{productId}", Object.class, 2)
                .getStatusCode();
        Assertions.assertSame(status, HttpStatus.OK);

        entity = restTemplate.getForEntity("/api/v1/cart/" + uuid, CartDto.class);
        Assertions.assertSame(entity.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(1, Objects.requireNonNull(entity.getBody()).getItems().size());

        status = restTemplate.getForEntity("/api/v1/cart/" + uuid + "/add/{productId}", Object.class, 5)
                .getStatusCode();
        Assertions.assertSame(entity.getStatusCode(), HttpStatus.OK);

        entity = restTemplate.getForEntity("/api/v1/cart/" + uuid, CartDto.class);
        Assertions.assertSame(entity.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(2, Objects.requireNonNull(entity.getBody()).getItems().size());

        status = restTemplate.getForEntity("/api/v1/cart/" + uuid + "/add/{productId}", Object.class, 5)
                .getStatusCode();
        Assertions.assertSame(status, HttpStatus.OK);

        entity = restTemplate.getForEntity("/api/v1/cart/" + uuid, CartDto.class);
        Assertions.assertSame(entity.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(2, Objects.requireNonNull(entity.getBody()).getItems().size());

        Assertions.assertEquals(BigDecimal.valueOf(98.35 + 2 * 1500), (BigDecimal) Objects.requireNonNull(entity.getBody()).getTotalCost());

        Assertions.assertEquals(2L, (long) Objects.requireNonNull(entity.getBody()).getItems().get(0).getProductId());
        Assertions.assertEquals("бананы", Objects.requireNonNull(entity.getBody()).getItems().get(0).getProductTitle());
        Assertions.assertEquals(1L, (int) Objects.requireNonNull(entity.getBody()).getItems().get(0).getQuantity());
        Assertions.assertEquals(BigDecimal.valueOf(98.35), (BigDecimal) Objects.requireNonNull(entity.getBody()).getItems().get(0).getPricePerProduct());
        Assertions.assertEquals(BigDecimal.valueOf(98.35), (BigDecimal) Objects.requireNonNull(entity.getBody()).getItems().get(0).getCost());

        Assertions.assertEquals(5L, (long) Objects.requireNonNull(entity.getBody()).getItems().get(1).getProductId());
        Assertions.assertEquals("кукла", Objects.requireNonNull(entity.getBody()).getItems().get(1).getProductTitle());
        Assertions.assertEquals(2L, (int) Objects.requireNonNull(entity.getBody()).getItems().get(1).getQuantity());
        Assertions.assertEquals(BigDecimal.valueOf(1500), (BigDecimal) Objects.requireNonNull(entity.getBody()).getItems().get(1).getPricePerProduct());
        Assertions.assertEquals(BigDecimal.valueOf(3000), (BigDecimal) Objects.requireNonNull(entity.getBody()).getItems().get(1).getCost());
    }

    @Test
    @Order(3)
    public void cartControllerSubtractProductFromCartTest() {
        ResponseEntity<CartDto> entity;
        HttpStatus status;

        status = restTemplate.getForEntity("/api/v1/cart/" + uuid + "/subtract/{productId}", Object.class, 5)
                .getStatusCode();
        Assertions.assertSame(status, HttpStatus.OK);

        entity = restTemplate.getForEntity("/api/v1/cart/" + uuid, CartDto.class);
        Assertions.assertSame(entity.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(2, Objects.requireNonNull(entity.getBody()).getItems().size());

        Assertions.assertEquals(BigDecimal.valueOf(98.35 + 1500), (BigDecimal) Objects.requireNonNull(entity.getBody()).getTotalCost());

        Assertions.assertEquals(2L, (long) Objects.requireNonNull(entity.getBody()).getItems().get(0).getProductId());
        Assertions.assertEquals("бананы", Objects.requireNonNull(entity.getBody()).getItems().get(0).getProductTitle());
        Assertions.assertEquals(1L, (int) Objects.requireNonNull(entity.getBody()).getItems().get(0).getQuantity());
        Assertions.assertEquals(BigDecimal.valueOf(98.35), (BigDecimal) Objects.requireNonNull(entity.getBody()).getItems().get(0).getPricePerProduct());
        Assertions.assertEquals(BigDecimal.valueOf(98.35), (BigDecimal) Objects.requireNonNull(entity.getBody()).getItems().get(0).getCost());

        Assertions.assertEquals(5L, (long) Objects.requireNonNull(entity.getBody()).getItems().get(1).getProductId());
        Assertions.assertEquals("кукла", Objects.requireNonNull(entity.getBody()).getItems().get(1).getProductTitle());
        Assertions.assertEquals(1L, (int) Objects.requireNonNull(entity.getBody()).getItems().get(1).getQuantity());
        Assertions.assertEquals(BigDecimal.valueOf(1500), (BigDecimal) Objects.requireNonNull(entity.getBody()).getItems().get(1).getPricePerProduct());
        Assertions.assertEquals(BigDecimal.valueOf(1500), (BigDecimal) Objects.requireNonNull(entity.getBody()).getItems().get(1).getCost());
    }

    @Test
    @Order(4)
    public void cartControllerRemoveProductFromCartTest() {
        ResponseEntity<CartDto> entity;
        HttpStatus status;

        status = restTemplate.getForEntity("/api/v1/cart/" + uuid + "/remove/{productId}", Object.class, 5)
                .getStatusCode();
        Assertions.assertSame(status, HttpStatus.OK);

        entity = restTemplate.getForEntity("/api/v1/cart/" + uuid, CartDto.class);
        Assertions.assertSame(entity.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(1, Objects.requireNonNull(entity.getBody()).getItems().size());

        Assertions.assertEquals(BigDecimal.valueOf(98.35), (BigDecimal) Objects.requireNonNull(entity.getBody()).getTotalCost());

        Assertions.assertEquals(2L, (long) Objects.requireNonNull(entity.getBody()).getItems().get(0).getProductId());
        Assertions.assertEquals("бананы", Objects.requireNonNull(entity.getBody()).getItems().get(0).getProductTitle());
        Assertions.assertEquals(1L, (int) Objects.requireNonNull(entity.getBody()).getItems().get(0).getQuantity());
        Assertions.assertEquals(BigDecimal.valueOf(98.35), (BigDecimal) Objects.requireNonNull(entity.getBody()).getItems().get(0).getPricePerProduct());
        Assertions.assertEquals(BigDecimal.valueOf(98.35), (BigDecimal) Objects.requireNonNull(entity.getBody()).getItems().get(0).getCost());
    }

    @Test
    @Order(5)
    public void cartControllerClearTest() {
        ResponseEntity<CartDto> entity;
        HttpStatus status;

        status = restTemplate.getForEntity("/api/v1/cart/" + uuid + "/clear", Object.class)
                .getStatusCode();
        Assertions.assertSame(status, HttpStatus.OK);

        entity = restTemplate.getForEntity("/api/v1/cart/" + uuid, CartDto.class);
        Assertions.assertSame(entity.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(0, Objects.requireNonNull(entity.getBody()).getItems().size());
        Assertions.assertNull(Objects.requireNonNull(entity.getBody()).getTotalCost());
    }

    private String getCartId(String username, String uuid) {
        if (username != null) {
            return username;
        }
        return uuid;
    }
}
