package com.walletmanagement.crypto.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssetData {
    private String id;
    private String symbol;
    private String priceUsd;
    private String quantity;

}
