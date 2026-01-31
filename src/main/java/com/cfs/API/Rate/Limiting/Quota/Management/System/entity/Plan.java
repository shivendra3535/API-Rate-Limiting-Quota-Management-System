package com.cfs.API.Rate.Limiting.Quota.Management.System.entity;


import com.cfs.API.Rate.Limiting.Quota.Management.System.enums.SubscriptionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="plans")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Plan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SubscriptionType name;

    @Column(nullable = false)
    private Integer dailyQuota;

    @Column(nullable = false)
    private Integer softLimitThreshold;
}
