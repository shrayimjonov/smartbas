package uz.devops.humoMiddleware.dto.response;

public class ClientRemoveCardResponseDTO extends ClientResponseDTO {

    public ClientRemoveCardResponseDTO(String state, String message) {
        super(state, message);
    }

    @Override
    public String toString() {
        return "CustomerRemoveCardResponseDTO{" + super.toString() + "}";
    }
}
