package mk.ukim.finki.wp.kol2022.g2.service.Impl;

import mk.ukim.finki.wp.kol2022.g2.model.Course;
import mk.ukim.finki.wp.kol2022.g2.model.exceptions.InvalidCourseIdException;
import mk.ukim.finki.wp.kol2022.g2.repository.CourseRepository;
import mk.ukim.finki.wp.kol2022.g2.service.CourseService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;

    public CourseServiceImpl(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public Course findById(Long id) {
        return this.courseRepository.findById(id)
                .orElseThrow(InvalidCourseIdException::new);
    }

    @Override
    public List<Course> listAll() {
        return this.courseRepository.findAll();
    }

    @Override
    public Course create(String name) {
        Course course=new Course(name);
        return this.courseRepository.save(course);
    }
}
