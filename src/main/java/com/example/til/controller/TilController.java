package com.example.til.controller;

import com.example.til.dto.TilCreateRequest;
import com.example.til.dto.TilDetail;
import com.example.til.dto.TilListItem;
import com.example.til.service.TilService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tils")
public class TilController {

    private final TilService tilService;

    public TilController(TilService tilService) {
        this.tilService = tilService;
    }

    @GetMapping
    public ResponseEntity<List<TilListItem>> list() {
        return ResponseEntity.ok(tilService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TilDetail> detail(@PathVariable Long id) {
        return ResponseEntity.ok(tilService.findById(id));
    }

    @PostMapping
    public ResponseEntity<TilDetail> create(@RequestBody @Valid TilCreateRequest req) {
        TilDetail created = tilService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tilService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
