package com.walletmanagement.crypto.repository;

import com.walletmanagement.crypto.entity.Asset;
import java.util.Optional;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends ReactiveCrudRepository<Asset, Integer> {

    Optional<Asset> getAssetBySymbol(final String symbol);
}
