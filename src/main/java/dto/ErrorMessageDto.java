package dto;

import lombok.*;

@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class ErrorMessageDto {
    private String timestamp; //	string($date-time)
    private int status; //	integer($int32)
    private String error; //	string
    private Object message;
    private String path; //	string
}