package uz.company.leskarserver.leskarintegration;

public record Address(
	String country,
	String city,
	String postcode,
	String state,
	String line2,
	String line1
) {
}
