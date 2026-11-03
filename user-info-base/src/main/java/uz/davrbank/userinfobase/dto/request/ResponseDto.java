package uz.davrbank.userinfobase.dto.request;

import lombok.*;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseDto {
    private Integer code;
    private String message;
}
