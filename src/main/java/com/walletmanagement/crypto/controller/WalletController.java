package com.walletmanagement.crypto.controller;

import com.walletmanagement.crypto.dto.AssetData;
import com.walletmanagement.crypto.dto.WalletSimulationDTO;
import com.walletmanagement.crypto.entity.Wallet;
import com.walletmanagement.crypto.service.WalletService;
import jakarta.inject.Inject;
import jakarta.validation.ValidationException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("wallet")
public class WalletController {

    private static final Logger logger = Logger.getLogger("WalletController");

    private final WalletService walletService;

    @Inject
    public WalletController(final WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping
    @RequestMapping("create")
    public ResponseEntity<Wallet> createWallet(@RequestParam("email") final String email)
        throws ValidationException {
        logger.log(Level.INFO, "");
        return ResponseEntity.ok(walletService.createWallet(email));
    }

    @PostMapping
    @RequestMapping("addAsset")
    public void addAssetToWallet(@RequestParam("email") final String email,
        @RequestParam("asset") final AssetData asset) throws NoSuchElementException {
        walletService.addAssetToWallet(email, asset);
    }

    @GetMapping
    @RequestMapping("show")
    public ResponseEntity<Wallet> showWallet(@RequestParam("email") final String email) {
        return ResponseEntity.ok(walletService.getWallet(email));
    }

    @GetMapping
    @RequestMapping("simulateValue")
    public ResponseEntity<WalletSimulationDTO> simulateWalletValue(
        @RequestParam("assets") final List<AssetData> assetList) {
        return ResponseEntity.ok(walletService.simulateWalletValue(assetList));
    }
}
