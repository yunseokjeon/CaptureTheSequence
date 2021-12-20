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
    List<Double> kellyXAxis = new ArrayList<>();
    List<Double> kellyYAxis = new ArrayList<>();
    private List<Double> capitalGrowth = new ArrayList<>();
}
