package uz.davrbank.auditaccounthubservice.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uz.davrbank.auditaccounthubservice.aggregate.config.OracleDatabaseConfig;
import uz.davrbank.auditaccounthubservice.aggregate.dto.AccountSummaryDTO;
import uz.davrbank.auditaccounthubservice.aggregate.dto.ResponseUniversalDTO;
import uz.davrbank.auditaccounthubservice.aggregate.dto.TransactionDTO;
import uz.davrbank.auditaccounthubservice.exception.condition.ConnectionException;
import uz.davrbank.auditaccounthubservice.service.cases.AuditPaginationInterface;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class AuditPaginationService implements AuditPaginationInterface {
    private static final Logger LOG = LoggerFactory.getLogger(AuditPaginationService.class);

    private final OracleDatabaseConfig oracleDatabaseConfig;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AuditPaginationService(OracleDatabaseConfig oracleDatabaseConfig) {
        this.oracleDatabaseConfig = oracleDatabaseConfig;
    }

    @Override
    public ResponseUniversalDTO<List<AccountSummaryDTO<TransactionDTO>>> getAuditPagination(String startDate, String endDate, Integer pageNo, Integer pageSize, String iAccKod, String iBranch) {

        LOG.info("REQUEST: Getting getAuditPagination transactions; of Period :{}  :{}, page: {}, size: {}, sort: {}", startDate, endDate, pageNo, pageSize, iAccKod);

        final String jdbcUrl = oracleDatabaseConfig.getServerUrl();
        final String username = oracleDatabaseConfig.getUsername();
        final String password = oracleDatabaseConfig.getPassword();
        iAccKod = StringUtils.isNotEmpty(iAccKod) ? (iAccKod.replace("[", "").replace("]", "").toLowerCase()) : iAccKod;
        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password)) {

            CallableStatement cstmt = conn.prepareCall(
                    "{ ? = call BAS_HUB_REPORT.AccSaldoOborotPageable(?, ?, ?, ?, ?, ?) }"
            );

            generateQuery(cstmt, startDate, endDate, pageNo, pageSize);
            cstmt.setString(6, iAccKod);
            cstmt.setString(7, iBranch);

            return getResponseUniversalDTO(cstmt);
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new ConnectionException("There is a problem connecting to the database. method: getAuditPagination");
        }
    }

    private ResponseUniversalDTO<List<AccountSummaryDTO<TransactionDTO>>> getResponseUniversalDTO(CallableStatement cstmt) throws SQLException, com.fasterxml.jackson.core.JsonProcessingException {
        cstmt.execute();

        Clob clob = cstmt.getClob(1);
        String jsonResult = clob.getSubString(1, (int) clob.length());

        cstmt.close();

        return objectMapper.readValue(
                jsonResult,
                new TypeReference<>() {
                }
        );
    }

    private void generateQuery(CallableStatement cstmt, String startDate, String endDate, int pageNo, int pageSize) throws SQLException, ParseException {
        cstmt.registerOutParameter(1, Types.CLOB);

        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        java.util.Date parsedDate1 = sdf.parse(startDate);
        Date sqlDate1 = new Date(parsedDate1.getTime());

        java.util.Date parsedDate2 = sdf.parse(endDate);
        Date sqlDate2 = new Date(parsedDate2.getTime());

        cstmt.setDate(2, sqlDate1);
        cstmt.setDate(3, sqlDate2);

        cstmt.setInt(4, pageNo);
        cstmt.setInt(5, pageSize);
    }
}
