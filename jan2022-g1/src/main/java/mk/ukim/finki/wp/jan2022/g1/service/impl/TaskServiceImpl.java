package mk.ukim.finki.wp.jan2022.g1.service.impl;

import mk.ukim.finki.wp.jan2022.g1.model.Task;
import mk.ukim.finki.wp.jan2022.g1.model.TaskCategory;
import mk.ukim.finki.wp.jan2022.g1.model.exceptions.InvalidTaskIdException;
import mk.ukim.finki.wp.jan2022.g1.repository.TaskRepository;
import mk.ukim.finki.wp.jan2022.g1.service.TaskService;
import mk.ukim.finki.wp.jan2022.g1.service.UserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService {
    private final TaskRepository taskRepository;
    private final UserService userService;
    public TaskServiceImpl(TaskRepository taskRepository, UserService userService) {
        this.taskRepository = taskRepository;
        this.userService = userService;
    }

    @Override
    public List<Task> listAll() {
        return taskRepository.findAll();
    }

    @Override
    public Task findById(Long id) {
        return taskRepository.findById(id).orElseThrow(InvalidTaskIdException::new);
    }

    @Override
    public Task create(String title, String description, TaskCategory category, List<Long> assignees, LocalDate dueDate) {
        return taskRepository.save(new Task(
                title,
                description,
                category,
                assignees.stream().map(userService::findById).collect(Collectors.toList()),
                dueDate
        ));
    }

    @Override
    public Task update(Long id, String title, String description, TaskCategory category, List<Long> assignees) {
        Task task = findById(id);

        task.setTitle(title);
        task.setCategory(category);
        task.setDescription(description);
        task.setAssignees(assignees.stream().map(userService::findById).collect(Collectors.toList()));

        return taskRepository.save(task);
    }

    @Override
    public Task delete(Long id) {
        Task task = findById(id);
        taskRepository.delete(task);
        return task;
    }

    @Override
    public Task markDone(Long id) {
        Task task = findById(id);
        task.setDone(true);
        return taskRepository.save(task);
    }

    @Override
    public List<Task> filter(Long assigneeId, Integer lessThanDayBeforeDueDate) {
        if (assigneeId != null && lessThanDayBeforeDueDate != null){
            return taskRepository.findByAssigneesContainingAndDueDateBefore(
                    userService.findById(assigneeId),
                    LocalDate.now().plusDays(lessThanDayBeforeDueDate));
        }
        else if (assigneeId != null){
            return taskRepository.findByAssigneesContaining(userService.findById(assigneeId));
        }
        else if (lessThanDayBeforeDueDate != null){
            return taskRepository.findByDueDateBefore(LocalDate.now().plusDays(lessThanDayBeforeDueDate));
        }
        return listAll();
    }
}
