package fr.aaw.template;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface TodoRepository extends JpaRepository<Todo, UUID> {
    List<Todo> findAllByOrderByCreatedAtDesc();

    @Modifying
    @Transactional
    @Query("delete from Todo todo where todo.id = :id")
    int removeById(@Param("id") UUID id);
}
