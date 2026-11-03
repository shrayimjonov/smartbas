package uz.davrbank.zalogipatekaservice.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.davrbank.zalogipatekaservice.aggregate.dto.request.ZalogIpotekaRequestParam;
import uz.davrbank.zalogipatekaservice.aggregate.dto.response.ResponseDTO;
import uz.davrbank.zalogipatekaservice.aggregate.dto.response.ResponsePaginationDTO;
import uz.davrbank.zalogipatekaservice.aggregate.dto.response.ResponseSortDTO;
import uz.davrbank.zalogipatekaservice.feign.ZalogIpotekaClient;
import uz.davrbank.zalogipatekaservice.service.cases.ZalogIpotekaService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


@Service
@Slf4j
@RequiredArgsConstructor
public class ZalogIpotekaServiceImpl implements ZalogIpotekaService {

    private final ZalogIpotekaClient zalogIpotekaClient;

    @Override
    public ResponseDTO getAll(ZalogIpotekaRequestParam requestParam) {
        log.info("REQUEST: getAll - startDate: {}, endDate: {}, cadastreType: {}, state: {}, branch: {}, loanId: {}, clientId: {}, pinfl: {}, clientName: {}, cadastreNum: {}",
                requestParam.getStartDate(), requestParam.getEndDate(),
                requestParam.getCadastreType(), requestParam.getState(),
                requestParam.getBranch(), requestParam.getLoanId(),
                requestParam.getClientId(), requestParam.getPinfl(),
                requestParam.getClientName(), requestParam.getCadastreNum());

        try {
            Integer cadastreType = requestParam.getCadastreType();
            Integer state = requestParam.getState();

            // CadastreType validatsiyasi
            if (cadastreType == null || cadastreType < 1 || cadastreType > 2) {
                return errorDTO("Invalid cadastreType. Allowed values: 1 or 2.");
            }

            // State validatsiyasi
            List<Integer> validStates = Arrays.asList(-1, 0, 1, 2, 3, 98, 99);
            if (state == null || !validStates.contains(state)) {
                return errorDTO("Invalid state. Allowed values: -1,0, 1, 2, 3, 98, 99.");
            }

            boolean hasStart = notEmpty(requestParam.getStartDate());
            boolean hasEnd = notEmpty(requestParam.getEndDate());

            if (hasStart != hasEnd) {
                return errorDTO("Both startDate and endDate must be provided together.");
            }


            ResponseDTO response = zalogIpotekaClient.getAll(
                    hasStart ? requestParam.getStartDate() : null,
                    hasEnd ? requestParam.getEndDate() : null,
                    cadastreType,
                    state,
                    requestParam.getBranch(),
                    requestParam.getLoanId(),
                    requestParam.getClientId(),
                    requestParam.getPinfl(),
                    requestParam.getClientName(),
                    requestParam.getCadastreNum()
            );


            if (isEmptyResponse(response)) {
                StringBuilder sb = new StringBuilder("No data found");

                if (hasStart) {
                    sb.append(" between ").append(requestParam.getStartDate())
                            .append(" - ").append(requestParam.getEndDate());
                }


                List<String> filters = new ArrayList<>();
                if (requestParam.getBranch() != null) filters.add("branch=" + requestParam.getBranch());
                if (requestParam.getLoanId() != null) filters.add("loanId=" + requestParam.getLoanId());
                if (requestParam.getClientId() != null) filters.add("clientId=" + requestParam.getClientId());
                if (requestParam.getPinfl() != null) filters.add("pinfl=" + requestParam.getPinfl());
                if (requestParam.getClientName() != null) filters.add("clientName=" + requestParam.getClientName());
                if (requestParam.getCadastreNum() != null) filters.add("cadastreNum=" + requestParam.getCadastreNum());

                if (!filters.isEmpty()) {
                    sb.append(" for parameters: ").append(String.join(", ", filters));
                }

                return errorDTO(sb.toString());
            }

            log.info("RESPONSE SUCCESS getAll");
            return response;

        } catch (Exception e) {
            log.error("ERROR getAll -> {}", e.getMessage(), e);
            return errorDTO(e.getMessage());
        }
    }


    @Override
    public ResponsePaginationDTO getPageable(ZalogIpotekaRequestParam requestParam) {
        log.info("REQUEST: getPageable - {}", requestParam);

        try {
            Integer cadastreType = requestParam.getCadastreType();
            Integer state = requestParam.getState();
            Integer page = requestParam.getPage();
            Integer size = requestParam.getSize();


            if (cadastreType == null || cadastreType < 1 || cadastreType > 2) {
                return errorPagination("Invalid cadastreType. Allowed values: 1 or 2.");
            }

            List<Integer> validStates = Arrays.asList(-1, 0, 1, 2, 3, 98, 99);
            if (state == null || !validStates.contains(state)) {
                return errorPagination("Invalid state. Allowed values: " + validStates);
            }

            if (page == null || page < 1) page = 1;
            if (size == null || size < 1) size = 50000;


            boolean hasStart = notEmpty(requestParam.getStartDate());
            boolean hasEnd = notEmpty(requestParam.getEndDate());
            if (hasStart != hasEnd) {
                return errorPagination("Both startDate and endDate must be provided together.");
            }

            ResponsePaginationDTO response = zalogIpotekaClient.getPageable(
                    hasStart ? requestParam.getStartDate() : null,
                    hasEnd ? requestParam.getEndDate() : null,
                    cadastreType,
                    state,
                    page,
                    size,
                    requestParam.getBranch(),
                    requestParam.getLoanId(),
                    requestParam.getClientId(),
                    requestParam.getPinfl(),
                    requestParam.getClientName(),
                    requestParam.getCadastreNum()
            );

            if (isEmptyPagination(response)) {
                StringBuilder sb = new StringBuilder("No data found");
                if (hasStart) {
                    sb.append(" between ").append(requestParam.getStartDate())
                            .append(" - ").append(requestParam.getEndDate());
                }

                List<String> filters = new ArrayList<>();
                if (requestParam.getBranch() != null) filters.add("branch=" + requestParam.getBranch());
                if (requestParam.getLoanId() != null) filters.add("loanId=" + requestParam.getLoanId());
                if (requestParam.getClientId() != null) filters.add("clientId=" + requestParam.getClientId());
                if (requestParam.getPinfl() != null) filters.add("pinfl=" + requestParam.getPinfl());
                if (requestParam.getClientName() != null) filters.add("clientName=" + requestParam.getClientName());
                if (requestParam.getCadastreNum() != null) filters.add("cadastreNum=" + requestParam.getCadastreNum());

                if (!filters.isEmpty()) {
                    sb.append(" for parameters: ").append(String.join(", ", filters));
                }

                return errorPagination(sb.toString());
            }

            log.info("RESPONSE SUCCESS getPageable");
            return response;

        } catch (Exception e) {
            log.error("ERROR getPageable -> {}", e.getMessage(), e);
            return errorPagination(e.getMessage());
        }
    }


    @Override
    public ResponseSortDTO getPageableSorted(ZalogIpotekaRequestParam requestParam) {
        log.info("REQUEST: getPageableSorted - {}", requestParam);

        try {
            Integer cadastreType = requestParam.getCadastreType();
            Integer state = requestParam.getState();
            Integer page = requestParam.getPage();
            Integer size = requestParam.getSize();
            String sort = requestParam.getSort();


            if (cadastreType == null || cadastreType < 1 || cadastreType > 2)
                return errorSort("Invalid cadastreType. Allowed values: 1 or 2.");

            List<Integer> validStates = Arrays.asList(-1, 0, 1, 2, 3, 98, 99);
            if (state == null || !validStates.contains(state))
                return errorSort("Invalid state. Allowed values: " + validStates);

            if (page == null || page < 1) page = 1;
            if (size == null || size < 1) size = 50000;


            if (!notEmpty(sort)) return errorSort("Sort field is required.");

            boolean hasStart = notEmpty(requestParam.getStartDate());
            boolean hasEnd = notEmpty(requestParam.getEndDate());
            if (hasStart != hasEnd)
                return errorSort("Both startDate and endDate must be provided together.");

            ResponseSortDTO response = zalogIpotekaClient.getPageableSorted(
                    hasStart ? requestParam.getStartDate() : null,
                    hasEnd ? requestParam.getEndDate() : null,
                    cadastreType,
                    state,
                    page,
                    size,
                    sort,
                    requestParam.getBranch(),
                    requestParam.getLoanId(),
                    requestParam.getClientId(),
                    requestParam.getPinfl(),
                    requestParam.getClientName(),
                    requestParam.getCadastreNum()
            );

            if (isEmptySort(response)) {
                StringBuilder sb = new StringBuilder("No data found");
                if (hasStart) {
                    sb.append(" between ").append(requestParam.getStartDate())
                            .append(" - ").append(requestParam.getEndDate());
                }

                List<String> filters = new ArrayList<>();
                if (requestParam.getBranch() != null) filters.add("branch=" + requestParam.getBranch());
                if (requestParam.getLoanId() != null) filters.add("loanId=" + requestParam.getLoanId());
                if (requestParam.getClientId() != null) filters.add("clientId=" + requestParam.getClientId());
                if (requestParam.getPinfl() != null) filters.add("pinfl=" + requestParam.getPinfl());
                if (requestParam.getClientName() != null) filters.add("clientName=" + requestParam.getClientName());
                if (requestParam.getCadastreNum() != null) filters.add("cadastreNum=" + requestParam.getCadastreNum());

                if (!filters.isEmpty()) {
                    sb.append(" for parameters: ").append(String.join(", ", filters));
                }

                return errorSort(sb.toString());
            }

            log.info("RESPONSE SUCCESS getPageableSorted");
            return response;

        } catch (Exception e) {
            log.error("ERROR getPageableSorted -> {}", e.getMessage(), e);
            return errorSort(e.getMessage());
        }
    }

    private boolean notEmpty(String val) {
        return val != null && !val.trim().isEmpty();
    }

    private boolean isEmptyResponse(ResponseDTO response) {
        return response == null || response.getData() == null || response.getData().isEmpty();
    }

    private boolean isEmptyPagination(ResponsePaginationDTO response) {
        return response == null || response.getData() == null || response.getData().isEmpty();
    }

    private boolean isEmptySort(ResponseSortDTO response) {
        return response == null || response.getData() == null || response.getData().isEmpty();
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
