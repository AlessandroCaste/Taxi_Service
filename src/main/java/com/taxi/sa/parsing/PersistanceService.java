package com.taxi.sa.parsing;

import com.taxi.sa.parsing.input.InputCheckpoint;
import com.taxi.sa.parsing.input.InputWall;
import com.taxi.sa.parsing.output.Checkpoint;
import com.taxi.sa.parsing.output.CityMap;
import com.taxi.sa.parsing.output.Wall;
import com.taxi.sa.parsing.users.Taxi;
import com.taxi.sa.repositories.CheckpointRepository;
import com.taxi.sa.repositories.MapRepository;
import com.taxi.sa.repositories.TaxiRepository;
import com.taxi.sa.repositories.WallRepository;
import org.hibernate.MappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void save(InputMapInterface inputMap) {

        // Creating the new CityMap, eventually overwriting any old entry
        String cityId = inputMap.getCityId();
        int width = inputMap.getWidth();
        int height = inputMap.getHeight();
        CityMap newEntry = new CityMap(cityId,width,height);
        Optional<CityMap> previousEntry = mapRepository.findById(cityId);
        if(previousEntry.isPresent()) {
            previousEntry.get().clear();
            mapRepository.delete(previousEntry.get());
        }
        mapRepository.save(newEntry);

        // Creating and linking walls
        for(InputWall inputWall: inputMap.getWalls()) {
            Wall outputWall = new Wall(inputWall);
            newEntry.addWall(outputWall);
            wallRepository.save(outputWall);
        }

        // Creating and linking checkpoints
        for(InputCheckpoint inputCheckpoint: inputMap.getCheckpoints()) {
            Checkpoint outputCheckpoint = new Checkpoint(inputCheckpoint);
            newEntry.addCheckpoint(outputCheckpoint);
            checkpointRepository.save(outputCheckpoint);
        }

    }

    public void storeTaxi(String taxiId, String city, Coordinate position) {
        if(mapRepository.findById(city).isEmpty())
            throw new MappingException("no city named '" + city + "' exists");
        CityMap cityMap = mapRepository.findById(city).get();
        Taxi taxi = new Taxi(taxiId, position, cityMap);
        if(taxiRepository.findById(taxiId).isPresent())
            taxiRepository.delete(taxi);
        taxiRepository.save(taxi);
    }

}

