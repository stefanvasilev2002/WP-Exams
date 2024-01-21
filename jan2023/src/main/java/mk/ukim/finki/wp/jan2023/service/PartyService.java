package mk.ukim.finki.wp.jan2023.service;

import mk.ukim.finki.wp.jan2023.model.exceptions.InvalidPartyIdException;
import mk.ukim.finki.wp.jan2023.model.Party;

import java.util.List;

public interface PartyService {

    /**
     * returns the party with the given id
     *
     * @param id The id of the party that we want to obtain
     * @return
     * @throws InvalidPartyIdException when there is no party with the given id
     */
    Party findById(Long id);

    /**
     * @return List of all parties in the database
     */
    List<Party> listAll();

    /**
     * This method is used to create a new party, and save it in the database.
     *
     * @param name
     * @return The party that is created. The id should be generated when the party is created.
     */
    Party create(String name);
}
