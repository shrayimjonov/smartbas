package uz.davrbank.zalogipatekahubservice.service;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import uz.davrbank.zalogipatekahubservice.aggregate.dto.response.ResponseDTO;
import uz.davrbank.zalogipatekahubservice.aggregate.dto.response.ResponsePaginationDTO;
import uz.davrbank.zalogipatekahubservice.aggregate.dto.response.ResponseSortDTO;
import uz.davrbank.zalogipatekahubservice.service.cases.ZalogIpotekaInterface;
import uz.davrbank.zalogipatekahubservice.service.coredb.CoreFundamentalService;
import uz.davrbank.zalogipatekahubservice.utils.HttpServletUtils;
import uz.davrbank.zalogipatekahubservice.utils.VerficationUtil;

import java.util.Objects;

@Service
public class ZalogIpotekaInfoService implements ZalogIpotekaInterface {

    private final CoreFundamentalService coreFundamentalService;
    private static final Logger LOG = LoggerFactory.getLogger(ZalogIpotekaInfoService.class);
    private final HttpServletUtils httpServletUtils;

    public ZalogIpotekaInfoService(CoreFundamentalService coreFundamentalService, HttpServletUtils httpServletUtils) {
        this.coreFundamentalService = coreFundamentalService;
        this.httpServletUtils = httpServletUtils;
    }

    @Override
    public ResponseDTO getAllNoPagination(int cadastreType, int state) {
        LOG.info("REQUEST: (getAllNoPagination) cadastreType: {}, State: {}", cadastreType, state);

        HttpServletRequest requestProject =
                ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                        .getRequest();

        httpServletUtils.httpRequestLog(requestProject);

        try {
            // Validate state before calling DB
            String stateDesc = VerficationUtil.checkingVerifyState(state);
            LOG.info("State verified: {}", stateDesc);

            String cadastralType = VerficationUtil.checkingCadastrType(cadastreType);
            LOG.info("CADASTRAL TYPE: {}", cadastralType);

            // Define procedure name and statement
            String procedureName = "BAS_HUB_REPORT.ZalogIpoteka";
            String callStatement = "{ ? = call BAS_HUB_REPORT.ZalogIpoteka(?, ?) }";

            // Call reusable DB logic
            return coreFundamentalService.executeQuery(
                    procedureName,
                    callStatement,
                    cadastreType,
                    state,
                    null, null, null,
                    ResponseDTO.class
            );
        } catch (Exception e) {
            LOG.error("Error in getAllNoPagination: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public ResponsePaginationDTO getAllPagination(Integer cadastreType, Integer state, Integer pageNumber,
                                                  Integer pageSize) {
        LOG.info("REQUEST : (getAllPagination) cadastreType: {}, State: {}", cadastreType, state);
        HttpServletRequest requestProject =
                ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                        .getRequest();

        httpServletUtils.httpRequestLog(requestProject);
        try {
            // Validate state before calling DB
            String stateDesc = VerficationUtil.checkingVerifyState(state);
            LOG.info("State verified (state): {}", stateDesc);

            String cadlType = VerficationUtil.checkingCadastrType(cadastreType);
            LOG.info("CADASTRAL TYPE (info) : {}", cadlType);


            String procedureName = "BAS_HUB_REPORT.ZalogIpotekaPageable";
            String callStatement = "{ ? = call BAS_HUB_REPORT.ZalogIpotekaPageable(?, ?, ?, ?) }";

            return coreFundamentalService.executeQuery(
                    procedureName,
                    callStatement,
                    cadastreType,
                    state,
                    pageNumber,
                    pageSize,
                    null, // no sort
                    ResponsePaginationDTO.class
            );

        } catch (Exception e) {
            LOG.error("Error in getAllPagination: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public ResponseSortDTO getAllPaginationSort(Integer cadastreType, Integer state, Integer pageNumber,
                                                Integer pageSize, String sort) {

        LOG.info("REQUEST: (getAllPaginationSort) cadastreType: {}, State: {}", cadastreType, state);
        HttpServletRequest requestProject =
                ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes()))
                        .getRequest();

        httpServletUtils.httpRequestLog(requestProject);
        try {
            // Validate state before calling DB
            String stateDesc = VerficationUtil.checkingVerifyState(state);
            LOG.info("State verified (state Sort): {}", stateDesc);

            String cadastralType = VerficationUtil.checkingCadastrType(cadastreType);
            LOG.info("CADASTRAL TYPE (for information) : {}", cadastralType);

            String procedureName = "BAS_HUB_REPORT.ZalogIpotekaPageableSort";
            String callStatement = "{ ? = call BAS_HUB_REPORT.ZalogIpotekaPageableSort(?, ?, ?, ?, ?) }";

            return coreFundamentalService.executeQuery(
                    procedureName,
                    callStatement,
                    cadastreType,
                    state,
                    pageNumber,
                    pageSize,
                    sort,
                    ResponseSortDTO.class
            );

        } catch (Exception e) {
            LOG.error("Error in getAllPaginationSort: {}", e.getMessage(), e);
            throw e;
        }
    }
}