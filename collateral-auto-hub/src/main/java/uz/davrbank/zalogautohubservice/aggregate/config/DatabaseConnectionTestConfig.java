package uz.davrbank.zalogautohubservice.aggregate.config;

import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;

@Service
public class DatabaseConnectionTestConfig {

    private final DataSource dataSource;
    public DatabaseConnectionTestConfig(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void testConnection() {
        long start = System.currentTimeMillis();
        try (Connection conn = dataSource.getConnection()) {
            long duration = System.currentTimeMillis() - start;
            System.out.println("✅ Connection acquired in " + duration + " ms");

            DatabaseMetaData meta = conn.getMetaData();

            System.out.println("┌─────────────────────────────────────────");
            System.out.println("│ Database: " + meta.getDatabaseProductName() + " " + meta.getDatabaseProductVersion());
            System.out.println("│ JDBC URL: " + meta.getURL());
            System.out.println("│ Username: " + meta.getUserName());

            try (Statement stmt = conn.createStatement()) {
                if (meta.getDatabaseProductName().toLowerCase().contains("oracle")) {
                    // Get the actual IP address and port of the connected instance
                    String query = """
                        SELECT 
                            SYS_CONTEXT('USERENV', 'SERVER_HOST') as hostname,
                            SYS_CONTEXT('USERENV', 'INSTANCE_NAME') as instance,
                            SYS_CONTEXT('USERENV', 'SID') as session_id,
                            SYS_CONTEXT('USERENV', 'IP_ADDRESS') as client_ip,
                            UTL_INADDR.GET_HOST_ADDRESS(SYS_CONTEXT('USERENV', 'SERVER_HOST')) as server_ip
                        FROM DUAL
                        """;

                    try (ResultSet rs = stmt.executeQuery(query)) {
                        if (rs.next()) {
                            String hostname = rs.getString("hostname");
                            String serverIp = rs.getString("server_ip");
                            String instance = rs.getString("instance");
                            String sessionId = rs.getString("session_id");
                            String clientIp = rs.getString("client_ip");

                            System.out.println("│ ═══════════════════════════════════════");
                            System.out.println("│ 🟢 CONNECTED TO NODE:");
                            System.out.println("│    IP Address: " + serverIp);
                            System.out.println("│    Hostname: " + hostname);
                            System.out.println("│    Instance: " + instance);
                            System.out.println("│ ═══════════════════════════════════════");
                            System.out.println("│ Session ID: " + sessionId);
                            System.out.println("│ Client IP: " + clientIp);
                        }
                    }

                    // Additionally, show listener port info
                    try (ResultSet rs = stmt.executeQuery(
                            "SELECT DBMS_XDB.GETHTTPPORT() as http_port FROM DUAL")) {
                        if (rs.next()) {
                            int port = rs.getInt("http_port");
                            if (port > 0) {
                                System.out.println("│ HTTP Port: " + port);
                            }
                        }
                    } catch (SQLException ignored) {
                        // HTTP port query might fail, that's okay
                    }
                }
            } catch (SQLException e) {
                System.out.println("│ ⚠️  Could not retrieve node info: " + e.getMessage());
            }

            System.out.println("└─────────────────────────────────────────");

        } catch (SQLException e) {
            long duration = System.currentTimeMillis() - start;
            System.err.println("Failed after " + duration + " ms: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
