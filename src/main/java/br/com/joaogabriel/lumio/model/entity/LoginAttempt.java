package br.com.joaogabriel.lumio.model.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.joaogabriel.lumio.model.enumerations.LoginStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_login_attempt", indexes = {
	@Index(name = "idx_login_user", columnList = "user_id"),
    @Index(name = "idx_login_ip", columnList = "ip_address")	
})
public class LoginAttempt {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	private String username; 
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	
	@Column(nullable = false, name = "ip_address")
	private String ipAddress;
	
	private String userAgent;
	
	@Enumerated(EnumType.STRING)
	private LoginStatus status;
	
	private String failureReason;
	
	private LocalDateTime createdAt;

}
