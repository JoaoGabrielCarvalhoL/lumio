package br.com.joaogabriel.lumio.repository;

import java.util.UUID;

import br.com.joaogabriel.lumio.model.entity.UserProvisioning;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserProvisioningRepository implements PanacheRepositoryBase<UserProvisioning, UUID>{

}
