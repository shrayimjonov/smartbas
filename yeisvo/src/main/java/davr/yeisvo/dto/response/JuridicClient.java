package davr.yeisvo.dto.response;

import davr.yeisvo.entity.postgres.JuridicINNEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JuridicClient {
    //    private Long id;
    private int clientcbId;
    private int clientId;
    private String companyName;
    private String companyInn;
    private int statusClient;
    private String branchClient;
    private String requestBankId;
    private String requestActive;

    public JuridicINNEntity exchange() {
        JuridicINNEntity entity = new JuridicINNEntity();
        entity.setClientcbId(this.clientcbId);
        entity.setClientId(this.clientId);
        entity.setCompanyName(this.companyName);
        entity.setCompanyInn(this.companyInn);
        entity.setStatusClient(this.statusClient);
        entity.setBranchClient(this.branchClient);
        entity.setRequestBankId(this.requestBankId);
        entity.setRequestActive(this.requestActive);
        return entity;
    }
    public JuridicClient exchange(JuridicINNEntity entity) {
        JuridicClient juridicClient = new JuridicClient();
        juridicClient.setClientcbId(entity.getClientcbId());
        juridicClient.setClientId(entity.getClientId());
        juridicClient.setCompanyName(entity.getCompanyName());
        juridicClient.setCompanyInn(entity.getCompanyInn());
        juridicClient.setStatusClient(entity.getStatusClient());
        juridicClient.setBranchClient(entity.getBranchClient());
        juridicClient.setRequestBankId(entity.getRequestBankId());
        juridicClient.setRequestActive(entity.getRequestActive());
        return juridicClient;
    }
}
