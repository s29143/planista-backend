package edu.pjatk.planista.config.security;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(indexes = @Index(name="ux_jti_hash", columnList="jtiHash", unique=true))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(nullable=false, unique=true, length=64)
    private String jtiHash;
    @Column(nullable=false)
    private String username;
    @Column(nullable=false)
    private Instant expiresAt;
    @Column(nullable=false)
    private boolean revoked = false;

    public RefreshToken(String jtiHash, String username, Instant expiresAt, boolean revoked) {
        this.jtiHash = jtiHash;
        this.username = username;
        this.expiresAt = expiresAt;
        this.revoked = revoked;
    }
}