package revolut.bank.dto;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@XmlRootElement
public class ErrorMessage {

    private int status;
    private String message;
    private String detailedMessage;
}
