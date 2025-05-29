package com.walletmanagement.crypto.dto;

import java.math.BigDecimal;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class AssetSimulation {

    private String symbol;
    private BigDecimal valueMultiplied;
    private BigDecimal performance;


}
