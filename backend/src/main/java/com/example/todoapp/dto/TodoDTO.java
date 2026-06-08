package com.example.todoapp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 待办事项数据传输对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoDTO {

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 任务文本内容
     */
    private String text;

    /**
     * 是否完成
     */
    private Boolean completed;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
