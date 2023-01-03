package com.trilhaback.pedro.controller;

import com.trilhaback.pedro.domain.Dimension;
import com.trilhaback.pedro.domain.Repository;
import com.trilhaback.pedro.service.DimensionService;
import com.trilhaback.pedro.service.dto.form.DimensionForm;
import com.trilhaback.pedro.service.dto.view.DimensionView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
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
    public ResponseEntity<DimensionView> findById(@PathVariable Long id){
        return new ResponseEntity<>(
                dimensionService.findById(id),
                HttpStatus.OK
        );
    }


}
