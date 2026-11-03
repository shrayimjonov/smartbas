package davr.yeisvo.entity.postgres;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(
        name = "JURIDIC_INN",
        schema = "yeisvo",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_branch_client_clientcb_id",
                        columnNames = {"clientcbId", "branchClient" }
                )
        }
)
public class JuridicINNEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int clientcbId;
    private int clientId;
    private String companyName;
    private String companyInn;
    private int statusClient;
    private String branchClient;
    private String requestBankId;
    private String requestActive;

}