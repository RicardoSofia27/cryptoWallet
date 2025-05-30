package com.walletmanagement.crypto.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

import com.walletmanagement.crypto.dto.AssetData;
import com.walletmanagement.crypto.dto.WalletSimulationDTO;
import com.walletmanagement.crypto.entity.Asset;
import com.walletmanagement.crypto.entity.Wallet;
import com.walletmanagement.crypto.service.WalletService;
import jakarta.validation.ValidationException;
import java.util.List;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;


@ExtendWith(MockitoExtension.class)
class WalletControllerTest {


    @Mock
    private WalletService walletServiceMock;

    @InjectMocks
    private WalletController walletController;

    @CsvSource("")
    @DisplayName("""
        GIVEN {0} email parameter
        WHEN walletController createWallet is invoked
        THEN controller should throw an internal server error
        """)
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"aaa@aaa.com"})
    void createWalletNullAndDuplicatedEmail(String email) {
        when(walletServiceMock.createWallet(any())).thenThrow(new ValidationException());
        ResponseEntity<Wallet> wallet = walletController.createWallet(email);
        assertEquals(INTERNAL_SERVER_ERROR, wallet.getStatusCode());
    }

    @DisplayName("""
        GIVEN new email parameter
        WHEN walletController createWallet is invoked
        THEN controller should return new wallet with email
        """)
    @Test
    void createWalletDuplicatedEmail() {
        when(walletServiceMock.createWallet(any())).thenReturn(Wallet.builder().email("aaa@gmail.com").build());
        ResponseEntity<Wallet> walletResponseEntity = walletController.createWallet("aaa@gmail.com");
        assertEquals(OK, walletResponseEntity.getStatusCode());
        assertEquals("aaa@gmail.com", walletResponseEntity.getBody().getEmail());

    }

    @DisplayName("""
        GIVEN existing asset and non existing wallet
        WHEN walletController addAssetToWallet is invoked
        THEN controller should throw an Exception
        """)
    @Test
    void addAssetToNonExistingWallet() {
        doThrow(NoSuchElementException.class).when(walletServiceMock).addAssetToWallet(any(), any());
        assertThrows(NoSuchElementException.class, () -> walletController.addAssetToWallet(null,
            new AssetData()));
    }


    @DisplayName("""
        GIVEN existing asset and existing wallet
        WHEN walletController addAssetToWallet is invoked
        THEN controller should call method addAssetToWallet 1 time
        """)
    @Test
    void addAssetToExistingWallet() {
        doNothing().when(walletServiceMock).addAssetToWallet(any(), any());
        walletController.addAssetToWallet(null, new AssetData());

        verify(walletServiceMock, times(1)).addAssetToWallet(any(), any());
    }

    @DisplayName("""
        GIVEN non existing wallet
        WHEN walletController showWallet is invoked
        THEN controller should throw NoSuchElementException
        """)
    @Test
    void showNullWallet() {
        when(walletServiceMock.getWallet(any())).thenThrow(NoSuchElementException.class);
        assertThrows(NoSuchElementException.class, () -> walletController.showWallet("aa"));
    }


    @DisplayName("""
        GIVEN existing wallet
        WHEN walletController showWallet is invoked
        THEN controller should return wallet
        """)
    @Test
    void showWallet() {
        when(walletServiceMock.getWallet(any())).thenReturn(Wallet.builder().email("aa").build());
        final var walletResponseEntity = walletController.showWallet("aa");
        assertEquals("aa", walletResponseEntity.getBody().getEmail());
    }


    @DisplayName("""
        """)
    @Test
    void simulateWalletValue() {
        when(walletServiceMock.simulateWalletValue(any())).thenReturn(WalletSimulationDTO.builder()
            .build());
        ResponseEntity<WalletSimulationDTO> walletSimulationDTOResponseEntity = walletController.simulateWalletValue(
            List.of(new AssetData()));
        assertEquals(OK, walletSimulationDTOResponseEntity.getStatusCode());
    }

}
