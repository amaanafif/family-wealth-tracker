package com.familywealth.tracker.portfolio;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HoldingsImportService {

    private final HoldingsRepository repository;

    public void importExcel(MultipartFile file) {
        try (InputStream is = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            while (rows.hasNext()) {
                Row row = rows.next();

                if (!isValidRow(row)) continue;

                Holdings h = mapRow(row);

                repository.save(h);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to import Excel");
        }
    }

    // ✅ VALIDATION (CRITICAL FIX)
    private boolean isValidRow(Row row) {
        String name = getString(row.getCell(0));
        String isin = getString(row.getCell(2));

        if (name == null || name.isBlank()) return false;
        if (isin == null || isin.isBlank()) return false;

        // skip header rows
        if ("name".equalsIgnoreCase(name)) return false;
        if ("client id".equalsIgnoreCase(name)) return false;

        return true;
    }

    // ✅ MAPPING
    private Holdings mapRow(Row row) {
        Holdings h = new Holdings();

        h.setId(UUID.randomUUID());
        h.setOwnerId(UUID.fromString("11111111-1111-1111-1111-111111111111"));

        h.setName(getString(row.getCell(0)));
        h.setSymbol(getString(row.getCell(1)));
        h.setIsin(getString(row.getCell(2)));
        h.setType(getString(row.getCell(3)));
        h.setSector(getString(row.getCell(4)));
        h.setSchemeType(getString(row.getCell(5)));

        h.setQuantity(getNumber(row.getCell(6)));
        h.setAvgPrice(getNumber(row.getCell(7)));
        h.setLastPrice(getNumber(row.getCell(8)));

        BigDecimal qty = defaultZero(h.getQuantity());
        BigDecimal avg = defaultZero(h.getAvgPrice());
        BigDecimal last = defaultZero(h.getLastPrice());

        h.setInvestedValue(qty.multiply(avg));
        h.setCurrentValue(qty.multiply(last));
        h.setPnl(h.getCurrentValue().subtract(h.getInvestedValue()));

        if (h.getInvestedValue().compareTo(BigDecimal.ZERO) != 0) {
            h.setPnlPct(
                    h.getPnl()
                            .divide(h.getInvestedValue(), 4, BigDecimal.ROUND_HALF_UP)
                            .multiply(BigDecimal.valueOf(100))
            );
        } else {
            h.setPnlPct(BigDecimal.ZERO);
        }

        h.setCreatedAt(Instant.from(LocalDateTime.now()));
        h.setUpdatedAt(Instant.from(LocalDateTime.now()));

        return h;
    }

    // ✅ SAFE STRING PARSER
    private String getString(Cell cell) {
        if (cell == null) return null;

        try {
            return switch (cell.getCellType()) {
                case STRING -> cell.getStringCellValue().trim();
                case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
                default -> null;
            };
        } catch (Exception e) {
            return null;
        }
    }

    // ✅ SAFE NUMBER PARSER
    private BigDecimal getNumber(Cell cell) {
        if (cell == null) return BigDecimal.ZERO;

        try {
            return switch (cell.getCellType()) {
                case NUMERIC -> BigDecimal.valueOf(cell.getNumericCellValue());
                case STRING -> new BigDecimal(cell.getStringCellValue());
                default -> BigDecimal.ZERO;
            };
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal defaultZero(BigDecimal val) {
        return val == null ? BigDecimal.ZERO : val;
    }
}