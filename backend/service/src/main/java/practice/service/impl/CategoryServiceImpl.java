package practice.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import practice.dto.CategoryDto;
import practice.entity.Category;
import practice.entity.GroupEntity;
import practice.exception.CategoryAlreadyExistException;
import practice.exception.UserNotBelongingInGroupException;
import practice.exception.UserNotFoundException;
import practice.repository.CategoryRepository;
import practice.repository.GroupRepository;
import practice.repository.UserGroupRepository;
import practice.repository.UserRepository;
import practice.service.CategoryService;
import practice.service.GroupService;
import practice.vo.CategoryVo;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    GroupRepository groupRepository;

    @Autowired
    UserGroupRepository userGroupRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    GroupService groupService;

    @Override
    @Transactional
    public CategoryVo addCategory(Integer groupId, String categoryName) {
        categoryRepository.findByGroupIdAndName(groupId, categoryName).ifPresent(c -> {
            throw new CategoryAlreadyExistException(c.getName());
        });

        Category newCategory = Category.builder()
                .name(categoryName)
                .group(entityManager.getReference(GroupEntity.class, groupId))
                .build();

        categoryRepository.save(newCategory);

        return CategoryVo
                .builder()
                .id(newCategory.getId())
                .name(categoryName)
                .build();
    }

    @Override
    public List<CategoryVo> getCategories(Integer userId, Integer groupId) {
        userGroupRepository.findByUserIdAndGroupId(groupId, userId).orElseThrow(
                () -> new UserNotBelongingInGroupException(userId)
        );

        List<Category> categories = categoryRepository.findAllByGroupId(groupId);

        return categories
                .stream()
                .map(c -> CategoryVo
                        .builder()
                        .id(c.getId())
                        .name(c.getName())
                        .build())
                .toList();
    }
}
