package mk.ukim.finki.wp.kol2022.g3.service.impl;

import mk.ukim.finki.wp.kol2022.g3.model.ForumUser;
import mk.ukim.finki.wp.kol2022.g3.model.ForumUserType;
import mk.ukim.finki.wp.kol2022.g3.model.exceptions.InvalidForumUserIdException;
import mk.ukim.finki.wp.kol2022.g3.repository.ForumUserRepository;
import mk.ukim.finki.wp.kol2022.g3.service.ForumUserService;
import mk.ukim.finki.wp.kol2022.g3.service.InterestService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class ForumUserServiceImpl implements ForumUserService, UserDetailsService {
    private final ForumUserRepository forumUserRepository;
    private final InterestService interestService;
    private final PasswordEncoder passwordEncoder;
    public ForumUserServiceImpl(ForumUserRepository forumUserRepository, InterestService interestService, PasswordEncoder passwordEncoder) {
        this.forumUserRepository = forumUserRepository;
        this.interestService = interestService;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public List<ForumUser> listAll() {
        return forumUserRepository.findAll();
    }

    @Override
    public ForumUser findById(Long id) {
        return forumUserRepository.findById(id).orElseThrow(InvalidForumUserIdException::new);
    }

    @Override
    public ForumUser create(String name, String email, String password, ForumUserType type, List<Long> interestId, LocalDate birthday) {
        return forumUserRepository.save(new ForumUser(
                name,
                email,
                password,
                type,
                interestId.stream().map(interestService::findById).collect(Collectors.toList()),
                birthday
        ));
    }

    @Override
    public ForumUser update(Long id, String name, String email, String password, ForumUserType type, List<Long> interestId, LocalDate birthday) {
        ForumUser user = findById(id);

        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setBirthday(birthday);
        user.setType(type);
        user.setInterests(interestId.stream().map(interestService::findById).collect(Collectors.toList()));

        return forumUserRepository.save(user);
    }

    @Override
    public ForumUser delete(Long id) {
        ForumUser user = findById(id);
        forumUserRepository.delete(user);
        return user;
    }

    @Override
    public List<ForumUser> filter(Long interestId, Integer age) {
        if (interestId != null && age != null) {
            return forumUserRepository.findAllByInterestsContainingAndBirthdayBefore(interestService.findById(interestId),
                    LocalDate.now().minusYears(age));
        }

        if (interestId != null) {
            return forumUserRepository.findAllByInterestsContaining(interestService.findById(interestId));
        }

        if (age != null) {
            return forumUserRepository.findAllByBirthdayBefore(LocalDate.now().minusYears(age));
        }

        return listAll();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ForumUser user = forumUserRepository.findByEmail(username);
        return new User(
                user.getEmail(),
                passwordEncoder.encode(user.getPassword()),
                Collections.singleton(user.getType())
        );
    }
}
