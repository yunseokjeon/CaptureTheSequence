package capture.the.sequence.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KellyDTO {
    double kellyRatio;
    Map<Double, Double> kellyMap = new HashMap<>();
    private List<Double> capitalGrowth = new ArrayList<>();
}
