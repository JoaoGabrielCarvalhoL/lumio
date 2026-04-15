package br.com.joaogabriel.lumio.model.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.joaogabriel.lumio.model.enumerations.AddressType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_address", indexes = {
		@Index(name = "idx_address_zipcode", columnList = "zip_code")
})
public class Address {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "zip_code", nullable = false, length = 8)
    private String zipCode; 

    @Column(nullable = false)
    private String street;

    @Column(nullable = false)
    private String number;

    @Column(nullable = true)
    private String complement;

    @Column(nullable = false)
    private String neighborhood;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false, length = 2)
    private String state;

    @Column(nullable = false)
    private String country;
    
    @Enumerated(EnumType.STRING)
    private AddressType type;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    public Address() {}

}
