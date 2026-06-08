package com.example.todoapp.controller;

import com.example.todoapp.dto.ApiResponse;
import com.example.todoapp.dto.PageResponse;
import com.example.todoapp.dto.TodoDTO;
import com.example.todoapp.dto.TodoStatsDTO;
import com.example.todoapp.exception.TodoException;
import com.example.todoapp.exception.TodoNotFoundException;
import com.example.todoapp.exception.TodoValidationException;
import com.example.todoapp.service.TodoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * 待办事项REST API控制器
 * 提供待办事项的CRUD以及查询、统计等接口
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/todos")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class TodoController {

    private final TodoService todoService;

    /**
     * 获取所有待办事项
     * GET /api/v1/todos
     */
    @GetMapping
    public ResponseEntity<ApiResponse<List<TodoDTO>>> getAllTodos() {
        String requestId = UUID.randomUUID().toString();
        log.info("[{}] GET /api/v1/todos - 获取所有待办事项", requestId);
        try {
            List<TodoDTO> todos = todoService.getAllTodos();
            ApiResponse<List<TodoDTO>> response = ApiResponse.success(todos, "获取待办事项成功");
            response.setRequestId(requestId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[{}] 获取待办事项失败", requestId, e);
            return handleException(requestId, e);
        }
    }

    /**
     * 分页查询待办事项
     * GET /api/v1/todos/page
     */
    @GetMapping("/page")
    public ResponseEntity<ApiResponse<PageResponse<TodoDTO>>> getTodosByPage(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder) {
        String requestId = UUID.randomUUID().toString();
        log.info("[{}] GET /api/v1/todos/page - 分页查询待办事项: pageNo={}, pageSize={}", requestId, pageNo, pageSize);
        try {
            PageResponse<TodoDTO> page = todoService.getTodosByPage(pageNo, pageSize, sortBy, sortOrder);
            ApiResponse<PageResponse<TodoDTO>> response = ApiResponse.success(page, "分页查询成功");
            response.setRequestId(requestId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[{}] 分页查询待办事项失败", requestId, e);
            return handleException(requestId, e);
        }
    }

    /**
     * 获取单个待办事项
     * GET /api/v1/todos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TodoDTO>> getTodoById(@PathVariable Long id) {
        String requestId = UUID.randomUUID().toString();
        log.info("[{}] GET /api/v1/todos/{} - 获取单个待办事项", requestId, id);
        try {
            TodoDTO todo = todoService.getTodoById(id);
            ApiResponse<TodoDTO> response = ApiResponse.success(todo, "获取待办事项成功");
            response.setRequestId(requestId);
            return ResponseEntity.ok(response);
        } catch (TodoNotFoundException e) {
            log.warn("[{}] 待办事项不存在: {}", requestId, id);
            return handleException(requestId, e);
        } catch (Exception e) {
            log.error("[{}] 获取待办事项失败", requestId, e);
            return handleException(requestId, e);
        }
    }

    /**
     * 创建新的待办事项
     * POST /api/v1/todos
     */
    @PostMapping
    public ResponseEntity<ApiResponse<TodoDTO>> createTodo(@RequestBody TodoDTO todoDTO) {
        String requestId = UUID.randomUUID().toString();
        log.info("[{}] POST /api/v1/todos - 创建待办事项", requestId);
        try {
            TodoDTO created = todoService.createTodo(todoDTO);
            ApiResponse<TodoDTO> response = ApiResponse.success(created, "待办事项创建成功");
            response.setRequestId(requestId);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (TodoValidationException e) {
            log.warn("[{}] 参数验证失败", requestId);
            return handleException(requestId, e);
        } catch (Exception e) {
            log.error("[{}] 创建待办事项失败", requestId, e);
            return handleException(requestId, e);
        }
    }

    /**
     * 更新待办事项
     * PUT /api/v1/todos/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TodoDTO>> updateTodo(
            @PathVariable Long id,
            @RequestBody TodoDTO todoDTO) {
        String requestId = UUID.randomUUID().toString();
        log.info("[{}] PUT /api/v1/todos/{} - 更新待办事项", requestId, id);
        try {
            TodoDTO updated = todoService.updateTodo(id, todoDTO);
            ApiResponse<TodoDTO> response = ApiResponse.success(updated, "待办事项更新成功");
            response.setRequestId(requestId);
            return ResponseEntity.ok(response);
        } catch (TodoNotFoundException e) {
            log.warn("[{}] 待办事项不存在: {}", requestId, id);
            return handleException(requestId, e);
        } catch (TodoValidationException e) {
            log.warn("[{}] 参数验证失败", requestId);
            return handleException(requestId, e);
        } catch (Exception e) {
            log.error("[{}] 更新待办事项失败", requestId, e);
            return handleException(requestId, e);
        }
    }

    /**
     * 删除待办��项
     * DELETE /api/v1/todos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTodo(@PathVariable Long id) {
        String requestId = UUID.randomUUID().toString();
        log.info("[{}] DELETE /api/v1/todos/{} - 删除待办事项", requestId, id);
        try {
            todoService.deleteTodo(id);
            ApiResponse<Void> response = ApiResponse.success(null, "待办事项删除成功");
            response.setRequestId(requestId);
            return ResponseEntity.ok(response);
        } catch (TodoNotFoundException e) {
            log.warn("[{}] 待办事项不存在: {}", requestId, id);
            return handleException(requestId, e);
        } catch (Exception e) {
            log.error("[{}] 删除待办事项失败", requestId, e);
            return handleException(requestId, e);
        }
    }

    /**
     * 批量删除待办事项
     * POST /api/v1/todos/batch-delete
     */
    @PostMapping("/batch-delete")
    public ResponseEntity<ApiResponse<Void>> deleteTodos(@RequestBody List<Long> ids) {
        String requestId = UUID.randomUUID().toString();
        log.info("[{}] POST /api/v1/todos/batch-delete - 批量删除待办事项", requestId);
        try {
            todoService.deleteTodos(ids);
            ApiResponse<Void> response = ApiResponse.success(null, "批量删除成功");
            response.setRequestId(requestId);
            return ResponseEntity.ok(response);
        } catch (TodoValidationException e) {
            log.warn("[{}] 参数验证失败", requestId);
            return handleException(requestId, e);
        } catch (Exception e) {
            log.error("[{}] 批量删除待办事项失败", requestId, e);
            return handleException(requestId, e);
        }
    }

    /**
     * 清空所有已完成的待办事项
     * DELETE /api/v1/todos/clear-completed
     */
    @DeleteMapping("/clear-completed")
    public ResponseEntity<ApiResponse<Void>> clearCompleted() {
        String requestId = UUID.randomUUID().toString();
        log.info("[{}] DELETE /api/v1/todos/clear-completed - 清空已完成的待办事项", requestId);
        try {
            todoService.clearCompleted();
            ApiResponse<Void> response = ApiResponse.success(null, "已完成的待办事项已清空");
            response.setRequestId(requestId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[{}] 清空已完成的待办事项失败", requestId, e);
            return handleException(requestId, e);
        }
    }

    /**
     * 清空所有待办事项
     * DELETE /api/v1/todos/clear-all
     */
    @DeleteMapping("/clear-all")
    public ResponseEntity<ApiResponse<Void>> clearAll() {
        String requestId = UUID.randomUUID().toString();
        log.warn("[{}] DELETE /api/v1/todos/clear-all - 清空所有待办事项", requestId);
        try {
            todoService.clearAll();
            ApiResponse<Void> response = ApiResponse.success(null, "所有待办事项已清空");
            response.setRequestId(requestId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[{}] 清空所有待办事项失败", requestId, e);
            return handleException(requestId, e);
        }
    }

    /**
     * 获取统计信息
     * GET /api/v1/todos/stats
     */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponse<TodoStatsDTO>> getStats() {
        String requestId = UUID.randomUUID().toString();
        log.info("[{}] GET /api/v1/todos/stats - 获取统计信息", requestId);
        try {
            TodoStatsDTO stats = todoService.getStats();
            ApiResponse<TodoStatsDTO> response = ApiResponse.success(stats, "获取统计信息成功");
            response.setRequestId(requestId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("[{}] 获取统计信息失败", requestId, e);
            return handleException(requestId, e);
        }
    }

    /**
     * 按优先级获取待办事项
     * GET /api/v1/todos/by-priority/{priority}
     */
    @GetMapping("/by-priority/{priority}")
    public ResponseEntity<ApiResponse<List<TodoDTO>>> getTodosByPriority(@PathVariable Integer priority) {
        String requestId = UUID.randomUUID().toString();
        log.info("[{}] GET /api/v1/todos/by-priority/{} - 按优先级获取待办事项", requestId, priority);
        try {
            List<TodoDTO> todos = todoService.getTodosByPriority(priority);
            ApiResponse<List<TodoDTO>> response = ApiResponse.success(todos, "获取成功");
            response.setRequestId(requestId);
            return ResponseEntity.ok(response);
        } catch (TodoValidationException e) {
            log.warn("[{}] 参数验证失败", requestId);
            return handleException(requestId, e);
        } catch (Exception e) {
            log.error("[{}] 按优先级查询待办事项失败", requestId, e);
            return handleException(requestId, e);
        }
    }

    /**
     * 按分类获取待办事项
     * GET /api/v1/todos/by-category/{category}
     */
    @GetMapping("/by-category/{category}")
    public ResponseEntity<ApiResponse<List<TodoDTO>>> getTodosByCategory(@PathVariable String category) {
        String requestId = UUID.randomUUID().toString();
        log.info("[{}] GET /api/v1/todos/by-category/{} - 按分类获取待办事项", requestId, category);
        try {
            List<TodoDTO> todos = todoService.getTodosByCategory(category);
            ApiResponse<List<TodoDTO>> response = ApiResponse.success(todos, "获取成功");
            response.setRequestId(requestId);
            return ResponseEntity.ok(response);
        } catch (TodoValidationException e) {
            log.warn("[{}] 参数验证失败", requestId);
            return handleException(requestId, e);
        } catch (Exception e) {
            log.error("[{}] 按分类查询待办事项失败", requestId, e);
            return handleException(requestId, e);
        }
    }

    /**
     * 异常处理
     */
    private <T> ResponseEntity<ApiResponse<T>> handleException(String requestId, Exception e) {
        if (e instanceof TodoNotFoundException) {
            TodoNotFoundException ex = (TodoNotFoundException) e;
            ApiResponse<T> response = ApiResponse.fail(404, ex.getMessage(), ex.getErrorCode());
            response.setRequestId(requestId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else if (e instanceof TodoValidationException) {
            TodoValidationException ex = (TodoValidationException) e;
            ApiResponse<T> response = ApiResponse.fail(400, ex.getMessage(), ex.getErrorCode());
            response.setRequestId(requestId);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } else if (e instanceof TodoException) {
            TodoException ex = (TodoException) e;
            ApiResponse<T> response = ApiResponse.fail(ex.getHttpStatus(), ex.getMessage(), ex.getErrorCode());
            response.setRequestId(requestId);
            return ResponseEntity.status(ex.getHttpStatus()).body(response);
        } else {
            ApiResponse<T> response = ApiResponse.error(e.getMessage());
            response.setRequestId(requestId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * 全局异常处理 - 400 Bad Request
     */
    @ExceptionHandler(TodoValidationException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(TodoValidationException e) {
        String requestId = UUID.randomUUID().toString();
        log.warn("[{}] 参数验证异常: {}", requestId, e.getMessage());
        ApiResponse<Void> response = ApiResponse.fail(400, e.getMessage(), e.getErrorCode());
        response.setRequestId(requestId);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * 全局异常处理 - 404 Not Found
     */
    @ExceptionHandler(TodoNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFoundException(TodoNotFoundException e) {
        String requestId = UUID.randomUUID().toString();
        log.warn("[{}] 资源未找到异常: {}", requestId, e.getMessage());
        ApiResponse<Void> response = ApiResponse.fail(404, e.getMessage(), e.getErrorCode());
        response.setRequestId(requestId);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    /**
     * 全局异常处理 - 业务异常
     */
    @ExceptionHandler(TodoException.class)
    public ResponseEntity<ApiResponse<Void>> handleTodoException(TodoException e) {
        String requestId = UUID.randomUUID().toString();
        log.error("[{}] 待办事项业务异常: {}", requestId, e.getMessage(), e);
        ApiResponse<Void> response = ApiResponse.fail(e.getHttpStatus(), e.getMessage(), e.getErrorCode());
        response.setRequestId(requestId);
        return ResponseEntity.status(e.getHttpStatus()).body(response);
    }

    /**
     * 全局异常处理 - 500 Internal Server Error
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        String requestId = UUID.randomUUID().toString();
        log.error("[{}] 服务器异常", requestId, e);
        ApiResponse<Void> response = ApiResponse.error("发生未知错误，请联系管理员");
        response.setRequestId(requestId);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
