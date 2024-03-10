package practice.service.test;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import practice.dto.TaskDto;
import practice.entity.Category;
import practice.entity.Task;
import practice.exception.CategoryNotFoundException;
import practice.exception.TaskNotExistException;
import practice.exception.UserNotBelongingInGroupException;
import practice.repository.CategoryRepository;
import practice.repository.TaskRepository;
import practice.service.GroupService;
import practice.service.impl.TaskServiceImpl;
import practice.vo.TaskVo;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    TaskRepository taskRepository;

    @Mock
    GroupService groupService;

    @Mock
    private EntityManager entityManager;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    TaskServiceImpl taskService;


    @Nested
    @DisplayName("addTask method")
    class addTask {
        @Nested
        class Success {
            @Test
            @DisplayName("task add successfully")
            public void TaskAddedSuccessfully() {
                int groupId = 1;
                int categoryId = 2;
                String categoryName = "test category";
                Date taskDate = getExpiredDate("2024-05-04");

                TaskDto dto = TaskDto
                        .builder()
                        .name("test task")
                        .status("InProgress")
                        .priorityLevel(Task.TaskPriorityLevel.Low.toString())
                        .expiredAt(taskDate)
                        .build();

                configureCategoryRepositoryFindByIdAndGroupId(categoryId, groupId, Optional.of(Category.builder().name(categoryName).build()));

                TaskVo res = taskService.addTask(groupId, categoryId, dto);

                assertEquals(res.getName(), "test task");
                assertEquals(res.getStatus(), "InProgress");
                assertEquals(res.getPriorityLevel(), "Low");
                assertEquals(res.getExpiredAt(), new Timestamp(taskDate.getTime()));
                assertEquals(res.getCategoryName(), categoryName);

            }
        }

        @Nested
        class Failed {
            @Test
            @DisplayName("category not exists")
            public void CategoryNotExist() {
                int groupId = 1;
                int invalidCategoryId = 2;

                TaskDto dto = TaskDto.builder().build();

                configureCategoryRepositoryFindByIdAndGroupId(invalidCategoryId, groupId, Optional.empty());

                assertThrows(CategoryNotFoundException.class, () -> {
                    taskService.addTask(groupId, invalidCategoryId, dto);
                });
            }
        }

        private void configureCategoryRepositoryFindByIdAndGroupId(int categoryId, int groupId, Optional<Category> res) {
            when(categoryRepository.findByIdAndGroupId(categoryId, groupId)).thenReturn(res);
        }
    }

    @Nested
    @DisplayName("getTasks method")
    class getTasks {
        @Nested
        class Success {
            @Test
            @DisplayName("Tasks returned successfully")
            public void tasksReturnedSuccessfully() {
                int groupId = 1;
                int userId = 5;

                Date expiredAt = getExpiredDate("2024-04-03");
                Category category = Category
                        .builder()
                        .name("test category")
                        .build();

                List<Task> tasks = List.of(
                        Task
                                .builder()
                                .id(1)
                                .name("test task 1")
                                .status(Task.TaskStatus.InProgress)
                                .priorityLevel(Task.TaskPriorityLevel.High)
                                .expiredAt(new Timestamp(expiredAt.getTime()))
                                .category(category)
                                .build(),
                        Task
                                .builder()
                                .id(2)
                                .name("test task 2")
                                .status(Task.TaskStatus.Finished)
                                .priorityLevel(Task.TaskPriorityLevel.Low)
                                .expiredAt(new Timestamp(expiredAt.getTime()))
                                .category(category)
                                .build()
                );

                configureGroupServiceIsUserInGroup(groupId, userId, true);
                when(taskRepository.findAllByGroupIdWithCategory(groupId)).thenReturn(tasks);

                List<TaskVo> res = taskService.getTasks(userId, groupId);

                List<TaskVo> expected = tasks
                        .stream()
                        .map(t -> TaskVo
                                .builder()
                                .id(t.getId())
                                .name(t.getName())
                                .status(t.getStatus().toString())
                                .priorityLevel(t.getPriorityLevel().toString())
                                .expiredAt(t.getExpiredAt())
                                .categoryName(t.getCategory().getName())
                                .build()
                        )
                        .toList();

                assertThat(res)
                        .usingRecursiveComparison()
                        .ignoringCollectionOrder()
                        .isEqualTo(expected);
            }
        }

        @Nested
        class Failed {
            @Test
            @DisplayName("user not in group")
            public void userNotInGroup() {
                int groupId = 1;
                int userId = 5;

                configureGroupServiceIsUserInGroup(groupId, userId, false);

                assertThrows(UserNotBelongingInGroupException.class, () -> {
                    taskService.getTasks(userId, groupId);
                });
            }
        }
    }

    @Nested
    @DisplayName("getTask method")
    class getTask {
        @Nested
        class Success {
            @Test
            @DisplayName("get task successfully")
            public void getTaskSuccessfully(){
                int taskId = 10;
                int userId = 1;
                int groupId = 2;

                Task task = Task
                        .builder()
                        .id(taskId)
                        .name("test task")
                        .priorityLevel(Task.TaskPriorityLevel.High)
                        .status(Task.TaskStatus.InProgress)
                        .expiredAt(new Timestamp(getExpiredDate("2024-05-06").getTime()))
                        .build();

                configureGroupServiceIsUserInGroup(groupId, userId, true);
                configureTaskRepositoryFindById(taskId, Optional.of(task));

                TaskVo res = taskService.getTask(userId, groupId, taskId);

                TaskVo expected = TaskVo
                        .builder()
                        .id(taskId)
                        .name(task.getName())
                        .priorityLevel("High")
                        .status("InProgress")
                        .expiredAt(new Timestamp(getExpiredDate("2024-05-06").getTime()))
                        .build();


                assertThat(res)
                        .usingRecursiveComparison()
                        .isEqualTo(expected);
            }
        }

        @Nested
        class Failed {
            @Test
            @DisplayName("user not in group")
            public void userNotInGroup() {
                int groupId = 1;
                int userId = 5;

                configureGroupServiceIsUserInGroup(groupId, userId, false);

                assertThrows(UserNotBelongingInGroupException.class, () -> {
                    taskService.getTasks(userId, groupId);
                });
            }

            @Test
            @DisplayName("Tasks not exist")
            public void taskNotExist() {
                int taskId = 1;
                int userId = 2;
                int groupId = 3;

                configureGroupServiceIsUserInGroup(groupId, userId, true);
                configureTaskRepositoryFindById(taskId, Optional.empty());

                assertThrows(TaskNotExistException.class, () -> {
                    taskService.getTask(userId, groupId, taskId);
                });
            }
        }
        private void configureTaskRepositoryFindById(int taskId, Optional<Task> res){
            when(taskRepository.findById(taskId)).thenReturn(res);
        }
    }

    @Nested
    @DisplayName("modifyTask method")
    class modifyTask {
        @Nested
        class Success {
            @Test
            @DisplayName("Tasks modified successfully")
            public void taskModifiedSuccessfully() {
                int taskId = 5;

                Date expiredAt = getExpiredDate("2024-07-06");

                TaskDto dto = TaskDto
                        .builder()
                        .name("modified name")
                        .expiredAt(expiredAt)
                        .priorityLevel("High")
                        .build();

                Task task = Task
                        .builder()
                        .id(taskId)
                        .name("task name")
                        .priorityLevel(Task.TaskPriorityLevel.Low)
                        .status(Task.TaskStatus.InProgress)
                        .expiredAt(new Timestamp(getExpiredDate("2024-04-05").getTime()))
                        .category(Category.builder().name("task category").build())
                        .build();

                configureTaskRepositoryFindByIdWithCategory(taskId, Optional.of(task));

                TaskVo res = taskService.modifyTask(taskId, dto);

                TaskVo expected = TaskVo
                        .builder()
                        .id(taskId)
                        .name(dto.getName())
                        .priorityLevel("High")
                        .expiredAt(new Timestamp(expiredAt.getTime()))
                        .status("InProgress")
                        .categoryName(task.getCategory().getName())
                        .build();

                assertThat(res)
                        .usingRecursiveComparison()
                        .isEqualTo(expected);
            }
        }

        @Nested
        class Failed {
            @Test
            @DisplayName("Tasks not exist")
            public void taskNotExist() {
                int taskId = 1;
                TaskDto dto = TaskDto.builder().build();

                configureTaskRepositoryFindByIdWithCategory(taskId, Optional.empty());

                assertThrows(TaskNotExistException.class, () -> {
                    taskService.modifyTask(taskId, dto);
                });
            }
        }

        private void configureTaskRepositoryFindByIdWithCategory(int taskId, Optional<Task> res) {
            when(taskRepository.findByIdWithCategory(taskId)).thenReturn(res);
        }
    }

    private void configureGroupServiceIsUserInGroup(int groupId, int userId, boolean res) {
        when(groupService.isUserInGroup(userId, groupId)).thenReturn(res);
    }

    private Date getExpiredDate(String date) {
        Date taskDate = null;
        try {
            taskDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
        }
        return taskDate;

    }
}