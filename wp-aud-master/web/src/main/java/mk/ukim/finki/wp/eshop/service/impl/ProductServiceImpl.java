package mk.ukim.finki.wp.eshop.service.impl;

import mk.ukim.finki.wp.eshop.model.Category;
import mk.ukim.finki.wp.eshop.model.dto.ProductDto;
import mk.ukim.finki.wp.eshop.model.exceptions.CategoryNotFoundException;
import mk.ukim.finki.wp.eshop.model.Manufacturer;
import mk.ukim.finki.wp.eshop.model.Product;
import mk.ukim.finki.wp.eshop.model.exceptions.ManufacturerNotFoundException;
import mk.ukim.finki.wp.eshop.model.exceptions.ProductNotFoundException;
import mk.ukim.finki.wp.eshop.repository.impl.InMemoryCategoryRepository;
import mk.ukim.finki.wp.eshop.repository.impl.InMemoryManufacturerRepository;
import mk.ukim.finki.wp.eshop.repository.impl.InMemoryProductRepository;
import mk.ukim.finki.wp.eshop.repository.jpa.CategoryRepository;
import mk.ukim.finki.wp.eshop.repository.jpa.ManufacturerRepository;
import mk.ukim.finki.wp.eshop.repository.jpa.ProductRepository;
import mk.ukim.finki.wp.eshop.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                              ManufacturerRepository manufacturerRepository,
                              CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.manufacturerRepository = manufacturerRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public List<Product> findAll() {
        return this.productRepository.findAll();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return this.productRepository.findById(id);
    }

    @Override
    public Optional<Product> findByName(String name) {
        return this.productRepository.findByName(name);
    }

    @Override
    @Transactional
    public Optional<Product> save(String name, Double price, Integer quantity, Long categoryId, Long manufacturerId) {
        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        Manufacturer manufacturer = this.manufacturerRepository.findById(manufacturerId)
                .orElseThrow(() -> new ManufacturerNotFoundException(manufacturerId));

        this.productRepository.deleteByName(name);
        return Optional.of(this.productRepository.save(new Product(name, price, quantity, category, manufacturer)));
    }

    @Override
    public Optional<Product> save(ProductDto productDto) {
        Category category = this.categoryRepository.findById(productDto.getCategory())
                .orElseThrow(() -> new CategoryNotFoundException(productDto.getCategory()));
        Manufacturer manufacturer = this.manufacturerRepository.findById(productDto.getManufacturer())
                .orElseThrow(() -> new ManufacturerNotFoundException(productDto.getManufacturer()));

        this.productRepository.deleteByName(productDto.getName());
        return Optional.of(this.productRepository.save(new Product(productDto.getName(), productDto.getPrice(), productDto.getQuantity(), category, manufacturer)));
    }

    @Override
    @Transactional
    public Optional<Product> edit(Long id, String name, Double price, Integer quantity, Long categoryId, Long manufacturerId) {

        Product product = this.productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

        product.setName(name);
        product.setPrice(price);
        product.setQuantity(quantity);

        Category category = this.categoryRepository.findById(categoryId)
                .orElseThrow(() -> new CategoryNotFoundException(categoryId));
        product.setCategory(category);

        Manufacturer manufacturer = this.manufacturerRepository.findById(manufacturerId)
                .orElseThrow(() -> new ManufacturerNotFoundException(manufacturerId));
        product.setManufacturer(manufacturer);

        return Optional.of(this.productRepository.save(product));
    }

    @Override
    public Optional<Product> edit(Long id, ProductDto productDto) {
        Product product = this.productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());

        Category category = this.categoryRepository.findById(productDto.getCategory())
                .orElseThrow(() -> new CategoryNotFoundException(productDto.getCategory()));
        product.setCategory(category);

        Manufacturer manufacturer = this.manufacturerRepository.findById(productDto.getManufacturer())
                .orElseThrow(() -> new ManufacturerNotFoundException(productDto.getManufacturer()));
        product.setManufacturer(manufacturer);

        return Optional.of(this.productRepository.save(product));
    }

    @Override
    public void deleteById(Long id) {
        this.productRepository.deleteById(id);
    }
}
