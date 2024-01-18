package mk.ukim.finki.wp.september2021.service.impl;

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
        return newsCategoryRepository.findById(id).orElseThrow(InvalidNewsCategoryIdException::new);
    }

    @Override
    public List<NewsCategory> listAll() {
        return newsCategoryRepository.findAll();
    }

    @Override
    public NewsCategory create(String name) {
        return newsCategoryRepository.save(new NewsCategory(name));
    }
}
