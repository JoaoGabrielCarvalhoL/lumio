package br.com.joaogabriel.lumio.model.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.joaogabriel.lumio.model.enumerations.ProvisioningStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
	
	private String username; 
	
	private String email; 
	
	private String externalId;
	
	private Integer retryCount;
	
	private String errorMessage;
	
	@Enumerated(EnumType.STRING)
	private ProvisioningStatus status;
	
	@Column(columnDefinition = "TEXT")
    private String payload;
	
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	
	public UserProvisioning() {}
	
	public UserProvisioning(String username, String email, String externalId, Integer retryCount,
			String errorMessage, ProvisioningStatus status, String payload) {
		this.username = username;
		this.email = email;
		this.externalId = externalId;
		this.retryCount = retryCount;
		this.errorMessage = errorMessage;
		this.status = status;
		this.payload = payload;
	}



	public UserProvisioning(int retryCount, String errorMessage,
			ProvisioningStatus status, String payload) {
		this.retryCount = retryCount;
		this.errorMessage = errorMessage;
		this.status = status;
		this.payload = payload;
	}

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

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public ProvisioningStatus getStatus() {
		return status;
	}

	public void setStatus(ProvisioningStatus status) {
		this.status = status;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public String getExternalId() {
		return externalId;
	}
}
