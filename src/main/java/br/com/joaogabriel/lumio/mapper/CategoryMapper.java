package br.com.joaogabriel.lumio.mapper;

import br.com.joaogabriel.lumio.model.dto.request.CategoryCreateRequest;
import br.com.joaogabriel.lumio.model.dto.response.CategoryResponse;
import br.com.joaogabriel.lumio.model.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.CDI,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    CategoryResponse toCategoryResponse(final Category category);

    Category toCategory(final CategoryCreateRequest categoryCreateRequest);
}
