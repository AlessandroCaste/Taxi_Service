package com.taxi.sa.repositories;

import com.taxi.sa.parsing.output.Checkpoint;
import com.taxi.sa.parsing.output.CityMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CheckpointRepository extends JpaRepository<Checkpoint, String> {
    Optional<List<Checkpoint>> findAllByCityMap(CityMap cityMap);
}
