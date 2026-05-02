package com.familywealth.tracker.liability;

import jakarta.validation.Valid;
import java.util.List;
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
@RequestMapping("/api/liabilities")
public class LiabilityController {
    private final LiabilityRepository liabilities;

    public LiabilityController(LiabilityRepository liabilities) {
        this.liabilities = liabilities;
    }

    @GetMapping
    List<Liability> list() {
        return liabilities.findAll();
    }

    @PostMapping
    Liability create(@Valid @RequestBody Liability liability) {
        liability.touch();
        return liabilities.save(liability);
    }

    @PutMapping("/{id}")
    ResponseEntity<Liability> update(@PathVariable Long id, @Valid @RequestBody Liability incoming) {
        return liabilities.findById(id)
            .map(liability -> {
                liability.setName(incoming.getName());
                liability.setOutstandingAmount(incoming.getOutstandingAmount());
                liability.setInterestRate(incoming.getInterestRate());
                liability.touch();
                return ResponseEntity.ok(liabilities.save(liability));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!liabilities.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        liabilities.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
