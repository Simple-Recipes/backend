package com.recipes.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Recipes")
public class Recipe implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(nullable = false, columnDefinition = "JSON")
    private String ingredients;

    @Column(nullable = false, columnDefinition = "JSON")
    private String directions;

    @Column(nullable = false, length = 1024)
    private String link;

    @Column(nullable = false)
    private int minutes;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "nutrition",  columnDefinition = "JSON")
    private String nutrition;


    @Column(name = "create_time", nullable = false, updatable = false)
    private LocalDateTime createTime;

    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @PrePersist
    protected void onCreate() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updateTime = LocalDateTime.now();
    }
}
