package mk.ukim.finki.wp.eshop.web.rest;

import mk.ukim.finki.wp.eshop.model.Product;
import mk.ukim.finki.wp.eshop.model.dto.ProductDto;
import mk.ukim.finki.wp.eshop.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/products")
public class ProductRestController {

    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    private List<Product> findAll() {
        return this.productService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable Long id) {
        return this.productService.findById(id)
                .map(product -> ResponseEntity.ok().body(product))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/add")
    public ResponseEntity<Product> save(@RequestBody ProductDto productDto) {
        return this.productService.save(productDto)
                .map(product -> ResponseEntity.ok().body(product))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Product> save(@PathVariable Long id, @RequestBody ProductDto productDto) {
        return this.productService.edit(id, productDto)
                .map(product -> ResponseEntity.ok().body(product))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteById(@PathVariable Long id) {
        this.productService.deleteById(id);
        if(this.productService.findById(id).isEmpty()) return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }
}
