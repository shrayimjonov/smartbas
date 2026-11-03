package soap.humo_gateway.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;

@ToString
@Getter
@Setter
@Entity
@Table(name = "humo_req_protocol",
        indexes = {
                @Index(columnList = "param", name = "req_protocol_param"),
                @Index(columnList = "subSystem", name = "req_protocol_system"),
                @Index(columnList = "logProtocolId", name = "req_protocol_log"),
                @Index(columnList = "createdTime", name = "req_protocol_createdtime")
        })
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RequestProtocol {
    @SequenceGenerator(name = "hum_req_pro_seq",
            sequenceName = "hum_req_pro_seq",
            allocationSize = 1)
    @GeneratedValue(generator = "hum_req_pro_seq",
            strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String param;
    private String subSystem;
    private Long logProtocolId;
    @Builder.Default
    private LocalDateTime createdTime = LocalDateTime.now(ZoneId.of("Asia/Tashkent"));
}
