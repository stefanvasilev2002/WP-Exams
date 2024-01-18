package mk.ukim.finki.wp.september2021.service.impl;

import mk.ukim.finki.wp.september2021.model.News;
import mk.ukim.finki.wp.september2021.model.NewsType;
import mk.ukim.finki.wp.september2021.model.exceptions.InvalidNewsIdException;
import mk.ukim.finki.wp.september2021.repository.NewsRepository;
import mk.ukim.finki.wp.september2021.service.NewsCategoryService;
import mk.ukim.finki.wp.september2021.service.NewsService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsServiceImpl implements NewsService {
    private final NewsRepository newsRepository;
    private final NewsCategoryService newsCategoryService;

    public NewsServiceImpl(NewsRepository newsRepository, NewsCategoryService newsCategoryService) {
        this.newsRepository = newsRepository;
        this.newsCategoryService = newsCategoryService;
    }

    @Override
    public List<News> listAllNews() {
        return newsRepository.findAll();
    }

    @Override
    public News findById(Long id) {
        return newsRepository.findById(id).orElseThrow(InvalidNewsIdException::new);
    }

    @Override
    public News create(String name, String description, Double price, NewsType type, Long category) {
        return newsRepository.save(new News(name, description, price, type, newsCategoryService.findById(category)));
    }

    @Override
    public News update(Long id, String name, String description, Double price, NewsType type, Long category) {
        News n = findById(id);

        n.setName(name);
        n.setDescription(description);
        n.setPrice(price);
        n.setType(type);
        n.setCategory(newsCategoryService.findById(category));

        return newsRepository.save(n);
    }

    @Override
    public News delete(Long id) {
        News n = findById(id);

        newsRepository.delete(n);

        return n;
    }

    @Override
    public News like(Long id) {
        News n = findById(id);

        n.setLikes(n.getLikes() + 1);

        return newsRepository.save(n);
    }

    @Override
    public List<News> listNewsWithPriceLessThanAndType(Double price, NewsType type) {
        if (price != null && type != null) {
            return newsRepository.findAllByTypeAndPriceLessThan(type, price);
        } else if (price == null && type == null) {
            return listAllNews();
        } else if (price != null) {
            return newsRepository.findAllByPriceLessThan(price);
        } else {
            return newsRepository.findAllByType(type);
        }
    }
}
