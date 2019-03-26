package revolut.bank.dto;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@XmlRootElement
public class TransferHistoriesDto {

    private List<TransferHistoryDto> history;
    private long total;
}
