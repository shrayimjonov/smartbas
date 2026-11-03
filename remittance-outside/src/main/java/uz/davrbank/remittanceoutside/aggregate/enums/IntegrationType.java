/**
 * Author: Shukhratjon Rayimjonov
 * Date: 12.11.2025
 * Time: 12:31
 * Project: remittance-outside
 */
package uz.davrbank.remittanceoutside.aggregate.enums;

import lombok.Getter;

@Getter
public enum IntegrationType {

    LESKAR("leskarClient");

    private final String name;


    IntegrationType(String beanName) {
        this.name = beanName;
    }

}
