package com.walletmanagement.crypto.service;

import com.walletmanagement.crypto.entity.Asset;
import com.walletmanagement.crypto.repository.AssetRepository;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AssetService {

    private final AssetRepository assetRepository;

    @Inject
    public AssetService (final AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    

    public Optional<Asset> getAssetBySymbol(final String symbol) {
        return assetRepository.getAssetBySymbol(symbol);
    }

    public Asset saveAsset(final Asset asset) {
        return assetRepository.save(asset);
    }


}
