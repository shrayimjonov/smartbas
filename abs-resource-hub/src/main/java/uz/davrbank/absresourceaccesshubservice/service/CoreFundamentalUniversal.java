package uz.davrbank.absresourceaccesshubservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import oracle.jdbc.internal.OracleTypes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uz.davrbank.absresourceaccesshubservice.aggregate.config.OracleDatabaseConfig;
import uz.davrbank.absresourceaccesshubservice.aggregate.dto.UniversalRequestDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.*;

public class CoreFundamentalUniversal {
    private final ObjectMapper objectMapper;
    private final OracleDatabaseConfig oracleDatabaseConfig;
    private static final Logger LOG = LoggerFactory.getLogger(CoreFundamentalUniversal.class);
    private static final int CLOB_BUFFER_SIZE = 64 * 1024; // 64KB

    public CoreFundamentalUniversal(ObjectMapper objectMapper, OracleDatabaseConfig oracleDatabaseConfig) {
        this.objectMapper = objectMapper;
        this.oracleDatabaseConfig = oracleDatabaseConfig;
    }

    public <T> T executeQueryUniversal(UniversalRequestDto request, Class<T> responseType) {

        final String urlServer = oracleDatabaseConfig.getServerUrl();
        final String username = oracleDatabaseConfig.getUsername();
        final String password = oracleDatabaseConfig.getPassword();

        validateRequest(request);

        try (Connection conn = DriverManager.getConnection(urlServer, username, password);
             CallableStatement cstmt = conn.prepareCall(request.getCallStatement())) {

            LOG.info("EXECUTING {} with params count: {}", request.getProcedureName(),
                    request.getInParams() == null ? 0 : request.getInParams().length);

            String sql = request.getCallStatement();
            long placeholderCount = sql.chars().filter(ch -> ch == '?').count();
            int inParamCount = request.getInParams() == null ? 0 : request.getInParams().length;

            if (placeholderCount != inParamCount + 1) {
                throw new IllegalArgumentException(
                        String.format("Parameter count mismatch! Call statement has %d placeholders, " +
                                        "but %d IN parameters were provided (expected %d placeholders). " +
                                        "SQL: %s",
                                placeholderCount, inParamCount, inParamCount + 1, sql));
            }

            LOG.info("EXECUTING {} with params count: {}", request.getProcedureName(),
                    request.getInParams() == null ? 0 : request.getInParams().length);

            cstmt.registerOutParameter(1, OracleTypes.CLOB);

            bindInputParameters(cstmt, request.getInParams());

            cstmt.execute();

            Clob clob = cstmt.getClob(1);
            T result = readClobAsObject(clob, responseType);

            if (result == null) {
                LOG.warn("Empty CLOB returned!");
                return null;
            }

            return result;

        } catch (SQLException e) {
            LOG.error("Database error: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            LOG.error("Unexpected error: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }


    private void validateRequest(UniversalRequestDto request) {
        if (request == null)
            throw new IllegalArgumentException("UniversalRequestDto must not be null");

        if (request.getProcedureName() == null || request.getProcedureName().isBlank())
            throw new IllegalArgumentException("procedureName must not be empty");

        if (request.getCallStatement() == null || request.getCallStatement().isBlank())
            throw new IllegalArgumentException("callStatement must not be empty");
    }

    private void bindInputParameters(CallableStatement cstmt, Object[] params) throws SQLException {
        if (params == null) return;

        LOG.info("Binding {} IN parameters (positions 2 to {})",
                params.length, params.length + 1);

        for (int i = 0; i < params.length; i++) {
            int paramIndex = i + 2; //  1 = OUT CLOB
            Object value = params[i];
            LOG.debug(" Binding position {} = {}", paramIndex, value);
            if (value instanceof java.sql.Date) {
                cstmt.setDate(paramIndex, (java.sql.Date) value);
            } else if (value instanceof Integer) {
                cstmt.setInt(paramIndex, (Integer) value);
            } else if (value instanceof String) {
                cstmt.setString(paramIndex, (String) value);
            } else {
                cstmt.setObject(paramIndex, value);
            }
        }
    }


    private <T> T readClobAsObject(Clob clob, Class<T> responseType) throws SQLException, IOException {
        if (clob == null) return null;
        try (Reader reader = new BufferedReader(clob.getCharacterStream(), CLOB_BUFFER_SIZE)) {
            reader.mark(1);
            int first = reader.read();
            if (first == -1) {
                return null;
            }
            reader.reset();
            return objectMapper.readValue(reader, responseType);
        } finally {
            try {
                clob.free();
            } catch (SQLException ignore) {
            }
        }
    }
}
