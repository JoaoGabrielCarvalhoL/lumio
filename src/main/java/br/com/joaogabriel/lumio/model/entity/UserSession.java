package br.com.joaogabriel.lumio.model.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_user_session", indexes = {
	@Index(name = "idx_session_user", columnList = "user_id"), 
	@Index(name = "idx_session_ip", columnList = "ip_address")
})
public class UserSession {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(name = "ip_address", nullable = false)
	private String ipAddress;
	
	@Column(name = "user_agent", nullable = false)
	private String userAgent;
	
	@Column(nullable = false)
	private String device; 
	
	@Column(nullable = false)
	private String browser;
	
	@Column(name = "operating_system", nullable = false)
	private String operatingSystem;
	
	@Column(nullable = false)
	private String country;
	
	@Column(nullable = false)
	private String city;
	
	@Column(nullable = false)
	private Boolean isActive;
	
	private LocalDateTime lastAccessAt;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private User user;
	
	private LocalDateTime createdAt;
	
	public UserSession() {}
}
