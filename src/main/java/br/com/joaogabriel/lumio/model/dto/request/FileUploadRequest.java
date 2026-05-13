package br.com.joaogabriel.lumio.model.dto.request;

import jakarta.ws.rs.FormParam;
import org.jboss.resteasy.reactive.multipart.FileUpload;

public record FileUploadRequest(
        @FormParam("file")
        FileUpload file
) {
}
