package com.walletmanagement.crypto.repository;

import com.walletmanagement.crypto.entity.Asset;
import com.walletmanagement.crypto.entity.Wallet;
import com.walletmanagement.crypto.entity.WalletAsset;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WalletAssetRepository extends JpaRepository<WalletAsset, Long> {


    Optional<WalletAsset> findByWalletAndAsset(final Wallet wallet, final Asset asset);

}
