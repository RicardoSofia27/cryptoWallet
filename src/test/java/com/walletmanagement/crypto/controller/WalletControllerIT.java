package com.walletmanagement.crypto.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.walletmanagement.crypto.dto.AssetData;
import jakarta.inject.Inject;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class WalletControllerIT {


    @Inject
    private MockMvc mockMvc;

    @Test
    void testWalletControllerCreate() throws Exception {
        mockMvc.perform(post("/wallet/create")
                .param("email", "aa@aa.com"))
            .andExpect(status().isOk());
    }

    @Test
    void testWalletControllerCreateError() throws Exception {
        mockMvc.perform(post("/wallet/create")
                .param("email", "aa"))
            .andExpect(status().isOk());
    }

    @Test
    void testWalletControllerAddAsset() throws Exception {
        mockMvc.perform(post("/wallet/addAsset")
                .param("email", "aa")
                .param("asset", new AssetData().toString()))
            .andExpect(status().isOk());
    }

    @Test
    void testWalletControllerShowWallet() throws Exception {
        mockMvc.perform(get("/wallet/show")
                .param("email", "email@email.com"))
            .andExpect(status().isOk());
    }

    @Test
    void testWalletControllerSimulateWallet() throws Exception {
        mockMvc.perform(get("/wallet/simulateValue")
                .param("assets", List.of(AssetData.builder().symbol("ETC").build(),
                    AssetData.builder().symbol("BTC").build()).toString()))
            .andExpect(status().isOk());
    }
}
