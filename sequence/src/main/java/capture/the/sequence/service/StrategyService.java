package capture.the.sequence.service;

import capture.the.sequence.dto.Strategy;
import capture.the.sequence.dto.StrategyDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StrategyService {

    public List<StrategyDTO> getStrategies() {
        List<StrategyDTO> strategies = new ArrayList<>();
        strategies.add(new StrategyDTO(Strategy.PYRAMIDING, "Jesse Livermore의 거래 전략"));
        return strategies;
    }
}
