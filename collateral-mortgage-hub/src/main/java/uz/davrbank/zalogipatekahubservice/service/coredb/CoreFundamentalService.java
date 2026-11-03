package uz.davrbank.zalogipatekahubservice.service.coredb;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uz.davrbank.zalogipatekahubservice.aggregate.config.OracleDatabaseConfig;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Service
public class CoreFundamentalService {

    private final ObjectMapper objectMapper;
    private final OracleDatabaseConfig oracleDatabaseConfig;
    private static final String DATE_FORMAT = "dd.MM.yyyy";
    private static final Logger LOG = LoggerFactory.getLogger(CoreFundamentalService.class);

    public CoreFundamentalService(ObjectMapper objectMapper, OracleDatabaseConfig oracleDatabaseConfig) {
        this.objectMapper = objectMapper;
        this.oracleDatabaseConfig = oracleDatabaseConfig;
    }


    public <T> T executeQuery(String procedureName, String callStatement,
                               Integer iCadastreType, Integer iState,
                               Integer pageNumber, Integer pageSize, String sort,
                               Class<T> responseType) {

        final String urlServer = oracleDatabaseConfig.getServerUrl();
        final String username = oracleDatabaseConfig.getUsername();
        final String password = oracleDatabaseConfig.getPassword();

        try (Connection conn = DriverManager.getConnection(urlServer, username, password);
             CallableStatement cstmt = conn.prepareCall(callStatement)) {

            // Register OUT parameter
            cstmt.registerOutParameter(1, Types.CLOB);

            cstmt.setInt(2, iCadastreType);
            cstmt.setInt(3, iState);

            // Set optional pagination parameters
            int paramIndex = 4;
            if (pageNumber != null) {
                cstmt.setInt(paramIndex++, pageNumber);
            }
            if (pageSize != null) {
                cstmt.setInt(paramIndex++, pageSize);
            }
            if (sort != null) {
                cstmt.setString(paramIndex, sort);
            }

            // Execute stored procedure
            executeStatement(cstmt, procedureName);

            // Read CLOB result
            Clob clob = cstmt.getClob(1);
            String jsonResult = readClob(clob);

            // Parse JSON to DTO
            T response = objectMapper.readValue(jsonResult, responseType);

            LOG.info("RESPONSE ASYNC: {} completed for period {} to {}",
                    procedureName, iCadastreType, iState);
            return response;

        } catch (SQLException sqlEx) {
            LOG.error("Database error in {}: {}", procedureName, sqlEx.getMessage(), sqlEx);
            throw new RuntimeException("Database Connection Error: " + sqlEx.getMessage(), sqlEx);
        } catch (Exception ex) {
            LOG.error("Unexpected error in {}: {}", procedureName, ex.getMessage(), ex);
            throw new RuntimeException("Unexpected Error occurred: " + ex.getMessage(), ex);
        }
    }

    public Date parseToSqlDate(String dateStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
            sdf.setLenient(false); // Strict parsing
            java.util.Date parsedDate = sdf.parse(dateStr);
            return new Date(parsedDate.getTime());
        } catch (ParseException pe) {
            LOG.error("Date parsing failed for: {}", dateStr, pe);
            throw new RuntimeException("Invalid date format. Expected " + DATE_FORMAT, pe);
        }
    }

    public void executeStatement(CallableStatement cstmt, String procedureName) throws SQLException {
        try {
            cstmt.execute();
        } catch (SQLException sqle) {
            LOG.error("SQL Execution failed for {}: {}", procedureName, sqle.getMessage(), sqle);
            if (sqle.getMessage() != null && sqle.getMessage().contains("PLS-00302")) {
                throw new RuntimeException("Stored Procedure " + procedureName +
                        " or its component is not found or invalid in DB.", sqle);
            }
            throw new RuntimeException("SQL Execution Error in " + procedureName, sqle);
        }
    }

    private String readClob(Clob clob) throws SQLException {
        if (clob == null) {
            LOG.error("Received NULL CLOB from procedure");
            throw new RuntimeException("No data returned from the procedure");
        }

        long clobLength = clob.length();
        if (clobLength == 0) {
            LOG.warn("Received empty CLOB");
            return "{}"; // Return empty JSON object
        }

        return clob.getSubString(1, (int) clobLength);
    }
}