package com.walletmanagement.crypto.schedule;

import com.walletmanagement.crypto.repository.AssetRepository;
import com.walletmanagement.crypto.service.CoinCapServiceWithReactive;
import jakarta.inject.Inject;
import java.util.concurrent.ExecutorService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class AssetPriceScheduled {


    private final ExecutorService executorService;
    private final AssetRepository assetRepository;
    private final CoinCapServiceWithReactive coinCapServiceWithReactive;

    @Inject
    public AssetPriceScheduled(final ExecutorService executorService,
                            final AssetRepository assetRepository,
                            final CoinCapServiceWithReactive coinCapServiceWithReactive) {
        this.executorService = executorService;
        this.assetRepository = assetRepository;
        this.coinCapServiceWithReactive = coinCapServiceWithReactive;
    }

    @Scheduled(cron = "${crypto.asset.cron1}")
    public void updateAssetValue() {
        final var assetList = assetRepository.findAll();
        assetList.forEach(asset -> executorService.submit(
            () -> coinCapServiceWithReactive.getAssetBySymbolAndSaveIt(asset.getSymbol())));
    }

}
