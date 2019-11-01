package com.karlo.springrestdocs.web.mapper;

import com.karlo.springrestdocs.domain.Beer;
import com.karlo.springrestdocs.web.model.BeerDTO;
import org.mapstruct.Mapper;

@Mapper(uses = {DateMapper.class})
public interface BeerMapper {
    BeerDTO BeerToBeerDTO(Beer beer);

    Beer BeerDTOToBeer(BeerDTO beerDTO);
}
