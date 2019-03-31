package revolut.bank.dto;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Entity for one money transfer
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@XmlRootElement
public class TransferHistoryDto {

    private String userAccountNumber;
    private String interactionAccountNumber;
    private String operation;
    private Long amount;
    private String timestamp;
    private String state;
}
