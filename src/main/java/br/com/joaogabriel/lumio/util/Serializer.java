package br.com.joaogabriel.lumio.util;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.joaogabriel.lumio.exception.SerializationException;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Serializer {
	
	private final ObjectMapper objectMapper;
	
	public Serializer(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
	}
	
	public <T> String serialize(T request) {
		try {
			return this.objectMapper.writeValueAsString(request);
		} catch (Exception e) {
			throw new SerializationException("Failed to serialize payload.", e);
		}
	}
	
	public <T> T deserialize(String payload, Class<T> clazz) {
	    try {
	        return objectMapper.readValue(payload, clazz);
	    } catch (Exception e) {
	        throw new SerializationException("Failed to deserialize payload.", e);
	    }
	}
}
