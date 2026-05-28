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
	private ContactType type;
	
	@Column(nullable = false)
	private String contact;
	
	private Boolean primary;
	
	private Boolean verified;
	
	public Contact() {}

	public Contact(ContactType type, String contact, Boolean primary) {
		this.type = type;
		this.contact = contact;
		this.primary = primary;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public ContactType getType() {
		return type;
	}

	public void setType(ContactType type) {
		this.type = type;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public Boolean getPrimary() {
		return primary;
	}

	public void setPrimary(Boolean primary) {
		primary = primary;
	}

	public Boolean getVerified() {
		return verified;
	}

	public void setVerified(Boolean verified) {
		verified = verified;
	}
}
