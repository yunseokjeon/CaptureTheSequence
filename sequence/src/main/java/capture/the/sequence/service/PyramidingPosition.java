package capture.the.sequence.service;

import capture.the.sequence.dto.KellyDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServlet;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class PyramidingPosition {
    private boolean hasFirst = false;
    private boolean hasSecond = false;
    private boolean hasThird = false;
    private double buyingPrice;
    private int holdingQuntity;
    private double holdingCash;
    private double lastRecord;
    private double alltimeHigh;
    private List<Double> capitalGrowth = new ArrayList<>();
    private double kellyRatio;
    private Map<Double, Double> kellyMap = new HashMap<>();

    public PyramidingPosition(double initialCapital) {
        capitalGrowth.add(initialCapital);
        this.buyingPrice = 0;
        this.holdingQuntity = 0;
        this.holdingCash = initialCapital;
        this.alltimeHigh = 0;
        this.lastRecord = initialCapital;
        this.kellyRatio = 0;
    }

    public void startFirst(double pricePerShare) {
        log.info("startFirst");
        this.hasFirst = true;
        int quantityICanBuy = (int) ((this.holdingCash / pricePerShare) / 3);
        this.holdingQuntity = quantityICanBuy;
        this.holdingCash -= (this.holdingQuntity * pricePerShare);
        this.buyingPrice = pricePerShare;
        this.alltimeHigh = Math.max(this.alltimeHigh, pricePerShare);
        this.lastRecord = this.buyingPrice * this.holdingQuntity
                + this.holdingCash;
    }

    public boolean shouldILiquidate(double pricePerShare) {
        if (this.hasFirst && pricePerShare <= (this.alltimeHigh * 0.9)) {
            return true;
        } else {
            return false;
        }
    }

    public void liquidate(double pricePerShare) {

        log.info("==============================");
        log.info("liquidate");
        log.info(String.valueOf(pricePerShare));
        log.info("==============================");

        this.hasFirst = false;
        this.hasSecond = false;
        this.hasThird = false;

        double presentCapital = pricePerShare
                * this.holdingQuntity
                + this.holdingCash;
        this.capitalGrowth.add(presentCapital);

        this.holdingCash = presentCapital;
        this.buyingPrice = 0;
        this.holdingQuntity = 0;
        this.alltimeHigh = 0;
    }

    public boolean shouldIAddSecond(double pricePerShare) {
        double presentCapital = pricePerShare
                * this.holdingQuntity
                + this.holdingCash;

        if (this.lastRecord * 1.2 <= presentCapital
                && !this.hasSecond) {
            return true;
        } else {
            return false;
        }
    }

    public void startSecond(double pricePerShare) {
        log.info("startSecond");
        this.hasSecond = true;
        int quantityICanBuy = (int) ((this.holdingCash / pricePerShare) / 2);
        this.buyingPrice = ((this.buyingPrice * this.holdingQuntity)
                + (pricePerShare * quantityICanBuy)) / (this.holdingQuntity + quantityICanBuy);
        this.holdingCash -= (pricePerShare * quantityICanBuy);
        this.holdingQuntity += quantityICanBuy;
        this.alltimeHigh = Math.max(this.alltimeHigh, pricePerShare);
        this.lastRecord = this.buyingPrice * this.holdingQuntity
                + this.holdingCash;
    }

    public boolean shouldIAddThird(double pricePerShare) {
        double presentCapital = pricePerShare
                * this.holdingQuntity
                + this.holdingCash;

        if (this.lastRecord * 1.2 <= presentCapital
                && !this.hasThird) {
            return true;
        } else {
            return false;
        }
    }

    public void startThird(double pricePerShare) {
        log.info("startThird");
        this.hasThird = true;
        int quantityICanBuy = (int) (this.holdingCash / pricePerShare);
        this.buyingPrice = ((this.buyingPrice * this.holdingQuntity)
                + (pricePerShare * quantityICanBuy)) / (this.holdingQuntity + quantityICanBuy);
        this.holdingCash -= (pricePerShare * quantityICanBuy);
        this.holdingQuntity += quantityICanBuy;
        this.alltimeHigh = Math.max(this.alltimeHigh, pricePerShare);
        this.lastRecord = this.buyingPrice * this.holdingQuntity
                + this.holdingCash;
    }

    public KellyDTO calculateKelly() {
        // Y = E ln(1 + fX)
        List<Double> earningsRates = new ArrayList<>();

        for (int i = 1; i < this.capitalGrowth.size(); i++) {
            double rate = (this.capitalGrowth.get(i) - this.capitalGrowth.get(i - 1)) / 100;
            earningsRates.add(rate);
        }

        double highest = 0;

        for (double f = 0; f < 10; f += 1) {
            double y = 0;
            for (int i = 0; i < earningsRates.size(); i++) {

                if (earningsRates.get(i) <= -0.5) {
                    y += (((double) 1 / (double) earningsRates.size())) * Math.log(0.1);
                } else {
                    y += ((double) 1 / (double) earningsRates.size()) * Math.log(1 + f * earningsRates.get(i));
                }
            }

            this.kellyMap.put(f, y);
            if (highest < y) {
                highest = y;
                this.kellyRatio = f;
            }
        }

        List<Map.Entry<Double, Double>> entries = new LinkedList<>(this.kellyMap.entrySet());
        Collections.sort(entries, (o1, o2) -> o1.getKey().compareTo(o2.getKey()));

        LinkedHashMap<Double, Double> result = new LinkedHashMap<>();
        for (Map.Entry<Double, Double> entry : entries) {
            result.put(entry.getKey(), entry.getValue());
        }

        this.kellyMap = result;

        KellyDTO kellyDTO = KellyDTO.builder()
                .kellyRatio(this.kellyRatio)
                .kellyMap(this.kellyMap)
                .capitalGrowth(this.capitalGrowth)
                .build();
        return kellyDTO;
    }



}

