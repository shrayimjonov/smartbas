package humo.integration.card.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity(name = "log_protocol")
public class LogProtocol {
    @SequenceGenerator(name = "LOG_PRO_SEQ",
    sequenceName = "LOG_PRO_SEQ",
    allocationSize = 1)
    @GeneratedValue(generator = "LOG_PRO_SEQ",
    strategy = GenerationType.SEQUENCE)
    @Id
    private Long id;
    private String method;
    private String requestedSessionId;
    private String requestURI;
    private String requestURL;
    private String remoteAddr;
    private String ServerPort;
    private String protocol;
    private LocalDateTime localDateTime;
    private String username;
    private String RemoteUser;
    private String ServletPath;
    private String RemotePort;
    private String LocalPort;
    private String LocalAddr;
    private String serviceName;

}

