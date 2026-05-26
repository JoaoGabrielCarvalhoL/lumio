package br.com.joaogabriel.lumio.service;

import br.com.joaogabriel.lumio.model.dto.request.CourseCreateRequest;
import br.com.joaogabriel.lumio.model.dto.request.CourseUpdateRequest;
import br.com.joaogabriel.lumio.model.dto.request.FileUploadRequest;
import br.com.joaogabriel.lumio.model.dto.response.CourseResponse;

import java.util.List;
import java.util.UUID;

public interface CourseService {

    CourseResponse findById(final UUID id);

    List<CourseResponse> findByName(final String name);

    List<CourseResponse> findAllPublished(int page, int size);

    CourseResponse save(final CourseCreateRequest courseCreateRequest, final UUID categoryId);

    CourseResponse update(final CourseUpdateRequest courseUpdateRequest);

    void delete(final UUID id);

    void uploadThumbnail(final UUID courseId, final FileUploadRequest request);

    void uploadFreeTrailer(final UUID courseId, final FileUploadRequest request);
}
