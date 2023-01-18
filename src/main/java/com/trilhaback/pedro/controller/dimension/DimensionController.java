package com.trilhaback.pedro.controller.dimension;

import com.trilhaback.pedro.domain.dimension.TreeContent;
import com.trilhaback.pedro.service.dimension.service.DimensionService;
import com.trilhaback.pedro.service.dimension.dto.form.DimensionForm;
import com.trilhaback.pedro.service.dimension.dto.view.DimensionView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/dimension")
public class DimensionController {

    @Autowired
    private DimensionService dimensionService;

    @PostMapping
    @Transactional
    public ResponseEntity<DimensionView> create(@RequestBody DimensionForm dimensionForm) {
        return new ResponseEntity<>(
                dimensionService.create(dimensionForm),
                HttpStatus.CREATED
        );
    }

    @PostMapping(value = "/save")
    @Transactional
    public ResponseEntity<DimensionView> update(@RequestBody DimensionForm dimensionForm) {
        return new ResponseEntity<>(
                dimensionService.update(dimensionForm),
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<DimensionView> findById(@PathVariable Long id) {
        return new ResponseEntity<>(
                dimensionService.findById(id),
                HttpStatus.OK
        );
    }

    @GetMapping
    public ResponseEntity<List<DimensionView>> findAll() {
        return new ResponseEntity<>(
                dimensionService.findAll(),
                HttpStatus.OK
        );
    }

    @DeleteMapping(value = "/{id}")
    @Transactional
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        dimensionService.deleteById(id);
        return new ResponseEntity<>(
                HttpStatus.NO_CONTENT
        );
    }

    @PutMapping(value = "/addparent")
    @Transactional
    public ResponseEntity<?> addDimensionSon(@RequestBody DimensionForm dimensionForm) {
        dimensionService.addDimensionSon(dimensionForm);
        return new ResponseEntity<>(
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/tree/{id}")
    public ResponseEntity<DimensionView> findTreeById(@PathVariable Long id) {
        return new ResponseEntity<>(
                dimensionService.findTreeById(id),
                HttpStatus.OK
        );
    }

    @PutMapping(value = "/removeSonId/")
    public ResponseEntity<?> removeSonId(@RequestBody DimensionForm dimensionForm) {
        dimensionService.removeSonId(dimensionForm);
        return new ResponseEntity<>(
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/dimensionTable/{id}")
    public ResponseEntity<TreeContent> getDimensionContentTable(@PathVariable Long id) {
        return new ResponseEntity<>(
                dimensionService.getDimensionContentTable(id),
                HttpStatus.OK
        );
    }

    @GetMapping(value = "/test")
    public String test() {
        return dimensionService.getQueryForDimensionContentTable(1000l);
    }

}
