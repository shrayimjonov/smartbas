package soap.humo_gateway.dto.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SubSystem {
    PAYMO("paymo additional Service"),
    EOPC("svgate, genesis and humo service"),
    MOBILE_BACK("davr mobile back end"),
    NCI_GATEWAY("nci gateway service"),
    TEST_SERVICE("using environment for testing from another services"),
    MONITORING_MOBILE("davr mobile card history method"),
    NEW_MOBILE("new davr mobile backend"),
    ADM("Automatic deposit machine"),
    EXTERNAL("External payment system");

    private final String name;
}
