package com.karlo.springrestdocs.web.controller;

import com.karlo.springrestdocs.repositories.BeerRepository;
import com.karlo.springrestdocs.web.mapper.BeerMapper;
import com.karlo.springrestdocs.web.model.BeerDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RequestMapping("/api/v1/beer")
@RestController
public class BeerController {

    private final BeerMapper beerMapper;
    private final BeerRepository beerRepository;

    @GetMapping("/{beerId}")
    public ResponseEntity<BeerDTO> getBeerById(@PathVariable("beerId") UUID beerId){
        return new ResponseEntity<>(beerMapper.BeerToBeerDTO(beerRepository.findById(beerId).get()), HttpStatus.OK);
    }
}
