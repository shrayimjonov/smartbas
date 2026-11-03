package uz.company.leskarserver.leskarintegration;

import java.util.List;

public record RequiredFields(
	List<String> beneficiary,
	List<String> originator
) {
}