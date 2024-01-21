package mk.ukim.finki.wp.jan2022.g2.service.impl;

import mk.ukim.finki.wp.jan2022.g2.model.Discussion;
import mk.ukim.finki.wp.jan2022.g2.model.DiscussionTag;
import mk.ukim.finki.wp.jan2022.g2.model.exceptions.InvalidDiscussionIdException;
import mk.ukim.finki.wp.jan2022.g2.repository.DiscussionRepository;
import mk.ukim.finki.wp.jan2022.g2.service.DiscussionService;
import mk.ukim.finki.wp.jan2022.g2.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DiscussionServiceImpl implements DiscussionService {
    private final DiscussionRepository discussionRepository;
    private final UserService userService;

    public DiscussionServiceImpl(DiscussionRepository discussionRepository, UserService userService) {
        this.discussionRepository = discussionRepository;
        this.userService = userService;
    }

    @Override
    public List<Discussion> listAll() {
        return discussionRepository.findAll();
    }

    @Override
    public Discussion findById(Long id) {
        return discussionRepository.findById(id).orElseThrow(InvalidDiscussionIdException::new);
    }

    @Override
    public Discussion create(String title, String description, DiscussionTag discussionTag, List<Long> participants, LocalDate dueDate) {
        return discussionRepository.save(new Discussion(
                title,
                description,
                discussionTag,
                participants.stream().map(userService::findById).collect(Collectors.toList()),
                dueDate
        ));
    }

    @Override
    public Discussion update(Long id, String title, String description, DiscussionTag discussionTag, List<Long> participants) {
        Discussion discussion = findById(id);

        discussion.setTitle(title);
        discussion.setDescription(description);
        discussion.setTag(discussionTag);
        discussion.setParticipants(participants.stream().map(userService::findById).collect(Collectors.toList()));

        return discussionRepository.save(discussion);
    }

    @Override
    public Discussion delete(Long id) {
        Discussion discussion = findById(id);
        discussionRepository.delete(discussion);
        return discussion;
    }

    @Override
    public Discussion markPopular(Long id) {
        Discussion discussion = findById(id);
        discussion.setPopular(true);
        return discussionRepository.save(discussion);
    }

    @Override
    public List<Discussion> filter(Long participantId, Integer daysUntilClosing) {
    if (participantId != null && daysUntilClosing != null){
        return discussionRepository.findAllByParticipantsContainingAndDueDateBefore(
                userService.findById(participantId),
                LocalDate.now().plusDays(daysUntilClosing)
        );
    }
    else if (participantId != null){
        return discussionRepository.findAllByParticipantsContaining(userService.findById(participantId));
    }
    else if (daysUntilClosing != null){
        return discussionRepository.findAllByDueDateBefore(LocalDate.now().plusDays(daysUntilClosing));
    }
    return listAll();
    }
}
