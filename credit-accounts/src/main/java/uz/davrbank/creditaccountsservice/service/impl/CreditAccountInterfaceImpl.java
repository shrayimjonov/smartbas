package uz.davrbank.creditaccountsservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.creditaccountsservice.aggregate.request.CreditAccountRequestDTO;
import uz.davrbank.creditaccountsservice.aggregate.request.CreditRequestPageable;
import uz.davrbank.creditaccountsservice.aggregate.request.CreditRequestSorted;
import uz.davrbank.creditaccountsservice.aggregate.response.ResponseDTO;
import uz.davrbank.creditaccountsservice.aggregate.response.ResponsePaginationDTO;
import uz.davrbank.creditaccountsservice.aggregate.response.ResponseSortDTO;
import uz.davrbank.creditaccountsservice.feign.CreditAccountClient;
import uz.davrbank.creditaccountsservice.service.CreditAccountInterface;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CreditAccountInterfaceImpl implements CreditAccountInterface {

    private final CreditAccountClient creditAccountClient;

    @Override
    public ResponseDTO getAllCreditAccounts(CreditAccountRequestDTO req) {
        log.info("Credit Account: get all - date: {} , branch: {} , state: {} , valType: {} , account:{} ",
                req.getDate(), req.getBranch(), req.getState(), req.getValType(), req.getAccount());

        try {

            if (req.getDate() == null || !req.getDate().matches("\\d{2}\\.\\d{2}\\.\\d{4}")) {
                return errorDTO("Invalid date format. Expected: dd.MM.yyyy");
            }

            List<String> validBranches = Arrays.asList(
                    "00981", "01072", "01086", "01046", "01069", "01121", "01122", "00000"
            );

            if (req.getBranch() == null || !validBranches.contains(req.getBranch())) {
                return errorDTO("Invalid branch. Allowed: 00981,01072,01086,01046,01069,01121,01122,00000");
            }

            List<Integer> validStates = Arrays.asList(-1, 0, 1, 2, 3, 98, 99);
            if (req.getState() == null || !validStates.contains(req.getState())) {
                return errorDTO("Invalid state. Allowed: -1,0,1,2,3,98,99");
            }

            List<Integer> validValTypes = Arrays.asList(0, 1, -1);
            if (req.getValType() == null || !validValTypes.contains(req.getValType())) {
                return errorDTO("Invalid valType. Allowed: 0,1,-1");
            }

            if (req.getAccount() == null || req.getAccount().trim().isEmpty()) {
                return errorDTO("Account is required and cannot be empty");
            }

            if (req.getAccount().length() > 20) {
                return errorDTO("Account length must be 0–20 characters");
            }

            ResponseDTO responseDTO = creditAccountClient.getAllAccountCard(req);

            if (responseDTO == null) {
                return errorDTO("Hub returned null response.");
            }
            log.info("RESPONSE SUCCESS getAll");
            return responseDTO;

        } catch (Exception e) {
            log.error("ERROR getAll -> {}", e.getMessage(), e);
            return errorDTO("Internal error: " + e.getMessage());
        }
    }

    @Override
    public ResponsePaginationDTO getAllCreditAccountsPageable(CreditRequestPageable req) {

        log.info("Credit Account: get all pageable - date: {} , branch: {} , state: {} , valType: {} , account:{}, page:{}, size:{} ",
                req.getDate(), req.getBranch(), req.getState(), req.getValType(),
                req.getAccount(), req.getPageNumber(), req.getPageSize());

        try {

            List<String> validBranches = Arrays.asList(
                    "00981", "01072", "01086", "01046", "01069", "01121", "01122", "00000"
            );

            if (req.getBranch() == null || !validBranches.contains(req.getBranch())) {
                return errorPagination("Invalid branch. Allowed: 00981,01072,01086,01046,01069,01121,01122,00000");
            }

            List<Integer> validStates = Arrays.asList(-1, 0, 1, 2, 3, 98, 99);
            if (req.getState() == null || !validStates.contains(req.getState())) {
                return errorPagination("Invalid state. Allowed: -1,0,1,2,3,98,99");
            }


            List<Integer> validValTypes = Arrays.asList(0, 1, -1);
            if (req.getValType() == null || !validValTypes.contains(req.getValType())) {
                return errorPagination("Invalid valType. Allowed: 0,1,-1");
            }

            if (req.getAccount() != null && req.getAccount().length() > 20) {
                return errorPagination("Account length must be 0–20 characters");
            }


            if (req.getPageNumber() == null || req.getPageNumber() < 0) {
                return errorPagination("iPageNumber must be >= 0");
            }

            if (req.getPageSize() == null || req.getPageSize() < 1 || req.getPageSize() > 20000) {
                return errorPagination("iPageSize must be between 1 and 20000");
            }

            ResponsePaginationDTO response = creditAccountClient.getAllPageable(req);

            if (response == null) {
                return errorPagination("Hub returned null response");
            }

            log.info("RESPONSE SUCCESS getAllPageable");
            return response;

        } catch (Exception e) {
            log.error("ERROR getAllPageable -> {}", e.getMessage(), e);
            return errorPagination("Internal error: " + e.getMessage());
        }
    }


    @Override
    public ResponseSortDTO getAllCreditAccountsPageableSorted(CreditRequestSorted req) {

        log.info("Credit Account: get all pageable sorted - date: {} , branch: {} , state: {} , valType: {} , account:{}, page:{}, size:{}, sort:{} ",
                req.getDate(), req.getBranch(), req.getState(), req.getValType(),
                req.getAccount(), req.getPageNumber(), req.getPageSize(), req.getSort());

        try {


            List<String> validBranches = Arrays.asList(
                    "00981", "01072", "01086", "01046", "01069", "01121", "01122", "00000"
            );

            if (req.getBranch() == null || !validBranches.contains(req.getBranch())) {
                return errorSort("Invalid branch. Allowed: 00981,01072,01086,01046,01069,01121,01122,00000");
            }

            List<Integer> validStates = Arrays.asList(-1, 0, 1, 2, 3, 98, 99);
            if (req.getState() == null || !validStates.contains(req.getState())) {
                return errorSort("Invalid state. Allowed: -1,0,1,2,3,98,99");
            }

            List<Integer> validValTypes = Arrays.asList(0, 1, -1);
            if (req.getValType() == null || !validValTypes.contains(req.getValType())) {
                return errorSort("Invalid valType. Allowed: 0,1,-1");
            }

            if (req.getAccount() != null && req.getAccount().length() > 20) {
                return errorSort("Account length must be 0–20 characters");
            }


            if (req.getPageNumber() == null || req.getPageNumber() < 0) {
                return errorSort("iPageNumber must be >= 0");
            }

            if (req.getPageSize() == null || req.getPageSize() < 1 || req.getPageSize() > 20000) {
                return errorSort("iPageSize must be between 1 and 20000");
            }

            if (req.getSort() == null || req.getSort().isEmpty()) {
                return errorSort("Sort parameter is required");
            }


            ResponseSortDTO response = creditAccountClient.getAllPageableSorted(req);

            if (response == null) {
                return errorSort("Hub returned null response");
            }

            log.info("RESPONSE SUCCESS getAllPageableSorted");
            return response;

        } catch (Exception e) {
            log.error("ERROR getAllPageableSorted -> {}", e.getMessage(), e);
            return errorSort("Internal error: " + e.getMessage());
        }
    }

    private ResponseDTO errorDTO(String msg) {
        ResponseDTO r = new ResponseDTO();
        r.setStatus("error");
        r.setError(msg);
        r.setData(Collections.emptyList());
        return r;
    }

    private ResponsePaginationDTO errorPagination(String msg) {
        ResponsePaginationDTO r = new ResponsePaginationDTO();
        r.setStatus("error");
        r.setError(msg);
        r.setData(Collections.emptyList());
        r.setTotalElements(0);
        return r;
    }

    private ResponseSortDTO errorSort(String msg) {
        ResponseSortDTO r = new ResponseSortDTO();
        r.setStatus("error");
        r.setError(msg);
        r.setData(Collections.emptyList());
        return r;
    }
}
