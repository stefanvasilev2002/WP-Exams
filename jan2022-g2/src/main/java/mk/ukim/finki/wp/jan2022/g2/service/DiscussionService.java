package mk.ukim.finki.wp.jan2022.g2.service;

import mk.ukim.finki.wp.jan2022.g2.model.Discussion;
import mk.ukim.finki.wp.jan2022.g2.model.DiscussionTag;
import mk.ukim.finki.wp.jan2022.g2.model.exceptions.InvalidDiscussionIdException;
import mk.ukim.finki.wp.jan2022.g2.model.exceptions.InvalidUserIdException;

import java.time.LocalDate;
import java.util.List;

public interface DiscussionService {

    /**
     * @return List of all entities in the database
     */
    List<Discussion> listAll();

    /**
     * returns the entity with the given id
     *
     * @param id The id of the entity that we want to obtain
     * @return
     * @throws InvalidDiscussionIdException when there is no entity with the given id
     */
    Discussion findById(Long id);

    /**
     * This method is used to create a new entity, and save it in the database.
     *
     * @return The entity that is created. The id should be generated when the entity is created.
     * @throws InvalidUserIdException when there is no user with the given id
     */
    Discussion create(String title, String description, DiscussionTag discussionTag, List<Long> participants, LocalDate dueDate);

    /**
     * This method is used to modify an entity, and save it in the database.
     *
     * @param id          The id of the entity that is being edited
     * @return The entity that is updated.
     * @throws InvalidDiscussionIdException when there is no entity with the given id
     * @throws InvalidUserIdException    when there is no user with the given id
     */
    Discussion update(Long id, String title, String description, DiscussionTag discussionTag, List<Long> participants);

    /**
     * Method that should delete an entity. If the id is invalid, it should throw InvalidDiscussionIdException.
     *
     * @param id
     * @return The entity that is deleted.
     * @throws InvalidDiscussionIdException when there is no entity with the given id
     */
    Discussion delete(Long id);

    /**
     * Method that should make an entity marked as popular. If the id is invalid, it should throw InvalidDiscussionIdException.
     *
     * @param id
     * @return The entity that should be marked as popular.
     * @throws InvalidDiscussionIdException when there is no entity with the given id
     */
    Discussion markPopular(Long id);

    /**
     * The implementation of this method should use repository implementation for the filtering.
     * All arguments are nullable. When an argument is null, we should not filter by that attribute
     *
     * @return The entities that meet the filtering criteria
     */
    List<Discussion> filter(Long participantId, Integer daysUntilClosing);
}
