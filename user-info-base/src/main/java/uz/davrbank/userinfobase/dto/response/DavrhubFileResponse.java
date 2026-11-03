package uz.davrbank.userinfobase.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record DavrhubFileResponse(

        @JsonProperty("ID")
        Long id,

        @JsonProperty("FILE_NAME")
        String fileName,

        @JsonProperty("SOURCE")
        String source,

        @JsonProperty("DTIME")
        String dtime,

        @JsonProperty("DOC_ID")
        String docId,

        @JsonProperty("STATE")
        String state,

        @JsonProperty("YEAR")
        Integer year,

        @JsonProperty("QUAR")
        Integer quar,

        @JsonProperty("PLACE_SRC")
        Integer placeSrc,

        @JsonProperty("COMMENT_TEXT")
        String commentText,

        @JsonProperty("FILE_URL")
        String fileUrl,

        @JsonProperty("NAME_DOC")
        String nameDoc,

        @JsonProperty("NAME_STATE")
        String nameState
) {}