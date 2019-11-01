package com.karlo.springrestdocs.bootstrap;

import com.karlo.springrestdocs.domain.Beer;
import com.karlo.springrestdocs.repositories.BeerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BeerLoader implements CommandLineRunner {
    private final BeerRepository beerRepository;

    public BeerLoader(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadBeerObjects();
    }

    private void loadBeerObjects() {
        if(beerRepository.count() == 0){
            beerRepository.save(Beer.builder()
                    .beerName("Heineken")
                    .beerStyle("Lager")
                    .quantityToBrew(200)
                    .minOnHand(12)
                    .upc(123123123L)
                    .price(new BigDecimal("12.5"))
                    .build());

            beerRepository.save(Beer.builder()
                    .beerName("Paulaner")
                    .beerStyle("Lager")
                    .quantityToBrew(200)
                    .minOnHand(12)
                    .upc(120893012L)
                    .price(new BigDecimal("15.5"))
                    .build());
        }
    }
}
