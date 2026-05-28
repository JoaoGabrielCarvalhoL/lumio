package br.com.joaogabriel.lumio.model.enumerations;

public enum S3EventType {
    OBJECT_CREATED,
    OBJECT_REMOVED,
    UNKNOWN;

    public static S3EventType fromString(String value) {
        if (value == null) return UNKNOWN;
        if (value.startsWith("s3:ObjectCreated") || value.equals("OBJECT_CREATED")) {
            return OBJECT_CREATED;
        }
        if (value.startsWith("s3:ObjectRemoved") || value.equals("OBJECT_REMOVED")) {
            return OBJECT_REMOVED;
        }
        return UNKNOWN;
    }
}
