package mk.ukim.finki.wp.jan2022.g1.service;


import mk.ukim.finki.wp.jan2022.g1.model.Task;
import mk.ukim.finki.wp.jan2022.g1.model.TaskCategory;
import mk.ukim.finki.wp.jan2022.g1.model.exceptions.InvalidTaskIdException;
import mk.ukim.finki.wp.jan2022.g1.model.exceptions.InvalidUserIdException;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {

    /**
     * @return List of all entities in the database
     */
    List<Task> listAll();

    /**
     * returns the entity with the given id
     *
     * @param id The id of the entity that we want to obtain
     * @return
     * @throws InvalidTaskIdException when there is no entity with the given id
     */
    Task findById(Long id);

    /**
     * This method is used to create a new entity, and save it in the database.
     *
     * @return The entity that is created. The id should be generated when the entity is created.
     * @throws InvalidUserIdException when there is no category with the given id
     */
    Task create(String title, String description, TaskCategory category, List<Long> assignees, LocalDate dueDate);

    /**
     * This method is used to modify an entity, and save it in the database.
     *
     * @param id          The id of the entity that is being edited
     * @return The entity that is updated.
     * @throws InvalidTaskIdException when there is no entity with the given id
     * @throws InvalidUserIdException    when there is no category with the given id
     */
    Task update(Long id, String title, String description, TaskCategory category, List<Long> assignees);

    /**
     * Method that should delete an entity. If the id is invalid, it should throw InvalidTaskIdException.
     *
     * @param id
     * @return The entity that is deleted.
     * @throws InvalidTaskIdException when there is no entity with the given id
     */
    Task delete(Long id);

    /**
     * Method that should delete an entity. If the id is invalid, it should throw InvalidTaskIdException.
     *
     * @param id
     * @return The entity that should be marked as done.
     * @throws InvalidTaskIdException when there is no entity with the given id
     */
    Task markDone(Long id);

    /**
     * The implementation of this method should use repository implementation for the filtering.
     * All arguments are nullable. When an argument is null, we should not filter by that attribute
     *
     * @return The entities that meet the filtering criteria
     */
    List<Task> filter(Long assigneeId, Integer lessThanDayBeforeDueDate);
}
