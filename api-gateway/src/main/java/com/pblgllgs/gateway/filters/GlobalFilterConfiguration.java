package com.pblgllgs.gateway.filters;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class GlobalFilterConfiguration {

    @Bean
    @Order(1)
    public GlobalFilter secondFilter() {
        return ((exchange, chain) -> {
            log.info("Global second pre filter executed!!");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> log.info("Global third post filter is executed")));
        });
    }

    @Bean
    @Order(2)
    public GlobalFilter thirdFilter() {
        return ((exchange, chain) -> {
            log.info("Global third pre filter executed!!");
            return chain.filter(exchange).then(Mono.fromRunnable(() -> log.info("Global second post filter is executed")));
        });
    }

}
