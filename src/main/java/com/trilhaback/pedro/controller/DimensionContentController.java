package com.trilhaback.pedro.controller;

import com.trilhaback.pedro.domain.DimensionContent;
import com.trilhaback.pedro.service.DimensionContentService;
import com.trilhaback.pedro.service.dto.form.DimensionContentForm;
import com.trilhaback.pedro.service.dto.view.DimensionContentView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/dimensionContent")
public class DimensionContentController {

    @Autowired
    public DimensionContentService dimensionContentService;

    @PostMapping(value = "/{dimensionId}")
    @Transactional
    public ResponseEntity<?> create(@PathVariable Long dimensionId
            , @RequestBody DimensionContentForm dimensionContentForm) {
        dimensionContentService.create(dimensionContentForm, dimensionId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping(value = "/{dimensionId}")
    @Transactional
    public ResponseEntity<?> update(@PathVariable Long dimensionId
            , @RequestBody DimensionContentForm dimensionContentForm) {
        dimensionContentService.update(dimensionContentForm, dimensionId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/{dimensionId}/{dimensionContentId}")
    public ResponseEntity<DimensionContentView> findById(@PathVariable Long dimensionId, @PathVariable String dimensionContentId) {
        return new ResponseEntity<>(
                dimensionContentService.findById(dimensionId, dimensionContentId),
                HttpStatus.OK);

    }

    @GetMapping(value = "/{dimensionId}")
    public ResponseEntity<List<DimensionContentView>> findAll(@PathVariable Long dimensionId) {
        return new ResponseEntity<>(
                dimensionContentService.findAll(dimensionId),
                HttpStatus.OK
        );
    }

    @DeleteMapping(value = "/{dimensionId}/{dimensionContentId}")
    @Transactional
    public ResponseEntity<?> deleteById(@PathVariable Long dimensionId, @PathVariable String dimensionContentId) {
        dimensionContentService.deleteById(dimensionId, dimensionContentId);
        return new ResponseEntity<>(
                HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "/addRelationship/{dimensionId}")
    @Transactional
    public ResponseEntity<?> addDimensionRelationship(@PathVariable Long dimensionId
            , @RequestBody DimensionContentForm dimensionContentForm) {
        dimensionContentService.addDimensionRelationship(dimensionId, dimensionContentForm);
        return new ResponseEntity<>(
                HttpStatus.OK);
    }
}



