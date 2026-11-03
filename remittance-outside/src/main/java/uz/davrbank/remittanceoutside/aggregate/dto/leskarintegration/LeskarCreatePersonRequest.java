/**
 * Author: Shukhratjon Rayimjonov
 * Date: 12.11.2025
 * Time: 12:31
 * Project: remittance-outside
 */
package uz.davrbank.remittanceoutside.aggregate.dto.leskarintegration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record LeskarCreatePersonRequest(
        @JsonProperty("address")
        Address address,
        @JsonProperty("occupation")
        String occupation,
        @JsonProperty("dob")
        String dob,
        @JsonProperty("additional")
        List<AdditionalItem> additional,
        @JsonProperty("citizenship")
        String citizenship,
        @JsonProperty("document")
        Document document,
        @JsonProperty("last_name")
        String lastName,
        @JsonProperty("middle_name")
        String middleName,
        @JsonProperty("first_name")
        String firstName
) {
}