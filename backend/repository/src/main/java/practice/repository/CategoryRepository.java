package practice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import practice.entity.Category;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> findByGroupIdAndName(Integer groupId, String name);

    Optional<Category> findByIdAndGroupId(int id, int groupId);
    List<Category> findAllByGroupId(Integer groupId);
}
