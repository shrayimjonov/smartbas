package uz.davrbank.userinfobase.service.nci;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import uz.davrbank.userinfobase.dto.pagination.PaginationRequest;
import uz.davrbank.userinfobase.dto.pagination.PaginationResponse;
import uz.davrbank.userinfobase.dto.request.ResultResponse;
import uz.davrbank.userinfobase.dto.request.nci.LoanByPinflRequest;
import uz.davrbank.userinfobase.dto.request.nci.LoanInfoDto;
import uz.davrbank.userinfobase.dto.request.nci.LoanRequest;
import uz.davrbank.userinfobase.dto.request.nci.PersonDetailsRequest;
import uz.davrbank.userinfobase.dto.response.ResultData;
import uz.davrbank.userinfobase.dto.response.nci.LoanByPinflResponse;
import uz.davrbank.userinfobase.dto.response.nci.LoanResponse;
import uz.davrbank.userinfobase.dto.response.nci.PersonDetailsResponse;
import uz.davrbank.userinfobase.entity.nci.LoanInfoEntity;
import uz.davrbank.userinfobase.entity.nci.PersonDetails;
import uz.davrbank.userinfobase.enums.ResponseStatus;
import uz.davrbank.userinfobase.exceptions.NciException;
import uz.davrbank.userinfobase.exceptions.PersonDetailsResponseNotFoundException;
import uz.davrbank.userinfobase.feign.nci.UserCreditInfoFeign;
import uz.davrbank.userinfobase.repository.nci.LoanInfoRepository;
import uz.davrbank.userinfobase.repository.nci.PersonalDetailsRepository;
import uz.davrbank.userinfobase.repository.nci.PersonalDetailsSpecification;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@Slf4j
@RequiredArgsConstructor
public class PersonDetailsServiceImpl implements PersonDetailsService {

    private final UserCreditInfoFeign feign;
    private final PersonalDetailsRepository personalDetailsRepository;
    private final LoanInfoRepository loanInfoRepository;

    @Override
    public PersonDetailsResponse getUserDetails(PersonDetailsRequest request) {
        try {
            Optional<PersonDetails> existingUser = getPersonDetails(request.pinfl());
            if (existingUser.isEmpty()) {
                var response = feign.getUserDetails(request);
                saveUserDetails(response);
                return response;
            }
            return mapUserDetailsEntityToResponse(existingUser.get());
        } catch (NciException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<PersonDetailsResponse> processPinflsFromCsv(MultipartFile file) {
        List<PersonDetailsResponse> responses = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String pinfl = line.trim().replaceAll("[^0-9]", "");
                if (pinfl.length() != 14) {
                    log.warn("Skipping PINFL with invalid format (length {})", pinfl.length());
                    continue;
                }
                Optional<PersonDetails> existingUser = getPersonDetails(pinfl);
                if (existingUser.isPresent()) {
                    PersonDetailsResponse response = mapUserDetailsEntityToResponse(existingUser.get());
                    responses.add(response);
                    log.debug("Person details already cached, skipping remote lookup");
                    continue;
                }
                PersonDetailsRequest request = new PersonDetailsRequest(pinfl);
                try {
                    PersonDetailsResponse response = feign.getUserDetails(request);
                    saveUserDetails(response);
                    responses.add(response);
                    log.debug("Person details fetched and saved");
                } catch (Exception e) {
                    log.error("Failed to fetch person details: {}", e.getMessage());
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error reading CSV file: " + e.getMessage(), e);
        }
        return responses;
    }


    public Optional<PersonDetails> getPersonDetails(String pinfl) {
        return personalDetailsRepository.findByPinfl(pinfl);
    }

    public void saveUserDetails(PersonDetailsResponse response) {
        if (response != null) {
            List<PersonDetailsResponse.Result> personInfoList = response.result();
            if (personInfoList != null) {
                if (!personInfoList.isEmpty()) {
                    savePersonToDatabase(personInfoList.get(0));
                }
            }
        }
    }

    public void savePersonToDatabase(PersonDetailsResponse.Result result) {

        PersonDetails personDetails = PersonDetails.builder()
                .pinfl(result.pinfl())
                .name(result.name())
                .branch(result.branch())
                .contractNum(result.contractNum())
                .dateRegistration(result.dateRegistration())
                .numberRegistrationDoc(result.numberRegistrationDoc())
                .codeBank(result.codeBank())
                .account(result.account())
                .postAddress(result.postAddress())
                .kodErr(result.kodErr())
                .fileName(result.fileName())
                .state(result.state())
                .codeCitizenship(result.codeCitizenship())
                .codeCapacity(result.codeCapacity())
                .typeDocument(result.typeDocument())
                .passportDateExpiration(result.passportDateExpiration())
                .inps(result.inps())
                .dateOpenNibbd(result.dateOpenNibbd())
                .dateCloseNibbd(result.dateCloseNibbd())
                .codeSubject(result.codeSubject())
                .clientId(result.clientId())
                .passportType(result.passportType())
                .passportPlaceRegistration(result.passportPlaceRegistration())
                .birthPlace(result.birthPlace())
                .passportSerial(result.passportSerial())
                .passportNumber(result.passportNumber())
                .phoneHome(result.phoneHome())
                .passportDateRegistration(result.passportDateRegistration())
                .phoneMobile(result.phoneMobile())
                .idNibbd(result.idNibbd())
                .birthday(result.birthday())
                .codeResident(result.codeResident())
                .codeAdrRegion(result.codeAdrRegion())
                .codeAdrDistr(result.codeAdrDistr())
                .codeGender(result.codeGender())
                .family(result.family())
                .firstName(result.firstName())
                .patronymic(result.patronymic())
                .build();

        personalDetailsRepository.save(personDetails);
    }

    public PersonDetailsResponse mapUserDetailsEntityToResponse(PersonDetails p) {

        PersonDetailsResponse.Result result = new PersonDetailsResponse.Result(
                p.getId() != null ? p.getId().toString() : null, // id
                p.getName(), // name
                p.getBranch(), // branch
                null, // signVIP
                null, // zipCode
                p.getContractNum(), // contractNum
                p.getDateRegistration(), // dateRegistration
                p.getNumberRegistrationDoc(), // numberRegistrationDoc
                null, // codeTaxOrg
                null, // numberTaxRegistration
                null, // codeSector
                null, // codeOrganDirect
                null, // codeClassCredit
                p.getCodeBank(), // codeBank
                p.getAccount(), // account
                p.getPostAddress(), // postAddress
                p.getKodErr(), // kodErr
                p.getFileName(), // fileName
                p.getState(), // state
                null, // codePlaceRegist
                p.getCodeCitizenship(), // codeCitizenship
                p.getCodeCapacity(), // codeCapacity
                null, // capacityStatusDate
                null, // capacityStatusPlace
                null, // numCertifCapacity
                null, // emailAddress
                null, // pensionSertifSerial
                null, // codeNation
                null, // codeBirthRegion
                null, // codeBirthDistr
                p.getTypeDocument(), // typeDocument
                p.getPassportDateExpiration(), // passportDateExpiration
                p.getInps(), // inps
                p.getDateOpenNibbd(), // dateOpenNibbd
                p.getDateCloseNibbd(), // dateCloseNibbd
                null, // closeType
                null, // closeDocN
                null, // closeDocD
                null, // drivePermitSer
                null, // drivePermitNum
                null, // drivePermitRegD
                null, // drivePermitExpD
                null, // drivePermitPlace
                null, // agreement
                p.getCodeSubject(), // codeSubject
                p.getClientId(), // clientId
                p.getPassportType(), // passportType
                p.getPassportPlaceRegistration(), // passportPlaceRegistration
                p.getBirthPlace(), // birthPlace
                p.getPassportSerial(), // passportSerial
                p.getPassportNumber(), // passportNumber
                p.getPhoneHome(), // phoneHome
                p.getPassportDateRegistration(), // passportDateRegistration
                p.getPhoneMobile(), // phoneMobile
                p.getIdNibbd(), // idNibbd
                p.getBirthday(), // birthday
                p.getCodeResident(), // codeResident
                p.getCodeAdrRegion(), // codeAdrRegion
                p.getCodeAdrDistr(), // codeAdrDistr
                p.getCodeGender(), // codeGender
                p.getPinfl(), // pinfl
                p.getFamily(), // family
                p.getFirstName(), // firstName
                p.getPatronymic() // patronymic
        );

        return new PersonDetailsResponse(
                200,
                true,
                "Found in database",
                List.of(result),
                System.currentTimeMillis()
        );
    }


    @Override
    public LoanByPinflResponse getLoanByPinfl(LoanByPinflRequest request) {
        try {
            var userDetails = getPersonDetails(request.pinfl());
            if (userDetails.isEmpty()) {
                throw new PersonDetailsResponseNotFoundException(
                        "User details validation failed for PINFL: " + request.pinfl()
                );
            }
            LoanByPinflResponse response = feign.getLoanByPinfl(request);
            if (response == null || response.result() == null) {
                throw new NciException(
                        "Failed to fetch loan details for PINFL: " + request.pinfl()
                );
            }
            response.result().forEach(r -> processLoanResult(userDetails.get(), r));
            return response;
        } catch (NciException e) {
            throw new PersonDetailsResponseNotFoundException(e.getMessage());
        }
    }

    @Override
    public ResultData<PaginationResponse<ResultResponse>> getAllUsers(String pinfl, String loanId, PaginationRequest paginationRequest) {
        try {
            Pageable pageable = paginationRequest.toPageable();
            Specification<PersonDetails> specification = PersonalDetailsSpecification.filterByPinflAndLoanIdAndOrderByIdDesc(pinfl, loanId);
            Page<PersonDetails> departmentPage = personalDetailsRepository.findAll(specification, pageable);
            PaginationResponse<ResultResponse> response = PaginationResponse.fromPage(
                    departmentPage,
                    ResultResponse::fromEntity
            );
            return new ResultData<>(response);
        } catch (PersonDetailsResponseNotFoundException e) {
            return new ResultData<>(false, ResponseStatus.FAILED, e.getMessage());
        }
    }


    @Override
    public ResultData<ResultResponse> findById(Long id) {
        try {
            PersonDetails personDetails = personalDetailsRepository.findById(id)
                    .orElseThrow(() -> new PersonDetailsResponseNotFoundException("PersonDetails not found with id : " + id));
            return new ResultData<>(ResultResponse.fromEntity(personDetails));
        } catch (PersonDetailsResponseNotFoundException e) {
            return new ResultData<>(false, ResponseStatus.FAILED, e.getMessage());
        }
    }

    @Override
    public ResultData<List<LoanInfoDto>> getUserLoans(String pinfl) {
        Optional<PersonDetails> personResult = personalDetailsRepository.findByPinfl(pinfl);
        if (personResult.isEmpty()) {
            return new ResultData<>(
                    false, ResponseStatus.FAILED, "Person with PINFL " + pinfl + " not found."
            );
        }
        var person = personResult.get();
        List<LoanInfoDto> loanInfoDtos = person.getLoanInfoEntities()
                .stream()
                .map(LoanInfoDto::toDto)
                .toList();

        return new ResultData<>(loanInfoDtos);
    }

    @Override
    public LoanResponse getLoanInfo(LoanRequest request) {
        try {
            return feign.getLoanInfo(request);
        } catch (NciException e) {
            throw new NciException(e.getMessage());
        }
    }


    public void processLoanResult(PersonDetails personDetails, LoanByPinflResponse.Result result) {
        var loanExtId = result.branch() + result.id();
        var loanOptional = loanInfoRepository.findByLoanId(loanExtId);
        if (loanOptional.isPresent()) {
            return;
        }
        try {
            var loanRequest = new LoanRequest(loanExtId);
            var loanResponse = feign.getLoanInfo(loanRequest);
            if (loanResponse == null || loanResponse.result() == null) {
                return;
            }
            saveLoanInfo(personDetails, loanResponse, loanExtId, result.id());
        } catch (NciException e) {
            throw new NciException(e.getMessage());
        }
    }


    public void saveLoanInfo(PersonDetails personDetails, LoanResponse loanResponse, String loanExtId, String loanId) {
        if (loanResponse == null || loanResponse.result() == null) {
            return;
        }
        var r = loanResponse.result();
        LoanInfoEntity loanInfoEntity = LoanInfoEntity.builder()
                .loanExtId(loanExtId)
                .loanId(loanId)
                .mfo(r.mfo())
                .clientName(r.clientName())
                .clientBirthday(r.clientBirthday() != null ? r.clientBirthday().toString() : null)
                .mainAccBalance(r.mainAccBalance())
                .transitBalance(r.transitBalance())
                .principalBalance(r.principalBalance())
                .arrearsOnPrincipal(r.arrearsOnPrincipal())
                .ofOverdueDebt(r.ofOverdueDebt())
                .interestOnTermDebt(r.interestOnTermDebt())
                .ofOverdueInterest(r.ofOverdueInterest())
                .arrearsOfInterest(r.arrearsOfInterest())
                .overdueCovenantCommission(r.overdueCovenantCommission())
                .recommendedAmount(r.recommendedAmount())
                .maxAmount(r.maxAmount())
                .loanAmount(r.loanAmount())
                .loanClose(r.loanClose() != null ? r.loanClose().toString() : null)
                .loanOpen(r.loanOpen() != null ? r.loanOpen().toString() : null)
                .transitState(r.transitState())
                .transitAccount(r.transitAccount())
                .loanType(r.loanType())
                .lossReserve(r.lossReserve())
                .currency(r.currency())
                .courtCosts(r.courtCosts())
                .build();
        personDetails.getLoanInfoEntities().add(loanInfoEntity);
        personalDetailsRepository.save(personDetails);
    }


}