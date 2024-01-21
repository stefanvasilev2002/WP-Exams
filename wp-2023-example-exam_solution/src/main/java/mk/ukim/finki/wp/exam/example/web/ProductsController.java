package mk.ukim.finki.wp.exam.example.web;

import mk.ukim.finki.wp.exam.example.model.Product;
import mk.ukim.finki.wp.exam.example.service.CategoryService;
import mk.ukim.finki.wp.exam.example.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

@Controller
public class ProductsController {

    private final ProductService service;
    private final CategoryService categoryService;

    public ProductsController(ProductService service, CategoryService categoryService) {
        this.service = service;
        this.categoryService = categoryService;
    }

    @GetMapping({"/", "/products"})
    public String showProducts(
            @RequestParam(required = false) String nameSearch,
            @RequestParam(required = false) Long categoryId,
            Model model) {
        List<Product> products;
        if (nameSearch == null && categoryId == null) {
            products = this.service.listAllProducts();
        } else {
            products = this.service.listProductsByNameAndCategory(nameSearch, categoryId);
        }

        model.addAttribute("categories", categoryService.listAll());
        model.addAttribute("products", products);
        return "list";
    }

    @GetMapping("/products/add")
    public String showAdd(Model model) {
        model.addAttribute("categories", categoryService.listAll());
        return "form";
    }

    @GetMapping("/products/{id}/edit")
    public String showEdit(@PathVariable Long id, Model model) {
        Product product = this.service.findById(id);
        model.addAttribute("product", product);
        model.addAttribute("categories", categoryService.listAll());
        return "form";
    }

    @PostMapping("/products")
    public String create(
            @RequestParam String name,
            @RequestParam Double price,
            @RequestParam Integer quantity,
            @RequestParam List<Long> categories) {
        this.service.create(name, price, quantity, categories);
        return "redirect:/products";
    }

    @PostMapping("/products/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String name,
                         @RequestParam Double price,
                         @RequestParam Integer quantity,
                         @RequestParam List<Long> categories
    ) {
        this.service.update(id, name, price, quantity, categories);
        return "redirect:/products";
    }

    @PostMapping("/products/{id}/delete")
    public String delete(@PathVariable Long id) {
        this.service.delete(id);
        return "redirect:/products";
    }
}
