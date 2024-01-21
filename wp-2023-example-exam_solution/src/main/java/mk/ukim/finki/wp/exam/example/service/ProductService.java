package mk.ukim.finki.wp.exam.example.service;

import mk.ukim.finki.wp.exam.example.model.Category;
import mk.ukim.finki.wp.exam.example.model.Product;
import mk.ukim.finki.wp.exam.example.model.User;

import java.util.List;
import java.util.Optional;

/**
 * 20 points
 */
public interface ProductService {

    /**
     * @return List of all products in the database
     */
    List<Product> listAllProducts();

    /**
     * returns an optional of the product with the given id, or an empty optional when there is no product with this id
     *
     * @param id The id of the product that we want to obtain
     * @return
     * @throws mk.ukim.finki.wp.exam.example.model.exceptions.InvalidProductIdException when there is no product with the given id
     */
    Product findById(Long id);

    /**
     * This method is used to create a new product, and save it in the database.
     *
     * @param name
     * @param price
     * @param quantity
     * @param categories The list of category ids to which the product belongs.
     * @return The product that is created. The id should be generated when the product is created.
     * @throws mk.ukim.finki.wp.exam.example.model.exceptions.InvalidCategoryIdException when there is no category with the given id
     */
    Product create(String name, Double price, Integer quantity, List<Long> categories);

    /**
     * This method is used to create a new product, and save it in the database.
     *
     * @param id         The id of the product that is being edited
     * @param name
     * @param price
     * @param quantity
     * @param categories The list of category ids to which the product belongs.
     * @return The product that is updated.
     * @throws mk.ukim.finki.wp.exam.example.model.exceptions.InvalidProductIdException  when there is no product with the given id
     * @throws mk.ukim.finki.wp.exam.example.model.exceptions.InvalidCategoryIdException when there is no category with the given id
     */
    Product update(Long id, String name, Double price, Integer quantity, List<Long> categories);

    /**
     * Method that should delete a product. If the id is invalid, it should throw InvalidProductIdException.
     *
     * @param id
     * @return The product that is deleted.
     * @throws mk.ukim.finki.wp.exam.example.model.exceptions.InvalidProductIdException when there is no product with the given id
     */
    Product delete(Long id);

    /**
     * The implementation of this method should use repository implementation for the filtering.
     *
     * @param name       String that is used to filter the products that contain it in their name.
     *                   This param can be null, and is not used for filtering in this case.
     * @param categoryId Used for filtering the products that belong to the category with id=categoryId.
     *                   This param can be null, and is not used for filtering in this case.
     * @return The products that meet the filtering criteria
     */
    List<Product> listProductsByNameAndCategory(String name, Long categoryId);

}
