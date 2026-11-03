package soap.humo_gateway.dto.enums;

public enum HumoAction {
    LIST_CUSTOMERS("listCustomers"),
    LIST_CUSTOMER_CARDS("listCustomerCards"),
    LIST_ACCOUNTS("listAccounts"),
    QUERY_CARD_INFO("queryCardInfo"),
    HISTORY("queryTransactionHistory"),
    EXPORT("export"),
    IMPORT("import"),
    GET_CARD_ACCOUNT_BALANCE("getCardAccountsBalance"),
    LIMITS("getLimits"),
    PAYMENT("payment"),
    GET_PAYMENT("GetPayment"),
    REVERT("ReturnPayment"),
    EXECUTE_TRANSACTION("executeTransaction"),
    getCardPinTryCounter("getCardPinTryCounter"),
    assignPin("assignPIN");

    HumoAction(String action) {
        this.action = action;
    }

    private final String action;

    public String getAction() {
        return action;
    }
}
