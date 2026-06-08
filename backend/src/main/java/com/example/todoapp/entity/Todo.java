package com.example.todoapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 待办事项实体类
 * 代表数据库中的一条待办事项记录
 */
@Entity
@Table(name = "todos", indexes = {
    @Index(name = "idx_completed", columnList = "completed"),
    @Index(name = "idx_created_at", columnList = "created_at"),
    @Index(name = "idx_updated_at", columnList = "updated_at"),
    @Index(name = "idx_priority", columnList = "priority"),
    @Index(name = "idx_category", columnList = "category")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Todo {

    /**
     * 主键ID，自增
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 任务文本内容，不允许为空，最大长度500
     */
    @Column(nullable = false, length = 500)
    private String text;

    /**
     * 是否完成，默认false
     */
    @Column(nullable = false)
    @Builder.Default
    private Boolean completed = false;

    /**
     * 任务优先级：1-低，2-中，3-高
     */
    @Column(name = "priority", nullable = false)
    @Builder.Default
    private Integer priority = 2;

    /**
     * 任务分类/标签
     */
    @Column(name = "category", length = 100)
    private String category;

    /**
     * 任务描述
     */
    @Column(name = "description", length = 1000)
    private String description;

    /**
     * 创建时间，不可更新
     */
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 完成时间
     */
    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    /**
     * 版本号，用于乐观锁
     */
    @Version
    private Long version;

    /**
     * 新增前自动设置时间戳
     */
    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
        if (this.priority == null) {
            this.priority = 2;
        }
    }

    /**
     * 更新前自动更新时间戳
     */
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        // 如果标记为完成且之前未完成，则记录完成时间
        if (this.completed && this.completedAt == null) {
            this.completedAt = LocalDateTime.now();
        }
        // 如果标记为未完成，清除完成时间
        if (!this.completed) {
            this.completedAt = null;
        }
    }

    /**
     * 获取任务完成所耗时间（毫秒）
     */
    public Long getCompletionDurationMs() {
        if (completed && completedAt != null) {
            return java.time.temporal.ChronoUnit.MILLIS.between(createdAt, completedAt);
        }
        return null;
    }
}
