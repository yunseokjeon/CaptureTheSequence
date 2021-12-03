package capture.the.sequence.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceDTO {
    LocalDate marketDate;
    String stockItem;
    Double startingPrice;
    Double closingPrice;
}
