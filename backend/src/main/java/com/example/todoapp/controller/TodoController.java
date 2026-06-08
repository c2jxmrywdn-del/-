package com.example.todoapp.controller;

import com.example.todoapp.dto.TodoDTO;
import com.example.todoapp.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 待办事项REST API控制器
 */
@Slf4j
@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class TodoController {

    private final TodoService todoService;

    /**
     * 获取所有待办事项
     * GET /todos
     */
    @GetMapping
    public ResponseEntity<List<TodoDTO>> getAllTodos() {
        log.info("GET /todos - 获取所有待办事项");
        List<TodoDTO> todos = todoService.getAllTodos();
        return ResponseEntity.ok(todos);
    }

    /**
     * 获取单个待办事项
     * GET /todos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<TodoDTO> getTodoById(@PathVariable Long id) {
        log.info("GET /todos/{} - 获取单个待办事项", id);
        TodoDTO todo = todoService.getTodoById(id);
        return ResponseEntity.ok(todo);
    }

    /**
     * 创建新的待办事项
     * POST /todos
     */
    @PostMapping
    public ResponseEntity<TodoDTO> createTodo(@RequestBody TodoDTO todoDTO) {
        log.info("POST /todos - 创建待办事项");
        TodoDTO created = todoService.createTodo(todoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * 更新待办事项
     * PUT /todos/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<TodoDTO> updateTodo(
            @PathVariable Long id,
            @RequestBody TodoDTO todoDTO) {
        log.info("PUT /todos/{} - 更新待办事项", id);
        TodoDTO updated = todoService.updateTodo(id, todoDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * 删除待办事项
     * DELETE /todos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable Long id) {
        log.info("DELETE /todos/{} - 删除待办事项", id);
        todoService.deleteTodo(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * 清空所有已完成的待办事项
     * DELETE /todos/completed/clear
     */
    @DeleteMapping("/completed/clear")
    public ResponseEntity<Void> clearCompleted() {
        log.info("DELETE /todos/completed/clear - 清空已完成的待办事项");
        todoService.clearCompleted();
        return ResponseEntity.noContent().build();
    }

    /**
     * 清空所有待办事项
     * DELETE /todos/all/clear
     */
    @DeleteMapping("/all/clear")
    public ResponseEntity<Void> clearAll() {
        log.warn("DELETE /todos/all/clear - 清空所有待办事项");
        todoService.clearAll();
        return ResponseEntity.noContent().build();
    }

    /**
     * 获取统计信息
     * GET /todos/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<TodoService.TodoStatsDTO> getStats() {
        log.info("GET /todos/stats - 获取统计信息");
        TodoService.TodoStatsDTO stats = todoService.getStats();
        return ResponseEntity.ok(stats);
    }

    /**
     * 全局异常处理
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error("参数异常: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("参数错误", e.getMessage()));
    }

    /**
     * 通用异常处理
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("服务器异常", e);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("服务器错误", "发生未知错误"));
    }

    /**
     * 错误响应对象
     */
    @lombok.Data
    @lombok.AllArgsConstructor
    public static class ErrorResponse {
        private String error;
        private String message;
    }
}
