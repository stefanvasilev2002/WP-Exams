package mk.ukim.finki.wp.september2021.service.Impl;

import mk.ukim.finki.wp.september2021.model.NewsCategory;
import mk.ukim.finki.wp.september2021.model.exceptions.InvalidNewsCategoryIdException;
import mk.ukim.finki.wp.september2021.repository.NewsCategoryRepository;
import mk.ukim.finki.wp.september2021.service.NewsCategoryService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NewsCategoryServiceImpl implements NewsCategoryService {

    private final NewsCategoryRepository newsCategoryRepository;

    public NewsCategoryServiceImpl(NewsCategoryRepository newsCategoryRepository) {
        this.newsCategoryRepository = newsCategoryRepository;
    }

    @Override
    public NewsCategory findById(Long id) {
        return this.newsCategoryRepository.findById(id)
                .orElseThrow(InvalidNewsCategoryIdException::new);
    }

    @Override
    public List<NewsCategory> listAll() {
        return this.newsCategoryRepository.findAll();
    }

    @Override
    public NewsCategory create(String name) {
        NewsCategory newsCategory=new NewsCategory(name);
        return this.newsCategoryRepository.save(newsCategory);
    }
}
