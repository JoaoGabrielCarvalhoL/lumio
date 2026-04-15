package br.com.joaogabriel.lumio.model.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.joaogabriel.lumio.model.enumerations.SecurityEventType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_security_event")
public class SecurityEvent {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private User user;
	
	@Enumerated(EnumType.STRING)
	private SecurityEventType type;
	
	private String ipAddress;
    private String device;

    private boolean notified;

    private LocalDateTime createdAt;

}
