package mk.ukim.finki.wp.june2022.g1.config;


import mk.ukim.finki.wp.june2022.g1.service.VirtualServerService;
import mk.ukim.finki.wp.june2022.g1.model.OSType;
import mk.ukim.finki.wp.june2022.g1.model.User;
import mk.ukim.finki.wp.june2022.g1.service.UserService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DataInitializer {

    private final UserService userService;

    private final VirtualServerService service;

    public DataInitializer(UserService userService, VirtualServerService service) {
        this.userService = userService;
        this.service = service;
    }

    private OSType randomizeOSType(int i) {
        if (i % 3 == 0) return OSType.WINDOWS;
        else if (i % 3 == 1) return OSType.DEBIAN;
        return OSType.UBUNTU;
    }

    private String randomizeIpAddress(int i) {
        if (i % 3 == 0) return "10.10.5.102";
        else if (i % 3 == 1) return "194.149.136.102";
        return "85.12.124.6";
    }
    @PostConstruct
    public void initData() {
        this.userService.create("user" + 0, "pass" + 0, "ROLE_SYSADMIN");
        for (int i = 1; i < 6; i++) {
            this.userService.create("user" + i, "pass" + i, "ROLE_USER");
        }

        List<User> users = this.userService.listAll();
        for (int i = 1; i < 11; i++) {
            this.service.create(
                    "VirtualServer: " + i,
                    this.randomizeIpAddress(i),
                    this.randomizeOSType(i),
                    Stream.of(users.get((i - 1) % 5).getId(), users.get((i + 1) % 5).getId()).collect(Collectors.toList()),
                    LocalDate.now().minusDays((i + 1) / 2)
            );
        }
    }
}
