package br.com.joaogabriel.lumio.service;

import br.com.joaogabriel.lumio.model.dto.request.FileUploadRequest;
import br.com.joaogabriel.lumio.model.dto.response.FileUploadResponse;

public interface ThumbnailStorageService {

    FileUploadResponse upload(String key, final FileUploadRequest request);
}
