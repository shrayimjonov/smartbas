package uz.davrbank.auditaccounthubservice.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uz.davrbank.auditaccounthubservice.aggregate.config.OracleDatabaseConfig;
import uz.davrbank.auditaccounthubservice.aggregate.dto.AccountSummaryDTO;
import uz.davrbank.auditaccounthubservice.aggregate.dto.ResponseUniversalDTO;
import uz.davrbank.auditaccounthubservice.exception.condition.ConnectionException;
import uz.davrbank.auditaccounthubservice.service.cases.AuditInterface;

import java.sql.*;
import java.text.SimpleDateFormat;

@Service
public class AuditService implements AuditInterface {
    private static final Logger LOG = LoggerFactory.getLogger(AuditService.class);

    private final OracleDatabaseConfig oracleDatabaseConfig;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AuditService(OracleDatabaseConfig oracleDatabaseConfig) {
        this.oracleDatabaseConfig = oracleDatabaseConfig;
    }

    @Override
    public ResponseUniversalDTO<AccountSummaryDTO> getData(String startDate, String endDate) {

        LOG.info("REQUEST: Getting getData transactions; of Period :{}  :{}", startDate, endDate);

        final String jdbcUrl = oracleDatabaseConfig.getServerUrl();
        final String username = oracleDatabaseConfig.getUsername();
        final String password = oracleDatabaseConfig.getPassword();

        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password)) {

            CallableStatement cstmt = conn.prepareCall(
                    "{ ? = call UNION_TRANSACTION.UnionpayEposObiReport(?, ?) }"
            );

            // OUT parameter
            cstmt.registerOutParameter(1, Types.CLOB);

            // IN parameters
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            // Starting Date
            java.util.Date parsedDate1 = sdf.parse(startDate);
            Date sqlDate1 = new Date(parsedDate1.getTime());

            //Ending Date
            java.util.Date parsedDate2 = sdf.parse(endDate);
            Date sqlDate2 = new Date(parsedDate2.getTime());

            cstmt.setDate(2, sqlDate1); // iDate1
            cstmt.setDate(3, sqlDate2); // iDate2

            cstmt.execute();

            Clob clob = cstmt.getClob(1);
            String jsonResult = clob.getSubString(1, (int) clob.length());
            cstmt.close();

            return objectMapper.readValue(
                    jsonResult,
                    new TypeReference<>() {
                    }
            );
        } catch (Exception e) {
            LOG.error(e.getMessage());
            throw new ConnectionException("There is a problem connecting to the database. method: getData");
        }
    }
}