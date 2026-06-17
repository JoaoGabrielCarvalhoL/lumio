package br.com.joaogabriel.lumio.service;

import br.com.joaogabriel.lumio.model.dto.request.FileUploadRequest;
import br.com.joaogabriel.lumio.model.dto.response.FileUploadResponse;
import br.com.joaogabriel.lumio.model.dto.response.VideoUploadResponse;

import java.util.UUID;

public interface VideoStorageService {

    FileUploadResponse upload(UUID courseId, UUID lessonId, FileUploadRequest request);

    VideoUploadResponse initiateVideoUpload(UUID courseId, UUID lessonId, FileUploadRequest fileUploadRequest);
}
