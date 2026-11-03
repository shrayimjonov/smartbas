package uz.davrbank.payment.utils;

import uz.nodir.commondto.model.dto.core.response.SimpleResponse;
import uz.nodir.commondto.model.enums.ResultState;

public class BranchUtils {

    public static SimpleResponse<String> getSchemaByBranch(String branch) {
        switch (branch) {
            case "00981" -> {
                return new SimpleResponse<>("BANK981");
            }
            case "01046" -> {
                return new SimpleResponse<>("BANK1046");
            }
            case "01069" -> {
                return new SimpleResponse<>("BANK1069");
            }
            case "01072" -> {
                return new SimpleResponse<>("BANK1072");
            }
            case "01086" -> {
                return new SimpleResponse<>("BANK1086");
            }
            case "01121" -> {
                return new SimpleResponse<>("BANK1121");
            }
            case "01122" -> {
                return new SimpleResponse<>("BANK1122");
            }
            default -> {
                return new SimpleResponse<>(ResultState.BadRequest.getCode(), "Branch: %s not found".formatted(branch));
            }
        }
    }
}
