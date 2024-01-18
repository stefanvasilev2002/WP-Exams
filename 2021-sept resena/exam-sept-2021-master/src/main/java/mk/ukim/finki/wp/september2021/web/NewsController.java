package mk.ukim.finki.wp.september2021.web;

import mk.ukim.finki.wp.september2021.model.News;
import mk.ukim.finki.wp.september2021.model.NewsCategory;
import mk.ukim.finki.wp.september2021.model.NewsType;
import mk.ukim.finki.wp.september2021.service.NewsCategoryService;
import mk.ukim.finki.wp.september2021.service.NewsService;
import org.apache.xpath.operations.Mod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping
public class NewsController {

    private final NewsService service;
    private final NewsCategoryService newsCategoryService;

    public NewsController(NewsService service, NewsCategoryService newsCategoryService) {
        this.service = service;
        this.newsCategoryService = newsCategoryService;
    }

    /**
     * This method should use the "list.html" template to display all news.
     * The method should be mapped on paths '/' and '/news'.
     * The arguments that this method takes are optional and can be 'null'.
     * In the case when the arguments are not passed (both are 'null') all news should be displayed.
     * If one, or both of the arguments are not 'null', the news that are the result of the call
     * to the method 'listNewsWithPriceLessThanAndType' from the service should be displayed.
     *
     * @param price
     * @param type
     * @return The view "list.html".
     */
    @GetMapping({"/","/news"})
    public String showNews(@RequestParam(required = false) Double price,
                           @RequestParam(required = false) NewsType type, Model model) {
        List<News> news;
        if (price == null && type == null) {
            news = this.service.listAllNews();
        } else {
            news = this.service.listNewsWithPriceLessThanAndType(price, type);
        }
        model.addAttribute("Types",NewsType.values());
        model.addAttribute("news",news);

        return "list.html";
    }

    /**
     * This method should display the "form.html" template.
     * The method should be mapped on path '/news/add'.
     *
     * @return The view "form.html".
     */
    @GetMapping("/news/add")
    public String showAdd(Model model) {
        List<NewsCategory> categories=this.newsCategoryService.listAll();
        model.addAttribute("Types",NewsType.values());
        model.addAttribute("categories",categories);


        return "form.html";
    }

    /**
     * This method should display the "form.html" template.
     * However, in this case all 'input' elements should be filled with the appropriate value for the entity that is updated.
     * The method should be mapped on path '/news/[id]/edit'.
     *
     * @return The view "form.html".
     */
    @GetMapping("/news/{id}/edit")
    public String showEdit(@PathVariable Long id,Model model) {
        News news=this.service.findById(id);

        List<NewsCategory> categories=this.newsCategoryService.listAll();
        model.addAttribute("Types",NewsType.values());
        model.addAttribute("categories",categories);
        model.addAttribute("none",news);

        return "form.html";
    }

    /**
     * This method should create a news given the arguments it takes.
     * The method should be mapped on path '/news'.
     * After the entity is created, all news should be displayed.
     *
     * @return The view "list.html".
     */
    @PostMapping("/news")
    public String create(@RequestParam String name,
                         @RequestParam String description,
                         @RequestParam Double price,
                         @RequestParam NewsType type,
                         @RequestParam Long category) {
        this.service.create(name, description, price, type, category);
        return "redirect:/news";
    }

    /**
     * This method should update a news given the arguments it takes.
     * The method should be mapped on path '/news/[id]'.
     * After the entity is updated, all news should be displayed.
     *
     * @return The view "list.html".
     */
    @PostMapping("/news/{id}")
    public String update(@PathVariable Long id,
                         @RequestParam String name,
                         @RequestParam String description,
                         @RequestParam Double price,
                         @RequestParam NewsType type,
                         @RequestParam Long category) {
        this.service.update(id, name, description, price, type, category);
        return "redirect:/news";
    }

    /**
     * This method should delete the news that has the appropriate identifier.
     * The method should be mapped on path '/news/[id]/delete'.
     * After the entity is deleted, all news should be displayed.
     *
     * @return The view "list.html".
     */
    @PostMapping("/news/{id}/delete")
    public String delete(@PathVariable Long id) {
        this.service.delete(id);
        return "redirect:/news";
    }

    /**
     * This method should increase the number of likes of the appropriate news by 1.
     * The method should be mapped on path '/news/[id]/like'.
     * After the operation, all news should be displayed.
     *
     * @return The view "list.html".
     */

    @PostMapping("/news/{id}/like")
    public String like(@PathVariable Long id) {
        this.service.like(id);
        return "redirect:/news";
    }
}
