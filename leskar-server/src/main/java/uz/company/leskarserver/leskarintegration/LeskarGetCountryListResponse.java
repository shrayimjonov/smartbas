package uz.company.leskarserver.leskarintegration;

import java.util.List;

public record LeskarGetCountryListResponse(
	String country,
	List<AvailableTypesItem> availableTypes
) {
}