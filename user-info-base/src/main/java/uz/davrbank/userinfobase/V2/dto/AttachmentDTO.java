package uz.davrbank.userinfobase.V2.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AttachmentDTO {

    String objectName;

    String originalName;

    String attachmentUrl;

    String source;

    String displayName;

    Long fileSize;

    String contentType;
}
