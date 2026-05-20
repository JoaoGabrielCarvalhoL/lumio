package br.com.joaogabriel.lumio.repository;

import br.com.joaogabriel.lumio.model.entity.Address;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.UUID;

@ApplicationScoped
public class AddressRepository implements PanacheRepositoryBase<Address, UUID> {
}
