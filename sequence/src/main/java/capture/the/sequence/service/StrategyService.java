package capture.the.sequence.service;

import capture.the.sequence.dto.Strategy;
import capture.the.sequence.dto.StrategyDTO;
import capture.the.sequence.model.PriceEntity;
import capture.the.sequence.model.UserEntity;
import capture.the.sequence.persistence.PriceRepository;
import capture.the.sequence.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class StrategyService {

    private final UserRepository userRepository;
    private final PriceRepository priceRepository;

    public List<StrategyDTO> getStrategies() {
        List<StrategyDTO> strategies = new ArrayList<>();
        strategies.add(new StrategyDTO(Strategy.PYRAMIDING, "Jesse Livermore의 거래 전략"));
        return strategies;
    }

    public Set<String> getPossessionItems(String userId) {
        Set<String> itemSet = new HashSet<>();
        UserEntity user = userRepository.getById(userId);
        user.getPriceEntityList().clear();
        List<PriceEntity> priceEntities = priceRepository.findAllByUser(user);
        user.setPriceEntityList(priceEntities);
        for (PriceEntity priceEntity : user.getPriceEntityList()) {
            itemSet.add(priceEntity.getItemName());
        }
        return itemSet;
    }
}
