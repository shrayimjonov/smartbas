package uz.davrbank.payment.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import uz.davrbank.payment.model.dto.billing.request.CheckRequest;
import uz.nodir.commondto.entity.BaseEntity;

import java.util.Map;


@Getter
@Setter
@ToString
@Entity
@Table(name = "bill",
        indexes = {
                @Index(name = "tranId", columnList = "tran_id")
        })
@AllArgsConstructor
@NoArgsConstructor
public class Bill extends BaseEntity {
    @SequenceGenerator(name = "bill_seq",
            sequenceName = "bill_seq",
            allocationSize = 1)
    @GeneratedValue(generator = "bill_seq",
            strategy = GenerationType.SEQUENCE)
    @Id
    private Long id;
    @Column(name = "tran_id")
    private Long tranId;
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<Object,Object> checkRequest;
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<Object,Object> checkResponse;
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<Object,Object> payRequest;
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<Object,Object> payResponse;
    private String requestId;


    public Bill(CheckRequest checkRequest, Long tranId, String requestId) {
        this.tranId = tranId;
        this.checkRequest = checkRequest.toMap();
        this.requestId = requestId;
    }
}
