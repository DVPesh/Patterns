package ru.peshekhonov.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import ru.peshekhonov.api.dto.CartDto;
import ru.peshekhonov.api.dto.CartItemDto;
import ru.peshekhonov.api.dto.OrderDetailsDto;
import ru.peshekhonov.core.integrations.CartServiceIntegration;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class OrderTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private CartServiceIntegration cartService;

    @Test
    public void orderTest() throws Exception {
        List<CartItemDto> cartItems = List.of(new CartItemDto(1L, "молочные продукты", 2,
                        BigDecimal.valueOf(110.5), BigDecimal.valueOf(110.5).multiply(BigDecimal.valueOf(2))),
                new CartItemDto(2L, "молочные продукты", 3,
                        BigDecimal.valueOf(82.32), BigDecimal.valueOf(82.32).multiply(BigDecimal.valueOf(3)))
        );

        CartDto cart = new CartDto();
        cart.setItems(cartItems);
        cart.setTotalCost(cartItems.get(0).getCost().add(cartItems.get(1).getCost()));
        Mockito.when(cartService.getCurrentCart("Ann")).thenReturn(cart);

        OrderDetailsDto orderDetailsDto = new OrderDetailsDto();
        orderDetailsDto.setAddress("г. Новосибирск ул. Новаторов дом 34 кв 3");
        orderDetailsDto.setPhone("+7895421");

        mvc.perform(
                        post("/api/v1/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(new ObjectMapper().writeValueAsString(orderDetailsDto))
                                .header("username", "Ann")
                )
                .andDo(print())
                .andExpect(status().isCreated());

        mvc.perform(
                        get("/api/v1/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("username", "Ann")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is("Ann")))
                .andExpect(jsonPath("$[0].items", hasSize(2)))
                .andExpect(jsonPath("$[0].items[0].productId", is(1)))
                .andExpect(jsonPath("$[0].items[0].productTitle", is("кефир")))
                .andExpect(jsonPath("$[0].items[0].quantity", is(2)))
                .andExpect(jsonPath("$[0].items[0].pricePerProduct", is(110.5)))
                .andExpect(jsonPath("$[0].items[0].cost", is(221.0)))
                .andExpect(jsonPath("$[0].items[1].productId", is(2)))
                .andExpect(jsonPath("$[0].items[1].productTitle", is("молоко")))
                .andExpect(jsonPath("$[0].items[1].quantity", is(3)))
                .andExpect(jsonPath("$[0].items[1].pricePerProduct", is(82.32)))
                .andExpect(jsonPath("$[0].items[1].cost", is(246.96)));

        mvc.perform(
                        get("/api/v1/orders")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("username", "Peter")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
