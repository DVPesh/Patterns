package ru.peshekhonov.cart;

import org.junit.jupiter.api.*;
import ru.peshekhonov.api.dto.ProductDto;
import ru.peshekhonov.cart.utils.Cart;

import java.math.BigDecimal;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CartUnitTest {

    private final static long ID_1 = 5L;
    private final static long ID_2 = 9L;
    private final static long ID_3 = 12L;
    private final static double PRICE_1 = 200.55;
    private final static double PRICE_2 = 2300.34;
    private final static double PRICE_3 = 1500;

    private static Cart cart;
    private static ProductDto product1;
    private static ProductDto product2;
    private static ProductDto product3;

    @BeforeAll
    public static void setUp() {
        cart = new Cart();
        product1 = new ProductDto();
        product1.setId(ID_1);
        product1.setTitle("масло");
        product1.setPrice(BigDecimal.valueOf(PRICE_1));
        product1.setCategoryTitle("продукты");

        product2 = new ProductDto();
        product2.setId(ID_2);
        product2.setTitle("наушники");
        product2.setPrice(BigDecimal.valueOf(PRICE_2));
        product2.setCategoryTitle("электроника");

        product3 = new ProductDto();
        product3.setId(ID_3);
        product3.setTitle("кукла");
        product3.setPrice(BigDecimal.valueOf(PRICE_3));
        product3.setCategoryTitle("детские товары");
    }

    @Test
    @Order(1)
    public void addProductTest() {
        cart.add(product1);
        cart.add(product1);
        cart.add(product1);
        Assertions.assertEquals(1, cart.getItems().size());
        BigDecimal value = BigDecimal.valueOf(PRICE_1).multiply(BigDecimal.valueOf(3));
        Assertions.assertEquals(value, cart.getTotalCost());
    }

    @Test
    @Order(2)
    public void subtractProductTest() {
        cart.subtract(product2);
        Assertions.assertEquals(1, cart.getItems().size());
        BigDecimal value = BigDecimal.valueOf(PRICE_1).multiply(BigDecimal.valueOf(3));
        Assertions.assertEquals(value, cart.getTotalCost());

        cart.subtract(product1);
        cart.subtract(product1);
        Assertions.assertEquals(1, cart.getItems().size());
        value = BigDecimal.valueOf(PRICE_1);
        Assertions.assertEquals(value, cart.getTotalCost());

        cart.subtract(product1);
        Assertions.assertEquals(0, cart.getItems().size());
        Assertions.assertNull(cart.getTotalCost());
    }

    @Test
    @Order(3)
    public void addThreeDifferentProductsTest() {
        cart.add(product1);
        cart.add(product2);
        cart.add(product3);
        cart.add(product1);
        cart.add(product2);
        cart.add(product1);
        cart.add(product2);
        cart.add(product2);
        cart.add(product2);
        cart.add(product3);
        Assertions.assertEquals(3, cart.getItems().size());
        BigDecimal value = BigDecimal.valueOf(PRICE_1).multiply(BigDecimal.valueOf(3))
                .add(BigDecimal.valueOf(PRICE_2).multiply(BigDecimal.valueOf(5)))
                .add(BigDecimal.valueOf(PRICE_3).multiply(BigDecimal.valueOf(2)));
        Assertions.assertEquals(value, cart.getTotalCost());
    }

    @Test
    @Order(4)
    public void removeItemTest() {
        cart.removeItem(ID_2);
        Assertions.assertEquals(2, cart.getItems().size());
        BigDecimal value = BigDecimal.valueOf(PRICE_1).multiply(BigDecimal.valueOf(3))
                .add(BigDecimal.valueOf(PRICE_3).multiply(BigDecimal.valueOf(2)));
        Assertions.assertEquals(value, cart.getTotalCost());
    }

    @Test
    @Order(5)
    public void clearTest() {
        cart.clear();
        Assertions.assertEquals(0, cart.getItems().size());
        Assertions.assertNull(cart.getTotalCost());
    }
}
