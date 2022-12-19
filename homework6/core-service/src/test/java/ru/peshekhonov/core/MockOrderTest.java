package ru.peshekhonov.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
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
import ru.peshekhonov.core.repositories.OrderItemRepository;
import ru.peshekhonov.core.repositories.OrderRepository;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MockOrderTest {

    @Autowired
    private MockMvc mvc;
    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private OrderItemRepository orderItemRepository;
    @MockBean
    private CartServiceIntegration cartService;

    @Test
    public void createOrderTest() throws Exception {
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

        Mockito.verify(orderRepository, Mockito.times(1)).save(ArgumentMatchers.any());
        Mockito.verify(orderItemRepository, Mockito.times(0)).save(ArgumentMatchers.any());
    }
}
