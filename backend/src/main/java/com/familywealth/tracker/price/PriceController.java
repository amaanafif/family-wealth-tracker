package com.familywealth.tracker.price;

import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/prices")
public class PriceController {
    private final PriceService prices;

    public PriceController(PriceService prices) {
        this.prices = prices;
    }

    @PostMapping("/refresh")
    Map<String, Integer> refresh(@RequestParam(defaultValue = "false") boolean force) {
        return prices.refreshStalePrices(force);
    }
}
