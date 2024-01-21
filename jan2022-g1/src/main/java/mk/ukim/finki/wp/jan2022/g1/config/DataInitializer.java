package mk.ukim.finki.wp.jan2022.g1.config;


import jakarta.annotation.PostConstruct;
import mk.ukim.finki.wp.jan2022.g1.service.TaskService;
import mk.ukim.finki.wp.jan2022.g1.model.TaskCategory;
import mk.ukim.finki.wp.jan2022.g1.model.User;
import mk.ukim.finki.wp.jan2022.g1.service.UserService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DataInitializer {

    private final UserService userService;

    private final TaskService service;

    public DataInitializer(UserService userService, TaskService service) {
        this.userService = userService;
        this.service = service;
    }

    private TaskCategory randomizeCategory(int i) {
        if (i % 3 == 0) return TaskCategory.OTHER;
        else if (i % 3 == 1) return TaskCategory.FEATURE;
        return TaskCategory.BUG;
    }
    @PostConstruct
    public void initData() {
        this.userService.create("user" + 0, "pass" + 0, "ROLE_MANAGER");
        for (int i = 1; i < 6; i++) {
            this.userService.create("user" + i, "pass" + i, "ROLE_DEVELOPER");
        }

        List<User> users = this.userService.listAll();
        for (int i = 1; i < 11; i++) {
            this.service.create(
                    "Task: " + i,
                    "Description." + i,
                    this.randomizeCategory(i),
                    Stream.of(users.get((i - 1) % 5).getId(), users.get((i + 1) % 5).getId()).collect(Collectors.toList()),
                    LocalDate.now().plusDays((i + 1) / 2)
            );
        }
    }
}
