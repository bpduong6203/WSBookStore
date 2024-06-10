package com.bookstore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.extern.apachecommons.CommonsLog;

import java.util.List;

@Data
@Entity
@Table
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 50, message = "Tên quyển cho phép tối đa 50 ký tự")
    @NotBlank(message = "Tên quyển không được đẻ trống")
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Size(max = 250, message = "Mô tả quyền cho phép tôi da 250 ký tự")
    @Column(name = "description", length = 250)
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_role",
        joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> users;
}
