package mk.ukim.finki.wp.september2021.service;

import mk.ukim.finki.wp.september2021.model.News;
import mk.ukim.finki.wp.september2021.model.NewsType;
import mk.ukim.finki.wp.september2021.model.exceptions.InvalidNewsIdException;
import mk.ukim.finki.wp.september2021.model.exceptions.InvalidNewsCategoryIdException;

import java.util.List;

public interface NewsService {

    /**
     * @return List of all news in the database
     */
    List<News> listAllNews();

    /**
     * returns the entity with the given id
     *
     * @param id The id of the entity that we want to obtain
     * @return
     * @throws InvalidNewsIdException when there is no entity with the given id
     */
    News findById(Long id);

    /**
     * This method is used to create a new entity, and save it in the database.
     *
     * @param name
     * @param description
     * @param price
     * @param type
     * @param category
     * @return The entity that is created. The id should be generated when the entity is created.
     * @throws InvalidNewsCategoryIdException when there is no category with the given id
     */
    News create(String name, String description, Double price, NewsType type, Long category);

    /**
     * This method is used to modify an entity, and save it in the database.
     *
     * @param id The id of the entity that is being edited
     * @param name
     * @param description
     * @param price
     * @param type
     * @param category
     * @return The entity that is updated.
     * @throws InvalidNewsIdException  when there is no entity with the given id
     * @throws InvalidNewsCategoryIdException when there is no category with the given id
     */
    News update(Long id, String name, String description, Double price, NewsType type, Long category);

    /**
     * Method that should delete an entity. If the id is invalid, it should throw InvalidNewsIdException.
     *
     * @param id
     * @return The entity that is deleted.
     * @throws InvalidNewsIdException when there is no entity with the given id
     */
    News delete(Long id);

    /**
     * Method for liking a news. If the id is invalid, it should throw InvalidNewsIdException.
     *
     * @param id
     * @return The event that is deleted.
     * @throws InvalidNewsIdException when there is no event with the given id
     */
    News like(Long id);

    /**
     * The implementation of this method should use repository implementation for the filtering.
     *
     * @param price       Double that is used to filter the entities that have a price less than it.
     *                    This param can be null, and is not used for filtering in this case.
     * @param type   Used for filtering the entities that belong to that type.
     *                    This param can be null, and is not used for filtering in this case.
     * @return The entities that meet the filtering criteria
     */
    List<News> listNewsWithPriceLessThanAndType(Double price, NewsType type);
}
