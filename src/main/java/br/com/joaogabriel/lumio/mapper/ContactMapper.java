package br.com.joaogabriel.lumio.mapper;

import br.com.joaogabriel.lumio.model.dto.request.ContactCreateRequest;
import br.com.joaogabriel.lumio.model.dto.response.ContactResponse;
import br.com.joaogabriel.lumio.model.entity.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ContactMapper {

    Contact toContact(final ContactCreateRequest request);

    ContactResponse toContactResponse(final Contact contact);
}
