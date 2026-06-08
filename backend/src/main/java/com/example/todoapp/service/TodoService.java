package com.example.todoapp.service;

import com.example.todoapp.dto.TodoDTO;
import com.example.todoapp.entity.Todo;
import com.example.todoapp.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 待办事项业务逻辑服务层
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TodoService {

    private final TodoRepository todoRepository;

    /**
     * 获取所有待办事项（按创建时间倒序）
     */
    @Transactional(readOnly = true)
    public List<TodoDTO> getAllTodos() {
        log.debug("获取所有待办事项");
        return todoRepository.findAllOrderByCreatedAtDesc()
                .stream()
                .map(this::entityToDTO)
                .collect(Collectors.toList());
    }

    /**
     * 根据ID获取单个待办事项
     */
    @Transactional(readOnly = true)
    public TodoDTO getTodoById(Long id) {
        log.debug("获取待办事项: {}", id);
        return todoRepository.findById(id)
                .map(this::entityToDTO)
                .orElseThrow(() -> {
                    log.warn("待办事项不存在: {}", id);
                    return new IllegalArgumentException("待办事项不存在: " + id);
                });
    }

    /**
     * 创建新的待办事项
     */
    public TodoDTO createTodo(TodoDTO todoDTO) {
        log.info("创建待办事项: {}", todoDTO.getText());
        
        if (todoDTO.getText() == null || todoDTO.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("任务内容不能为空");
        }
        
        Todo todo = Todo.builder()
                .text(todoDTO.getText().trim())
                .completed(false)
                .build();

        Todo savedTodo = todoRepository.save(todo);
        log.info("待办事项创建成功，ID: {}", savedTodo.getId());
        return entityToDTO(savedTodo);
    }

    /**
     * 更新待办事项
     */
    public TodoDTO updateTodo(Long id, TodoDTO todoDTO) {
        log.info("更新待办事项: {}", id);
        
        Todo todo = todoRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("待办事项不存在: {}", id);
                    return new IllegalArgumentException("待办事项不存在: " + id);
                });

        // 更新文本内容
        if (todoDTO.getText() != null && !todoDTO.getText().trim().isEmpty()) {
            todo.setText(todoDTO.getText().trim());
        }
        
        // 更新完成状态
        if (todoDTO.getCompleted() != null) {
            todo.setCompleted(todoDTO.getCompleted());
        }

        Todo updatedTodo = todoRepository.save(todo);
        log.info("待办事项更新成功，ID: {}", id);
        return entityToDTO(updatedTodo);
    }

    /**
     * 删除待办事项
     */
    public void deleteTodo(Long id) {
        log.info("删除待办事项: {}", id);
        
        if (!todoRepository.existsById(id)) {
            log.warn("待办事项不存在: {}", id);
            throw new IllegalArgumentException("待办事项不存在: " + id);
        }
        
        todoRepository.deleteById(id);
        log.info("待办事项删除成功，ID: {}", id);
    }

    /**
     * 清空所有已完成的待办事项
     */
    public void clearCompleted() {
        log.info("清空所有已完成的待办事项");
        List<Todo> completedTodos = todoRepository.findAll()
                .stream()
                .filter(Todo::getCompleted)
                .collect(Collectors.toList());
        
        if (!completedTodos.isEmpty()) {
            todoRepository.deleteAll(completedTodos);
            log.info("已清空 {} 个已完成的待办事项", completedTodos.size());
        }
    }

    /**
     * 清空所有待办事项
     */
    public void clearAll() {
        log.warn("清空所有待办事项");
        long count = todoRepository.count();
        todoRepository.deleteAll();
        log.warn("已清空 {} 个待办事项", count);
    }

    /**
     * 获取统计信息
     */
    @Transactional(readOnly = true)
    public TodoStatsDTO getStats() {
        log.debug("获取统计信息");
        long total = todoRepository.count();
        long active = todoRepository.countActive();
        long completed = todoRepository.countCompleted();

        return TodoStatsDTO.builder()
                .total(total)
                .active(active)
                .completed(completed)
                .build();
    }

    /**
     * 实体转DTO
     */
    private TodoDTO entityToDTO(Todo todo) {
        return TodoDTO.builder()
                .id(todo.getId())
                .text(todo.getText())
                .completed(todo.getCompleted())
                .createdAt(todo.getCreatedAt())
                .updatedAt(todo.getUpdatedAt())
                .build();
    }

    /**
     * 统计信息DTO
     */
    @lombok.Data
    @lombok.Builder
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class TodoStatsDTO {
        /**
         * 总计数
         */
        private long total;
        
        /**
         * 未完成数
         */
        private long active;
        
        /**
         * 已完成数
         */
        private long completed;
    }
}
