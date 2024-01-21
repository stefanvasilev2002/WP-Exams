package mk.ukim.finki.wp.sep2022.service;

import mk.ukim.finki.wp.sep2022.model.Match;
import mk.ukim.finki.wp.sep2022.model.MatchType;
import mk.ukim.finki.wp.sep2022.model.exceptions.InvalidMatchIdException;
import mk.ukim.finki.wp.sep2022.model.exceptions.InvalidMatchLocationIdException;

import java.util.List;

public interface MatchService {

    /**
     * @return List of all matches in the database
     */
    List<Match> listAllMatches();

    /**
     * returns the entity with the given id
     *
     * @param id The id of the entity that we want to obtain
     * @return
     * @throws InvalidMatchIdException when there is no entity with the given id
     */
    Match findById(Long id);

    /**
     * This method is used to create a new entity, and save it in the database.
     *
     * @param name
     * @param description
     * @param price
     * @param type
     * @param location
     * @return The entity that is created. The id should be generated when the entity is created.
     * @throws InvalidMatchLocationIdException when there is no location with the given id
     */
    Match create(String name, String description, Double price, MatchType type, Long location);

    /**
     * This method is used to modify an entity, and save it in the database.
     *
     * @param id The id of the entity that is being edited
     * @param name
     * @param description
     * @param price
     * @param type
     * @param location
     * @return The entity that is updated.
     * @throws InvalidMatchIdException  when there is no entity with the given id
     * @throws InvalidMatchLocationIdException when there is no location with the given id
     */
    Match update(Long id, String name, String description, Double price, MatchType type, Long location);

    /**
     * Method that should delete an entity. If the id is invalid, it should throw InvalidMatchIdException.
     *
     * @param id
     * @return The entity that is deleted.
     * @throws InvalidMatchIdException when there is no entity with the given id
     */
    Match delete(Long id);

    /**
     * Method for following a match. If the id is invalid, it should throw InvalidMatchIdException.
     *
     * @param id
     * @return The event that is deleted.
     * @throws InvalidMatchIdException when there is no event with the given id
     */
    Match follow(Long id);

    /**
     * The implementation of this method should use repository implementation for the filtering.
     *
     * @param price       Double that is used to filter the entities that have a price less than it.
     *                    This param can be null, and is not used for filtering in this case.
     * @param type   Used for filtering the entities that belong to that type.
     *                    This param can be null, and is not used for filtering in this case.
     * @return The entities that meet the filtering criteria
     */
    List<Match> listMatchesWithPriceLessThanAndType(Double price, MatchType type);
}
