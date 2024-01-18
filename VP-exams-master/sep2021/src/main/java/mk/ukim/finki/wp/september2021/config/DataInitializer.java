package mk.ukim.finki.wp.september2021.config;

import mk.ukim.finki.wp.september2021.model.NewsType;
import mk.ukim.finki.wp.september2021.service.NewsCategoryService;
import mk.ukim.finki.wp.september2021.service.NewsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class DataInitializer {

    private final NewsCategoryService newsCategoryService;

    private final NewsService service;

    public DataInitializer(NewsCategoryService newsCategoryService, NewsService service) {
        this.newsCategoryService = newsCategoryService;
        this.service = service;
    }

    private NewsType randomizeEventType(int i) {
        if (i % 3 == 0) return NewsType.DRAFT;
        else if (i % 3 == 1) return NewsType.PROMOTION;
        return NewsType.PUBLIC;
    }

    @PostConstruct
    public void initData() {
        for (int i = 1; i < 6; i++) {
            this.newsCategoryService.create("News category: " + i);
        }

        for (int i = 1; i < 11; i++) {
            this.service.create("News: " + i, "News description: " + i, 20.9 * i, this.randomizeEventType(i), this.newsCategoryService.listAll().get((i - 1) % 5).getId());
        }
    }
}
