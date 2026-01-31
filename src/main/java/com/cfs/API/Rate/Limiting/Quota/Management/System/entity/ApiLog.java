package com.cfs.API.Rate.Limiting.Quota.Management.System.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name="api_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String endpoint;

    @Column(nullable = false)
    private Instant timestamp;

    @Column(nullable = false)
    private Integer status;
}
