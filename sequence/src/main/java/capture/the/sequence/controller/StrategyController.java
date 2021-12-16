package capture.the.sequence.controller;

import capture.the.sequence.dto.KellyDTO;
import capture.the.sequence.dto.ResponseDTO;
import capture.the.sequence.dto.StrategyDTO;
import capture.the.sequence.service.StrategyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/getPyramidingKelly")
    public ResponseEntity<?> calculatePyramiding(@AuthenticationPrincipal String userId,
                                              @RequestParam("itemName") String itemName) {
        try {
            KellyDTO kellyDTO = strategyService.calculatePyramiding(userId, itemName);
            return ResponseEntity.ok(kellyDTO);
        } catch (Exception e) {
            log.info(e.getMessage());
            ResponseDTO<Object> responseDTO = ResponseDTO.builder().error(e.getMessage()).build();
            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
