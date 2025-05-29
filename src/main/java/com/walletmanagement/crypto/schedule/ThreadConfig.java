package com.walletmanagement.crypto.schedule;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ThreadConfig {

    @Bean
    public ExecutorService taskExecutor() {
        return Executors.newFixedThreadPool(3);
    }
}
