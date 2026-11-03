package uz.davrbank.zalogautohubservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import uz.davrbank.zalogautohubservice.aggregate.DTO.response.ResponseZalogAutoHubDTO;
import uz.davrbank.zalogautohubservice.aggregate.config.OracleDatabaseConfig;
import uz.davrbank.zalogautohubservice.service.cases.ZalogAutoInterface;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;

@Service
public class ZalogAutoService implements ZalogAutoInterface {

    private static final Logger LOG = LoggerFactory.getLogger(ZalogAutoService.class);
    private final String urlServer;
    private final String username;
    private final String password;

    public ZalogAutoService(OracleDatabaseConfig config) {
        this.urlServer = config.getServerUrl();
        this.username = config.getUsername();
        this.password = config.getPassword();
    }

    @Override
    public ResponseZalogAutoHubDTO getAllNoPagination(String startDate, String endDate, Integer State) {
        LOG.info("REQUEST: (getAllNoPagination): startDate: {} : {}: {} :",  startDate + " endDate: ",
                endDate + " State: " , State);

        try (Connection conn = DriverManager.getConnection(urlServer, username, password)) {

            // Calling to Oracle Database Prodcedure
            CallableStatement cstmt = conn.prepareCall(
                    "{ ? = call BAS_HUB_REPORT.ZalogAuto(?, ?, ?) }"
            );

            // OUT parameter
            cstmt.registerOutParameter(1, Types.CLOB);

            // IN parameters
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            java.sql.Date sqlDate1;
            java.sql.Date sqlDate2;

            try {
                java.util.Date parsedDate1 = sdf.parse(startDate);
                java.util.Date parsedDate2 = sdf.parse(endDate);
                sqlDate1 = new java.sql.Date(parsedDate1.getTime());
                sqlDate2 = new java.sql.Date(parsedDate2.getTime());
            } catch (ParseException pe) {
                LOG.error("Date parsing failed for startDate: {} or endDate: {}", startDate, endDate, pe);
                throw new RuntimeException("Invalid date format. Expected dd.MM.yyyy", pe);
            }

            cstmt.setDate(2, sqlDate1);
            cstmt.setDate(3, sqlDate2);
            cstmt.setInt(4, State);


            try {
                cstmt.execute();
            } catch (SQLException sqle) {
                LOG.error("SQL Execution failed: {}", sqle.getMessage(), sqle);
                if (sqle.getMessage().contains("PLS-00302")) {
                    throw new RuntimeException("Stored Procedure BPCAllTransaction or" +
                            " its component is not found or invalid in DB.", sqle);
                } else {
                    throw new RuntimeException("SQL Execution Error", sqle);
                }
            }

            Clob clob = cstmt.getClob(1);

            if (clob == null) {
                LOG.error("Received NULL CLOB from getAllNoPagination procedure.");
                throw new RuntimeException("No data returned from the procedure.");
            }

            String jsonResult;
            try {
                long clobLength = clob.length();
                if (clobLength == 0) {
                    LOG.warn("Received empty CLOB.");
                    return (ResponseZalogAutoHubDTO) Collections.emptyList(); // or return an empty DTO.
                }
                jsonResult = clob.getSubString(1, (int) clobLength);
            } catch (SQLException sqle) {
                LOG.error("Error while reading CLOB data: {}", sqle.getMessage(), sqle);
                throw new RuntimeException("Failed to read CLOB data", sqle);
            } finally {
                cstmt.close();
            }

            // Convert JSON to DTO
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                ResponseZalogAutoHubDTO response = objectMapper.readValue(jsonResult, ResponseZalogAutoHubDTO.class);

                LOG.info("RESPONSE: All Zalog Auto period: {} from {} ", startDate, endDate);
                return response;
            } catch (Exception je) {
                LOG.error("Failed to parse JSON response: {}", je.getMessage(), je);
                throw new RuntimeException("JSON Parsing Error", je);
            }

        } catch (SQLException sqlEx) {
            LOG.error("Database Connection or SQL Error: {}", sqlEx.getMessage(), sqlEx);
            throw new RuntimeException("Database Connection Error", sqlEx);

        } catch (RuntimeException rex) {
            LOG.error("Runtime Exception occurred: {}", rex.getMessage(), rex);
            throw rex; // Re-throwing to propagate up.

        } catch (Exception ex) {
            LOG.error("Unexpected Error: {}", ex.getMessage(), ex);
            throw new RuntimeException("Unexpected Error occurred", ex);
        }
    }

    @Override
    public ResponseZalogAutoHubDTO getAllPagination(String startDate, String endDate, Integer State,
                                                    Integer pageNumber, Integer pageSize) {

        LOG.info("REQUEST: (getAllPagination) ");
        try (Connection conn = DriverManager.getConnection(urlServer, username, password)) {

            // Calling to Oracle Database Prodcedure
            CallableStatement cstmt = conn.prepareCall(
                    "{ ? = call BAS_HUB_REPORT.ZalogAutoPageable(?, ?, ?, ?, ?) }"
            );

            // OUT parameter
            cstmt.registerOutParameter(1, Types.CLOB);

            // IN parameters
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            java.sql.Date sqlDate1;
            java.sql.Date sqlDate2;

            try {
                java.util.Date parsedDate1 = sdf.parse(startDate);
                java.util.Date parsedDate2 = sdf.parse(endDate);
                sqlDate1 = new java.sql.Date(parsedDate1.getTime());
                sqlDate2 = new java.sql.Date(parsedDate2.getTime());
            } catch (ParseException pe) {
                LOG.error("Date parsing failed for startDate: {} or endDate: {}", startDate, endDate, pe);
                throw new RuntimeException("Invalid date format. Expected dd.MM.yyyy", pe);
            }

            cstmt.setDate(2, sqlDate1);
            cstmt.setDate(3, sqlDate2);
            cstmt.setInt(4, State);
            cstmt.setInt(5, pageNumber);
            cstmt.setInt(6, pageSize);


            try {
                cstmt.execute();
            } catch (SQLException sqle) {
                LOG.error("SQL Execution failed: {}", sqle.getMessage(), sqle);
                if (sqle.getMessage().contains("PLS-00302")) {
                    throw new RuntimeException("Stored Procedure BPCAllTransaction or" +
                            " its component is not found or invalid in DB.", sqle);
                } else {
                    throw new RuntimeException("SQL Execution Error", sqle);
                }
            }

            Clob clob = cstmt.getClob(1);

            if (clob == null) {
                LOG.error("Received NULL CLOB from getAllNoPagination procedure.");
                throw new RuntimeException("No data returned from the procedure.");
            }

            String jsonResult;
            try {
                long clobLength = clob.length();
                if (clobLength == 0) {
                    LOG.warn("Received empty CLOB.");
                    return (ResponseZalogAutoHubDTO) Collections.emptyList(); // or return an empty DTO.
                }
                jsonResult = clob.getSubString(1, (int) clobLength);
            } catch (SQLException sqle) {
                LOG.error("Error while reading CLOB data: {}", sqle.getMessage(), sqle);
                throw new RuntimeException("Failed to read CLOB data", sqle);
            } finally {
                cstmt.close();
            }

            // Convert JSON to DTO
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                ResponseZalogAutoHubDTO response = objectMapper.readValue(jsonResult, ResponseZalogAutoHubDTO.class);

                LOG.info("RESPONSE: All Zalog Auto period: {} from {} ", startDate, endDate);
                return response;
            } catch (Exception je) {
                LOG.error("Failed to parse JSON response: {}", je.getMessage(), je);
                throw new RuntimeException("JSON Parsing Error", je);
            }

        } catch (SQLException sqlEx) {
            LOG.error("Database Connection or SQL Error: {}", sqlEx.getMessage(), sqlEx);
            throw new RuntimeException("Database Connection Error", sqlEx);

        } catch (RuntimeException rex) {
            LOG.error("Runtime Exception occurred: {}", rex.getMessage(), rex);
            throw rex; // Re-throwing to propagate up.

        } catch (Exception ex) {
            LOG.error("Unexpected Error: {}", ex.getMessage(), ex);
            throw new RuntimeException("Unexpected Error occurred", ex);
        }
    }

    @Override
    public ResponseZalogAutoHubDTO getAllPaginationSort(String startDate, String endDate, Integer State,
                                                        Integer pageNumber, Integer pageSize, String sort) {
        return null;
    }
}
