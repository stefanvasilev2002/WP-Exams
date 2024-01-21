package mk.ukim.finki.wp.jan2022.g2.config;

import jakarta.annotation.PostConstruct;
import mk.ukim.finki.wp.jan2022.g2.model.DiscussionTag;
import mk.ukim.finki.wp.jan2022.g2.model.User;
import mk.ukim.finki.wp.jan2022.g2.service.DiscussionService;
import mk.ukim.finki.wp.jan2022.g2.service.UserService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DataInitializer {

    private final UserService userService;

    private final DiscussionService service;

    public DataInitializer(UserService userService, DiscussionService service) {
        this.userService = userService;
        this.service = service;
    }

    private DiscussionTag randomizeTag(int i) {
        if (i % 3 == 0) return DiscussionTag.OTHER;
        else if (i % 3 == 1) return DiscussionTag.IT;
        return DiscussionTag.FINANCE;
    }

    @PostConstruct
    public void initData() {
        this.userService.create("user" + 0, "pass" + 0, "ROLE_MASTER");
        for (int i = 1; i < 6; i++) {
            this.userService.create("user" + i, "pass" + i, "ROLE_PARTICIPANT");
        }

        List<User> users = this.userService.listAll();
        for (int i = 1; i < 11; i++) {
            this.service.create(
                    "Discussion: " + i,
                    "Description." + i,
                    this.randomizeTag(i),
                    Stream.of(users.get((i - 1) % 5).getId(), users.get((i + 1) % 5).getId()).collect(Collectors.toList()),
                    LocalDate.now().plusDays((i + 11) / 2)
            );
        }
    }
}
