package com.familywealth.tracker.asset;

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
@RequestMapping("/api/assets")
public class AssetController {
    private final AssetRepository assets;

    public AssetController(AssetRepository assets) {
        this.assets = assets;
    }

    @GetMapping
    List<Asset> list() {
        return assets.findAll();
    }

    @PostMapping
    Asset create(@Valid @RequestBody Asset asset) {
        asset.touch();
        return assets.save(asset);
    }

    @PutMapping("/{id}")
    ResponseEntity<Asset> update(@PathVariable Long id, @Valid @RequestBody Asset incoming) {
        return assets.findById(id)
            .map(asset -> {
                asset.setName(incoming.getName());
                asset.setSymbol(incoming.getSymbol());
                asset.setType(incoming.getType());
                asset.setBucket(incoming.getBucket());
                asset.setQuantity(incoming.getQuantity());
                asset.setManualPrice(incoming.getManualPrice());
                asset.touch();
                return ResponseEntity.ok(assets.save(asset));
            })
            .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!assets.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        assets.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
