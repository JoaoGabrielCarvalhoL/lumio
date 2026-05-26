package br.com.joaogabriel.lumio.service;

import br.com.joaogabriel.lumio.model.dto.request.FileUploadRequest;
import br.com.joaogabriel.lumio.model.dto.response.FileUploadResponse;
import br.com.joaogabriel.lumio.model.entity.Course;

import java.util.UUID;

public interface TrailerStorageService {

    FileUploadResponse upload(String key, final FileUploadRequest request);
}
