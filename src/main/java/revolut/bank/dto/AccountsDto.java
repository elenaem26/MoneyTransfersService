package revolut.bank.dto;

import lombok.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@XmlRootElement
public class AccountsDto {

    private List<AccountDto> accounts = new ArrayList<>();
    private long total;
}
