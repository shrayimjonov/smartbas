package uz.davrbank.userinfobase.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DavrhubFileListRequest(
        @JsonProperty("P_TRANS_ID")
        String pTransId,

        @JsonProperty("P_BRANCH")
        String pBranch
) {}