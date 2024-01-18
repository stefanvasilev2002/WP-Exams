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
        return newsRepository.save(new News(
                name,
                description,
                price,
                type,
                newsCategoryService.findById(category)));
    }

    @Override
    public News update(Long id, String name, String description, Double price, NewsType type, Long category) {
        News news = findById(id);
        news.setName(name);
        news.setDescription(description);
        news.setPrice(price);
        news.setType(type);
        news.setCategory(newsCategoryService.findById(category));
        newsRepository.save(news);
        return news;
    }

    @Override
    public News delete(Long id) {
        News news = findById(id);
        newsRepository.delete(findById(id));
        return news;
    }

    @Override
    public News like(Long id) {
        News news = findById(id);
        news.setLikes(news.getLikes() + 1);
        newsRepository.save(news);
        return news;
    }

    @Override
    public List<News> listNewsWithPriceLessThanAndType(Double price, NewsType type) {
        if (price == null && type == null) {
            return listAllNews();
        }
        else if (price != null && type != null){
            return newsRepository.findByPriceLessThanAndType(price, type);
        }
        else if (price == null){
            return newsRepository.findByType(type);
        }
        else return newsRepository.findByPriceLessThan(price);
    }
}
