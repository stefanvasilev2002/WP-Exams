package mk.ukim.finki.wp.september2021.repository;

import mk.ukim.finki.wp.september2021.model.NewsCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NewsCategoryRepository extends JpaRepository<NewsCategory, Long> {
}
