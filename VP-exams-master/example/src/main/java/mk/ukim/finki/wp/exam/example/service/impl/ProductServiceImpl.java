package mk.ukim.finki.wp.exam.example.service.impl;

import mk.ukim.finki.wp.exam.example.model.Product;
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
        return productRepository.save(new Product(name, price, quantity, categories.stream().map(categoryService::findById).collect(Collectors.toList())));
    }

    @Override
    public Product update(Long id, String name, Double price, Integer quantity, List<Long> categories) {
        Product p = findById(id);

        p.setName(name);
        p.setPrice(price);
        p.setQuantity(quantity);
        p.setCategories(categories.stream().map(categoryService::findById).collect(Collectors.toList()));

        return productRepository.save(p);
    }

    @Override
    public Product delete(Long id) {
        Product p = findById(id);

        productRepository.delete(p);

        return p;
    }

    @Override
    public List<Product> listProductsByNameAndCategory(String name, Long categoryId) {
        if (name != null && categoryId != null) {
            System.out.println("e");
            return productRepository.findAllByNameContainsAndCategoriesContains(name, categoryService.findById(categoryId));
        } else if (name == null && categoryId == null) {
            return listAllProducts();
        } else if (name != null) {
            return productRepository.findAllByNameContains(name);
        } else {
            return productRepository.findAllByCategoriesContains(categoryService.findById(categoryId));
        }
    }
}
