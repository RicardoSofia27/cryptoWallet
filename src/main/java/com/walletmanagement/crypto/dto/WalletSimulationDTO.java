package com.walletmanagement.crypto.dto;

import java.math.BigDecimal;
import lombok.Builder;

@Builder
public class WalletSimulationDTO {

    private BigDecimal total;
    private String bestAsset;
    private BigDecimal bestPerformance;
    private String worstAsset;
    private BigDecimal worstPerformance;

}
