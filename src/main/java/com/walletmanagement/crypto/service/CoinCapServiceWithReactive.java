package com.walletmanagement.crypto.service;

import com.walletmanagement.crypto.dto.AssetResponse;
import com.walletmanagement.crypto.entity.Asset;
import com.walletmanagement.crypto.repository.AssetRepository;
import jakarta.inject.Inject;
import java.math.BigDecimal;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CoinCapServiceWithReactive {

    private final AssetRepository assetRepository;

    @Inject
    public CoinCapServiceWithReactive(final AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    private final WebClient webClient = WebClient.builder()
        .baseUrl("https://rest.coincap.io/v3")
        .build();

    public Asset getAssetBySymbolAndSaveIt(final String symbol) {
        return webClient.get()
            .uri("/assets/{slug}", symbol)
            .header("Authorization",
                "Bearer a1457768ddf72e57d22b8045b13b6c522e59fce103897c44b4dedd88ec0f14fe")
            .retrieve()
            .bodyToMono(AssetResponse.class)
            .map(response -> {
                final var data = response.getData();

                return assetRepository.save(Asset.builder().symbol(data.getSymbol())
                    .price(new BigDecimal(data.getPriceUsd())).build());
            }).block();

    }

    public Asset getAssetBySymbol(final String symbol) {
        return webClient.get()
            .uri("/assets/{slug}", symbol)
            .header("Authorization",
                "Bearer a1457768ddf72e57d22b8045b13b6c522e59fce103897c44b4dedd88ec0f14fe")
            .retrieve()
            .bodyToMono(AssetResponse.class)
            .map(response -> {
                final var data = response.getData();
                return Asset.builder().symbol(data.getSymbol())
                    .price(new BigDecimal(data.getPriceUsd())).build();
            }).block();
    }
}
