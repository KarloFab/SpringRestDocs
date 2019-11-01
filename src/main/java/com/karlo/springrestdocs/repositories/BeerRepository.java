package com.karlo.springrestdocs.repositories;

import com.karlo.springrestdocs.domain.Beer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BeerRepository extends JpaRepository<Beer, UUID> {
}
