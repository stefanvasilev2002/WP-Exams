package mk.ukim.finki.wp.exam.example.service.impl;

import mk.ukim.finki.wp.exam.example.model.Category;
import mk.ukim.finki.wp.exam.example.model.Product;
import mk.ukim.finki.wp.exam.example.model.exceptions.InvalidCategoryIdException;
import mk.ukim.finki.wp.exam.example.model.exceptions.InvalidProductIdException;
import mk.ukim.finki.wp.exam.example.repository.ProductRepository;
import mk.ukim.finki.wp.exam.example.service.CategoryService;
import mk.ukim.finki.wp.exam.example.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryService categoryService;

    public ProductServiceImpl(ProductRepository productRepository, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
    }

    @Override
    public List<Product> listAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow(InvalidProductIdException::new);
    }

    @Override
    public Product create(String name, Double price, Integer quantity, List<Long> categories) {
        return productRepository.save(new Product(
                name,
                price,
                quantity,
                categories.stream().map(categoryService::findById).collect(Collectors.toList())
        ));
    }

    @Override
    public Product update(Long id, String name, Double price, Integer quantity, List<Long> categories) {
        Product product = findById(id);

        product.setName(name);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setCategories(categories.stream().map(categoryService::findById).collect(Collectors.toList()));

        return productRepository.save(product);
    }

    @Override
    public Product delete(Long id) {
        Product product = findById(id);
        productRepository.delete(product);
        return product;
    }

    @Override
    public List<Product> listProductsByNameAndCategory(String name, Long categoryId) {
        Category category = categoryId != null ? categoryService.findById(categoryId) : null;
        String nameLikePattern = "%" + name + "%";
        if (name != null && category != null) {
            return productRepository.findAllByNameLikeAndCategoriesContaining(nameLikePattern, category);
        }

        if (name != null) {
            return productRepository.findAllByNameLike(nameLikePattern);
        }

        if (category != null) {
            return productRepository.findAllByCategoriesContaining(category);
        }

        return listAllProducts();
    }
}
