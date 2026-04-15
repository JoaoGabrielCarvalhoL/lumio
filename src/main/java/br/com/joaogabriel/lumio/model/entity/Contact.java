package br.com.joaogabriel.lumio.model.entity;

import java.util.UUID;

import br.com.joaogabriel.lumio.model.enumerations.ContactType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_contact")
public class Contact {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Enumerated(EnumType.STRING)
	private ContactType contactType;
	
	@Column(nullable = false)
	private String contact;
	
	private Boolean isPrimary;
	
	private Boolean isVerified;
	
	public Contact() {}

}
