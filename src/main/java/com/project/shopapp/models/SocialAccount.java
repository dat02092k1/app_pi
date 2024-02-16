package com.project.shopapp.models;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "social_accounts")
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Data
public class SocialAccount {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(name = "provider_id", nullable = false, length = 50)
    private String providerId;

    @Column(name = "provider", nullable = false, length = 20)
    private String provider;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "name", length = 100)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
