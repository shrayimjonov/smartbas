package uz.davrbank.remittanceoutside.aggregate.enums;

import lombok.Getter;

/**
 * Author: Shukhratjon Rayimjonov
 * Date: 17.11.2025
 * Time: 11:30
 * Project: remittance-outside
 */

@Getter
public enum ExchangeType {

    INBOUND,
    OUTBOUND,
    THIRD_PARTY_REQUEST,
    THIRD_PARTY_RESPONSE;
}