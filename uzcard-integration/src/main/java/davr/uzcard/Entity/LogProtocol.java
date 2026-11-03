package davr.uzcard.Entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity(name = "log_protocol")
public class LogProtocol {
    @SequenceGenerator(name = "log_pro_seq",
            sequenceName = "log_pro_seq",
            allocationSize = 1)
    @GeneratedValue(generator = "log_pro_seq",
            strategy = GenerationType.IDENTITY)
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

