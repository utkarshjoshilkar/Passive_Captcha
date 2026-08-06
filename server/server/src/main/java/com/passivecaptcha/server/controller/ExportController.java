package com.passivecaptcha.server.controller;

import com.passivecaptcha.server.service.CsvExportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Provides ML-ready CSV export of the behavioral feature dataset.
 *
 * GET /api/v1/export/csv           — export all sessions
 * GET /api/v1/export/csv?label=HUMAN   — only HUMAN-labelled sessions
 * GET /api/v1/export/csv?label=BOT     — only BOT-labelled sessions
 * GET /api/v1/export/csv?label=UNKNOWN — only UNKNOWN sessions
 *
 * The CSV contains all 35+ features plus the label column (target variable).
 * It can be imported directly into pandas, scikit-learn, Weka, or R.
 */
@RestController
@RequestMapping("/api/v1/export")
@CrossOrigin(origins = "*")
public class ExportController {

    private final CsvExportService csvExportService;

    public ExportController(CsvExportService csvExportService) {
        this.csvExportService = csvExportService;
    }

    @GetMapping("/csv")
    public ResponseEntity<byte[]> exportCsv(
            @RequestParam(required = false) String label) {

        String csv = csvExportService.exportCsv(label);
        byte[] bytes = csv.getBytes(java.nio.charset.StandardCharsets.UTF_8);

        String filename = label != null
                ? "passive_captcha_" + label.toLowerCase() + ".csv"
                : "passive_captcha_dataset.csv";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("text/csv"))
                .contentLength(bytes.length)
                .body(bytes);
    }
}
