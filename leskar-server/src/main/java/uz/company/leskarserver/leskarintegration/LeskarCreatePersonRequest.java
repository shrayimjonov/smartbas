package uz.company.leskarserver.leskarintegration;

import java.util.List;

public record LeskarCreatePersonRequest(
	Address address,
	String occupation,
	String dob,
	List<AdditionalItem> additional,
	String citizenship,
	Document document,
	String lastName,
	String middleName,
	String firstName
) {
}