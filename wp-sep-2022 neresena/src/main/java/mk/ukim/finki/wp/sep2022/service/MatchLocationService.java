package mk.ukim.finki.wp.sep2022.service;

import mk.ukim.finki.wp.sep2022.model.MatchLocation;
import mk.ukim.finki.wp.sep2022.model.exceptions.InvalidMatchLocationIdException;

import java.util.List;

public interface MatchLocationService {

    /**
     * returns the location with the given id
     *
     * @param id The id of the location that we want to obtain
     * @return
     * @throws InvalidMatchLocationIdException when there is no  location with the given id
     */
    MatchLocation findById(Long id);

    /**
     * @return List of all  locations in the database
     */
    List<MatchLocation> listAll();

    /**
     * This method is used to create a new  location, and save it in the database.
     *
     * @param name
     * @return The location that is created. The id should be generated when the match location is created.
     */
    MatchLocation create(String name);
}
