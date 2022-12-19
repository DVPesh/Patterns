package ru.peshekhonov.core.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Пешехонов Дмитрий Владимирович - core-service - сервис для работы с товарами," +
                                        " категориями товаров и заказами")
                                .version("1")
                );
    }
}
