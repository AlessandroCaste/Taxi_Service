package com.taxi.sa.repository;

import com.taxi.sa.parsing.city.Checkpoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckpointRepository extends JpaRepository<Checkpoint, String> {
}
