package practice.service.test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import practice.entity.Category;
import practice.entity.UserGroup;
import practice.exception.CategoryAlreadyExistException;
import practice.exception.UserNotBelongingInGroupException;
import practice.repository.CategoryRepository;
import practice.repository.GroupRepository;
import practice.repository.UserGroupRepository;
import practice.repository.UserRepository;
import practice.service.GroupService;
import practice.service.impl.CategoryServiceImpl;
import practice.service.impl.GroupServiceImpl;
import practice.vo.CategoryVo;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    CategoryRepository categoryRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    GroupRepository groupRepository;

    @Mock
    UserGroupRepository userGroupRepository;
    @Mock
    private EntityManager entityManager;

    @Mock
    GroupServiceImpl groupService;

    @InjectMocks
    CategoryServiceImpl categoryService;


    @Nested
    @DisplayName("addCategory method")
    class addCategory {
        @Nested
        class Success {
            @Test
            @DisplayName("new category name added successfully")
            public void categoryNameAddedSuccessfully() {
                String newCategoryName = "test group name";

                configureCategoryRepositoryFindByGroupIdAndName(1, newCategoryName, Optional.empty());

                CategoryVo res = categoryService.addCategory(1, newCategoryName);

                assertEquals(res.getName(), newCategoryName);

            }
        }

        @Nested
        class Failed {
            @Test
            @DisplayName("category name already exists in the group")
            public void categoryNameAlreadyExist() {
                configureCategoryRepositoryFindByGroupIdAndName(1, "test group name", Optional.of(Category.builder().build()));

                assertThrows(CategoryAlreadyExistException.class, () -> {
                    categoryService.addCategory(1, "test group name");
                });
            }

        }

        private void configureCategoryRepositoryFindByGroupIdAndName(int groupId, String categoryName, Optional<Category> res) {
            when(categoryRepository.findByGroupIdAndName(groupId, categoryName)).thenReturn(res);
        }
    }

    @Nested
    @DisplayName("getCategories method")
    class getCategories {
        @Nested
        class Success {
            @Test
            @DisplayName("User in group")
            public void userInGroup() {
                int groupId = 1;
                int userId = 2;
                List<Category> categories = List.of(
                        Category
                                .builder()
                                .id(1)
                                .name("category 1")
                                .build(),
                        Category
                                .builder()
                                .id(2)
                                .name("category 2")
                                .build()
                );

                configureUserGroupRepositoryFindByUserIdAndGroupId(groupId, userId, Optional.of(UserGroup.builder().build()));

                configureCategoryRepositoryFindAllByGroupId(groupId, categories);

                List<CategoryVo> res = categoryService.getCategories(userId, groupId);

                List<CategoryVo> expected = categories.stream().map(
                        c -> CategoryVo
                                .builder()
                                .id(c.getId())
                                .name(c.getName())
                                .build()
                ).toList();

                assertThat(res)
                        .usingRecursiveComparison()
                        .ignoringCollectionOrder()
                        .isEqualTo(expected);

            }
        }

        @Nested
        class Failed {
            @Test
            @DisplayName("User not in the group")
            public void userNotInGroup() {
                configureUserGroupRepositoryFindByUserIdAndGroupId(1, 2, Optional.empty());

                assertThrows(UserNotBelongingInGroupException.class, () -> {
                    categoryService.getCategories(1, 2);
                });
            }
        }

        private void configureUserGroupRepositoryFindByUserIdAndGroupId(int userId, int groupId, Optional<UserGroup> res) {
            lenient().when(userGroupRepository.findByUserIdAndGroupId(userId, groupId)).thenReturn(res);
        }

        private void configureCategoryRepositoryFindAllByGroupId(int groupId, List<Category> res) {
            when(categoryRepository.findAllByGroupId(groupId)).thenReturn(res);
        }
    }
}