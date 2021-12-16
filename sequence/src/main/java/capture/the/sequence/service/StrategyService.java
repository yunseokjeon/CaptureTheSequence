package capture.the.sequence.service;

import capture.the.sequence.dto.KellyDTO;
import capture.the.sequence.dto.Strategy;
import capture.the.sequence.dto.StrategyDTO;
import capture.the.sequence.model.PriceEntity;
import capture.the.sequence.model.UserEntity;
import capture.the.sequence.persistence.PriceRepository;
import capture.the.sequence.persistence.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.method.P;
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

    public KellyDTO calculatePyramiding(String userId, String itemName) {
        UserEntity user = userRepository.getById(userId);
        user.getPriceEntityList().clear();
        List<PriceEntity> priceEntities = priceRepository.findAllByUser(user);
        user.setPriceEntityList(priceEntities);
        List<PriceEntity> material = priceRepository.getCalculationMaterial(user, itemName);

        if (material.size() < 50) {
            throw new RuntimeException("You don't have enough data.");
        }

        PyramidingPosition position = new PyramidingPosition(10000);
        for (PriceEntity priceEntity : material) {

            double price = priceEntity.getClosingPrice();

            if (position.isHasFirst()) {
                if (position.shouldILiquidate(price)) {
                    position.liquidate(price);
                    continue;
                }

                if (position.shouldIAddSecond(price)) {
                    log.info("두 번째 포지션 더하는 조건 충족");
                    log.info(String.valueOf(priceEntity.getMarketDate()));
                    position.startSecond(price);
                    continue;
                }

                if (position.shouldIAddThird(price)) {
                    log.info("세 번째 포지션 더하는 조건 충족");
                    log.info(String.valueOf(priceEntity.getMarketDate()));
                    position.startThird(price);
                    continue;
                }

            } else {
                log.info("첫 번째 포지션 더하는 조건 충족");
                log.info(String.valueOf(priceEntity.getMarketDate()));
                position.startFirst(priceEntity.getClosingPrice());
                continue;
            }
        }

        Double last = material.get(material.size() - 1).getClosingPrice();
        if (position.isHasFirst()) {
            position.liquidate(last);
        }

        KellyDTO kellyDTO = position.calculateKelly();
        return kellyDTO;

    }

}
