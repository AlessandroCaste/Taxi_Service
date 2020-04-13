package com.taxi.sa.services;

import com.taxi.sa.parsing.InputMapInterface;
import com.taxi.sa.parsing.input.city.InputCheckpoint;
import com.taxi.sa.parsing.input.city.InputWall;
import com.taxi.sa.parsing.input.user.InputCoordinate;
import com.taxi.sa.parsing.output.city.Checkpoint;
import com.taxi.sa.parsing.output.city.CityMap;
import com.taxi.sa.parsing.output.city.Wall;
import com.taxi.sa.parsing.output.user.Taxi;
import com.taxi.sa.repositories.CheckpointRepository;
import com.taxi.sa.repositories.MapRepository;
import com.taxi.sa.repositories.TaxiRepository;
import com.taxi.sa.repositories.WallRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import java.util.Optional;

// Verifies input data is sound
@Service
public class PersistanceService {

    private MapRepository mapRepository;
    private WallRepository wallRepository;
    private CheckpointRepository checkpointRepository;
    private TaxiRepository taxiRepository;

    @Autowired
    private void setMapRepository(MapRepository mapRepository){
        this.mapRepository = mapRepository;
    }

    @Autowired
    private void setWallRepository(WallRepository wallRepository) {
        this.wallRepository = wallRepository;
    }

    @Autowired
    private void setCheckpointRepository(CheckpointRepository checkpointRepository) {
        this.checkpointRepository = checkpointRepository;
    }

    @Autowired
    private void setTaxiRepository(TaxiRepository taxiRepository) {
        this.taxiRepository = taxiRepository;
    }

    public void save(InputMapInterface inputMap) throws PersistenceException {
        // Creating the new CityMap, deleting any old entry with the same name
        String cityId = inputMap.getCityId().toLowerCase();
        int width = inputMap.getWidth();
        int height = inputMap.getHeight();
        CityMap newEntry = new CityMap(cityId,width,height);
        Optional<CityMap> previousEntry = mapRepository.findById(cityId);
        if(previousEntry.isPresent()) {
            previousEntry.get().clear();
            mapRepository.delete(previousEntry.get());
        }

        // Creating and linking walls
        for(InputWall inputWall: inputMap.getWalls()) {
            Wall outputWall = new Wall(inputWall);
            newEntry.addWall(outputWall);
        }

        // Creating and linking checkpoints
        for(InputCheckpoint inputCheckpoint: inputMap.getCheckpoints()) {
            Checkpoint outputCheckpoint = new Checkpoint(inputCheckpoint);
            newEntry.addCheckpoint(outputCheckpoint);
        }

        // Storing the new map
        mapRepository.save(newEntry);
    }

    public void save(String taxiId, String city, InputCoordinate position) throws PersistenceException {
        CityMap referencedMap = mapRepository.findById(city).get();
        if(taxiRepository.findById(taxiId).isPresent())
            taxiRepository.deleteById(taxiId);
        Taxi taxi = new Taxi(taxiId, position);
        referencedMap.addTaxi(taxi);
        mapRepository.save(referencedMap);
    }

}

