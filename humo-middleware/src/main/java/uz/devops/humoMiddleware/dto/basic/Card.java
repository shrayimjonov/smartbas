package uz.devops.humoMiddleware.dto.basic;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "Card")
public class Card {

    @NotNull
    @Size(max = 20)
    @ApiModelProperty("Card (primary account number)")
    private String pan;

    @Size(max = 256)
    @ApiModelProperty("Card label(could be used in sms)")
    private String label;

    @Size(max = 128)
    @ApiModelProperty("Card owner identifier, is used to register supplementary cards")
    private String ownerId;

    private String state;

    private String expiry;

    @JsonProperty(value = "Service")
    @ApiModelProperty("List of service packages")
    private List<Service> service;

    @JsonProperty(value = "Charge")
    @ApiModelProperty("Card level charge settings")
    private Charge charge;

    public Card(String pan) {
        this.pan = pan;
    }

    public Card(String pan, String label, List<uz.devops.humoMiddleware.dto.basic.Service> service, uz.devops.humoMiddleware.dto.basic.Charge charge) {
        this.pan = pan;
        this.label = label;
        service = service;
        charge = charge;
    }


}
