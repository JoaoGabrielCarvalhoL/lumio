package br.com.joaogabriel.lumio.model.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Table(name = "tb_user_provisioning")
@Entity
public class UserProvisioning {

	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	private int retryCount;
	
	private String errorMessage;
	
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	public UserProvisioning() {}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	private void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	private void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	@PrePersist
	public void setupCreatedAt() {
		setCreatedAt(LocalDateTime.now());
	}
	
	@PreUpdate
	public void setupUpdatedAt() {
		setUpdatedAt(LocalDateTime.now());
	}
	
	
	
}
