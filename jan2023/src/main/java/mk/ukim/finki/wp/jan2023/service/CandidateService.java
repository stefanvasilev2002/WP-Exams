package mk.ukim.finki.wp.jan2023.service;

import mk.ukim.finki.wp.jan2023.model.Candidate;
import mk.ukim.finki.wp.jan2023.model.Gender;
import mk.ukim.finki.wp.jan2023.model.exceptions.InvalidCandidateIdException;
import mk.ukim.finki.wp.jan2023.model.exceptions.InvalidPartyIdException;

import java.time.LocalDate;
import java.util.List;

public interface CandidateService {

    /**
     * @return List of all candidates in the database
     */
    List<Candidate> listAllCandidates();

    /**
     * returns the candidate with the given id
     *
     * @param id The id of the candidate that we want to obtain
     * @return
     * @throws InvalidCandidateIdException when there is no candidate with the given id
     */
    Candidate findById(Long id);

    /**
     * This method is used to create a new candidate, and save it in the database.
     *
     * @param name
     * @param bio
     * @param dateOfBirth
     * @param gender
     * @param party
     * @return The candidate that is created. The id should be generated when the candidate is created.
     * @throws InvalidPartyIdException when there is no party with the given id
     */
    Candidate create(String name, String bio, LocalDate dateOfBirth, Gender gender, Long party);

    /**
     * This method is used to update a candidate, and save it in the database.
     *
     * @param id The id of the candidate that is being edited
     * @param name
     * @param bio
     * @param dateOfBirth
     * @param gender
     * @param party
     * @return The candidate that is updated.
     * @throws InvalidCandidateIdException when there is no candidate with the given id
     * @throws InvalidPartyIdException when there is no party with the given id
     */
    Candidate update(Long id, String name, String bio, LocalDate dateOfBirth, Gender gender, Long party);

    /**
     * Method that should delete a candidate. If the id is invalid, it should throw InvalidCandidateIdException.
     *
     * @param id
     * @return The candidate that is deleted.
     * @throws InvalidCandidateIdException when there is no candidate with the given id
     */
    Candidate delete(Long id);

    /**
     * Method that should vote for a candidate. If the id is invalid, it should throw InvalidCandidateIdException.
     *
     * @param id
     * @return The candidate that is voted for.
     * @throws InvalidCandidateIdException when there is no candidate with the given id
     */
    Candidate vote(Long id);

    /**
     * The implementation of this method should use repository implementation for the filtering.
     *
     * @param yearsMoreThan that is used to filter the candidates who are older than this value.
     *                        This param can be null, and is not used for filtering in this case.
     * @param gender        Used for filtering the candidates gender.
     *                        This param can be null, and is not used for filtering in this case.
     * @return The candidates that meet the filtering criteria
     */
    List<Candidate> listCandidatesYearsMoreThanAndGender(Integer yearsMoreThan, Gender gender);
}
