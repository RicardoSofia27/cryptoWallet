package com.walletmanagement.crypto.service;

import com.walletmanagement.crypto.dto.AssetData;
import com.walletmanagement.crypto.dto.AssetSimulation;
import com.walletmanagement.crypto.dto.WalletSimulationDTO;
import com.walletmanagement.crypto.entity.Asset;
import com.walletmanagement.crypto.entity.Wallet;
import com.walletmanagement.crypto.entity.WalletAsset;
import com.walletmanagement.crypto.repository.WalletAssetRepository;
import com.walletmanagement.crypto.repository.WalletRepository;
import jakarta.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.stereotype.Service;
import jakarta.validation.ValidationException;

@Service
public class WalletService {

    private final WalletRepository walletRepository;
    private final CoinCapServiceWithReactive coinCapServiceWithReactive;
    private final AssetService assetService;
    private final WalletAssetRepository walletAssetRepository;

    @Inject
    public WalletService(final WalletRepository walletRepository,
        final CoinCapServiceWithReactive coinCapServiceWithReactive,
        final AssetService assetService, final WalletAssetRepository walletAssetRepository) {
        this.walletRepository = walletRepository;
        this.coinCapServiceWithReactive = coinCapServiceWithReactive;
        this.assetService = assetService;
        this.walletAssetRepository = walletAssetRepository;
    }

    public Wallet createWallet(final String email) throws ValidationException {

        if (!EmailValidator.getInstance().isValid(email)) {
            throw new ValidationException("email must not be null");
        }

        if (walletRepository.findByEmail(email).isPresent()) {
            throw new ValidationException("Wallet already exists");
        }
        return walletRepository.save(Wallet.builder().email(email).build());
    }

    public void addAssetToWallet(final String email, final AssetData assetData)
        throws NoSuchElementException {
        final var wallet = walletRepository.findByEmail(email).orElseThrow();
        final var asset = coinCapServiceWithReactive.getAssetBySymbol(
            assetData.getSymbol());
        if (asset.getPrice() != null) {

            final var assetDb = assetService.getAssetBySymbol(asset.getSymbol())
                .orElse(assetService.saveAsset(asset));
            final var walletAssetOptional = walletAssetRepository.findByWalletAndAsset(
                wallet, asset);
            if (walletAssetOptional.isPresent()) {
                final var walletAsset = walletAssetOptional.get();
                walletAsset.setPrice(new BigDecimal(assetData.getPriceUsd()));
                walletAsset.setQuantity(new BigDecimal(assetData.getQuantity()));
                walletAssetRepository.save(walletAsset);
            } else {
                walletAssetRepository.save(WalletAsset.builder().wallet(wallet).asset(assetDb)
                    .price(new BigDecimal(assetData.getPriceUsd()))
                    .quantity(new BigDecimal(assetData.getQuantity())).build());
            }
        }

    }

    public Wallet getWallet(final String email) {
        return walletRepository.findByEmail(email).orElseThrow();
    }

    public WalletSimulationDTO simulateWalletValue(final List<AssetData> assetList) {

        final var assetSimulationListist = assetList.stream().map(
            this::getAssetSimulation).toList();
        final var total = assetSimulationListist.stream().map(AssetSimulation::getValueMultiplied)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        final var max = assetSimulationListist.stream()
            .max(Comparator.comparing(AssetSimulation::getValueMultiplied))
            .orElse(AssetSimulation.builder()
                .build());
        final var min = assetSimulationListist.stream()
            .min(Comparator.comparing(AssetSimulation::getValueMultiplied))
            .orElse(AssetSimulation.builder()
                .build());
        return WalletSimulationDTO.builder()
            .total(total)
            .bestAsset(max.getSymbol())
            .bestPerformance(max.getPerformance())
            .worstAsset(min.getSymbol())
            .worstPerformance(min.getPerformance())
            .build();
    }

    private AssetSimulation getAssetSimulation(AssetData asset) {
        final var quantity = new BigDecimal(asset.getQuantity());
        final var assetServiceResponse = coinCapServiceWithReactive.getAssetBySymbol(asset.getSymbol());
        return AssetSimulation.builder().symbol(asset.getSymbol())
            .valueMultiplied(assetServiceResponse.getPrice().multiply(quantity))
            .performance(calculatePerformance(new BigDecimal(asset.getPriceUsd()),
                assetServiceResponse.getPrice())).build();
    }


    private BigDecimal calculatePerformance(final BigDecimal requestedValue,
        final BigDecimal serviceValue) {
        if (requestedValue == null || requestedValue.compareTo(BigDecimal.ZERO) == 0) {
            throw new IllegalArgumentException("Old value must be non-zero and not null");
        }

        return serviceValue.subtract(requestedValue)
            .divide(requestedValue, 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100));
    }
}
