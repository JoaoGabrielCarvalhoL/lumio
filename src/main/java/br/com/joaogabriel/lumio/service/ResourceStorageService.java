package br.com.joaogabriel.lumio.service;

import br.com.joaogabriel.lumio.model.dto.request.FileUploadRequest;
import br.com.joaogabriel.lumio.model.dto.response.FileUploadResponse;

public interface ResourceStorageService {

    FileUploadResponse upload(final String key, final FileUploadRequest request);
}
