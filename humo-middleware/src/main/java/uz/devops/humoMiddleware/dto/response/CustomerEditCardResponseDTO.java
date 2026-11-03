package uz.devops.humoMiddleware.dto.response;

public class CustomerEditCardResponseDTO extends ClientResponseDTO {

    public CustomerEditCardResponseDTO(String state, String message) {
        super(state, message);
    }

    @Override
    public String toString() {
        return "CustomerEditCardResponseDTO{" + super.toString() + "}";
    }
}
