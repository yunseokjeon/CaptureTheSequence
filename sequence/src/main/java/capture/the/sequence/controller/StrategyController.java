package capture.the.sequence.controller;

import capture.the.sequence.dto.StrategyDTO;
import capture.the.sequence.service.StrategyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@Slf4j
@RestController
@RequestMapping("/strategy")
@RequiredArgsConstructor
public class StrategyController {

    private final StrategyService strategyService;

    @GetMapping("/getStrategies")
    public List<StrategyDTO> getStrategies() {
        return strategyService.getStrategies();
    }

    @GetMapping("/getPossessionItems")
    public Set<String> getPossessionItems(@AuthenticationPrincipal String userId) {
        return strategyService.getPossessionItems(userId);
    }
}
