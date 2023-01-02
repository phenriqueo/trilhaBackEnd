package com.trilhaback.pedro.controller;

import com.trilhaback.pedro.domain.Dimension;
import com.trilhaback.pedro.service.DimensionService;
import com.trilhaback.pedro.service.dto.form.DimensionForm;
import com.trilhaback.pedro.service.dto.view.DimensionView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping(value = "/dimension")
public class DimensionController {

    @Autowired
    private DimensionService dimensionService;

    @PostMapping
    public ResponseEntity<DimensionView> create(@RequestBody DimensionForm dimensionForm) {
        return new ResponseEntity<>(
                dimensionService.create(dimensionForm),
                HttpStatus.CREATED
        );
    }
}
