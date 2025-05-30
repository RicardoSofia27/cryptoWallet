package com.walletmanagement.crypto.repository;

import com.walletmanagement.crypto.entity.Asset;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

    Optional<Asset> getAssetBySymbol(final String symbol);
}
