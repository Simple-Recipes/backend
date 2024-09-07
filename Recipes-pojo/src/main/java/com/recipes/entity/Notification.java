package com.recipes.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Notification")
public class Notification {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long recipientId;
    private Long senderId;
    private Long recipeId;
    private Long commentId;
    private String message;
    private Boolean isRead;


    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

//    @PrePersist
//    protected void onCreate() {
//        this.createTime = LocalDateTime.now();
//    }

}

