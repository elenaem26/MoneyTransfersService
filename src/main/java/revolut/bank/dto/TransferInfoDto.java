package revolut.bank.dto;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity for posting while making money transfer
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@XmlRootElement
public class TransferInfoDto {

    private String fromAccountNumber;
    private String toAccountNumber;
    private Long amount;
}
