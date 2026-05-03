package com.familywealth.tracker.portfolio;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assets")
public class HoldingsController {
    private final HoldingsRepository holdings;

    public HoldingsController(HoldingsRepository holdings) {
        this.holdings = holdings;
    }

    @GetMapping
    List<Holdings> list() {
        return holdings.findAll();
    }

    @PostMapping
    Holdings create(@Valid @RequestBody Holdings holding) {
        holding.setCreatedAt(java.time.Instant.now());
        holding.setUpdatedAt(java.time.Instant.now());
        return holdings.save(holding);
    }

    @PutMapping("/{id}")
    ResponseEntity<Holdings> update(@PathVariable UUID id, @Valid @RequestBody Holdings incoming) {
        return holdings.findById(id)
            .map(holding -> {
                holding.setName(incoming.getName());
                holding.setSymbol(incoming.getSymbol());
                holding.setType(incoming.getType());
                holding.setQuantity(incoming.getQuantity());
                holding.setAvgPrice(incoming.getAvgPrice());
                holding.setLastPrice(incoming.getLastPrice());
                holding.setInvestedValue(incoming.getInvestedValue());
                holding.setCurrentValue(incoming.getCurrentValue());
                holding.setPnl(incoming.getPnl());
                holding.setPnlPct(incoming.getPnlPct());
                holding.setOwnerId(incoming.getOwnerId());
                holding.setIsin(incoming.getIsin());
                holding.setSector(incoming.getSector());
                holding.setSchemeType(incoming.getSchemeType());
                holding.setQuantityLongTerm(incoming.getQuantityLongTerm());
                holding.setQuantityPledgedMargin(incoming.getQuantityPledgedMargin());
                holding.setQuantityPledgedLoan(incoming.getQuantityPledgedLoan());
                holding.setUpdatedAt(java.time.Instant.now());
                return ResponseEntity.ok(holdings.save(holding));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable UUID id) {
        if (!holdings.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        holdings.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
