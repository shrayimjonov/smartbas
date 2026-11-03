package uz.davrbank.userinfobase.entity.nci;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "person_details")
public class PersonDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String pinfl;
    private String name;
    private String branch;
    private String contractNum;
    private String dateRegistration;
    private String numberRegistrationDoc;
    private String codeBank;
    private String account;
    private String postAddress;
    private String kodErr;
    private String fileName;
    private String state;
    private String codeCitizenship;
    private String codeCapacity;
    private String typeDocument;
    private String passportDateExpiration;
    private String inps;
    private String dateOpenNibbd;
    private String dateCloseNibbd;
    private String codeSubject;
    private String clientId;
    private String passportType;
    private String passportPlaceRegistration;
    private String birthPlace;
    private String passportSerial;
    private String passportNumber;
    private String phoneHome;
    private String passportDateRegistration;
    private String phoneMobile;
    private String idNibbd;
    private String birthday;
    private String codeResident;
    private String codeAdrRegion;
    private String codeAdrDistr;
    private String codeGender;
    private String family;
    private String firstName;
    private String patronymic;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "person_loan_mapping",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "loan_id")
    )
    private Set<LoanInfoEntity> loanInfoEntities = new HashSet<>();
}
