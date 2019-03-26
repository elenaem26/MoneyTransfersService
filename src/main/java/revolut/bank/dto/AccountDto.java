package revolut.bank.dto;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@XmlRootElement
public class AccountDto {

    private String number;
    private Long balance;
    private String createdDateTime;
    private String description;
    private Long partyId;
}
