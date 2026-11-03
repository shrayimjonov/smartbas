package soap.humo_gateway.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@ToString
@Getter
@Setter
@Entity
@Table(name = "humo_log_protocol",
        indexes = {
                @Index(columnList = "remoteAddr", name = "log_protocol_remoteAddr"),
                @Index(columnList = "localAddr", name = "log_protocol_localAddr"),
                @Index(columnList = "method", name = "log_protocol_method"),
                @Index(columnList = "createdDate", name = "log_protocol_createddate")
        })
@AllArgsConstructor
@NoArgsConstructor
public class LogProtocol {
    @SequenceGenerator(name = "hum_log_seq",
            sequenceName = "hum_log_seq",
            allocationSize = 1)
    @GeneratedValue(generator = "hum_log_seq",
            strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String method;
    private String requestURI;
    private String requestURL;
    private String remoteAddr;
    private String serverPort;
    private String protocol;
    private LocalDateTime addedTime;
    private LocalDate createdDate;
    private String remoteUser;
    private String servletPath;
    private String remotePort;
    private String localPort;
    private String localAddr;

}
