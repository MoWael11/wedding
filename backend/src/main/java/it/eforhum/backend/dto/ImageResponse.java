package it.eforhum.backend.dto;

import java.time.LocalDateTime;

public class ImageResponse {

    private Integer id;
    private String originalFilename;
    private String url;
    private String mimeType;
    private Integer sizeBytes;
    private LocalDateTime uploadedAt;
    private String uploadedBy;

    public ImageResponse() {}

    public ImageResponse(Integer id, String originalFilename, String url, String mimeType,
                         Integer sizeBytes, LocalDateTime uploadedAt, String uploadedBy) {
        this.id = id;
        this.originalFilename = originalFilename;
        this.url = url;
        this.mimeType = mimeType;
        this.sizeBytes = sizeBytes;
        this.uploadedAt = uploadedAt;
        this.uploadedBy = uploadedBy;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }

    public void setOriginalFilename(String originalFilename) {
        this.originalFilename = originalFilename;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Integer getSizeBytes() {
        return sizeBytes;
    }

    public void setSizeBytes(Integer sizeBytes) {
        this.sizeBytes = sizeBytes;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }
}
