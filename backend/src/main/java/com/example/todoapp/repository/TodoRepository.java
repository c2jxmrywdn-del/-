package com.example.todoapp.repository;

import com.example.todoapp.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 待办事项数据库操作接口
 */
@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {

    /**
     * 按创建时间倒序查询所有待办事项
     */
    @Query("SELECT t FROM Todo t ORDER BY t.createdAt DESC")
    List<Todo> findAllOrderByCreatedAtDesc();

    /**
     * 统计未完成的待办事项数量
     */
    @Query("SELECT COUNT(t) FROM Todo t WHERE t.completed = false")
    Long countActive();

    /**
     * 统计已完成的待办事项数量
     */
    @Query("SELECT COUNT(t) FROM Todo t WHERE t.completed = true")
    Long countCompleted();
}
