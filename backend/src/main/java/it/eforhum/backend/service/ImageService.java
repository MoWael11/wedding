package it.eforhum.backend.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import it.eforhum.backend.dto.ImageResponse;
import it.eforhum.backend.entity.Image;
import it.eforhum.backend.entity.User;
import it.eforhum.backend.entity.WeddingEvent;
import it.eforhum.backend.exception.InvalidFileException;
import it.eforhum.backend.exception.NotFoundException;
import it.eforhum.backend.exception.StorageLimitException;
import it.eforhum.backend.repository.ImageRepository;
import it.eforhum.backend.repository.WeddingEventRepository;

@Service
public class ImageService {

    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB
    private static final long MAX_WEDDING_SIZE = 200 * 1024 * 1024; // 200MB
    private static final Set<String> ALLOWED_TYPES = Set.of("image/jpeg", "image/jpg", "image/png");

    private final ImageRepository imageRepository;
    private final WeddingEventRepository weddingEventRepository;

    @Value("${app.upload.path}")
    private String uploadPath;

    @Value("${app.base.url}")
    private String baseUrl;

    public ImageService(ImageRepository imageRepository, WeddingEventRepository weddingEventRepository) {
        this.imageRepository = imageRepository;
        this.weddingEventRepository = weddingEventRepository;
    }

    @Transactional
    public ImageResponse uploadImage(Integer weddingId, MultipartFile file, User uploader) {
        WeddingEvent wedding = weddingEventRepository.findById(weddingId)
                .orElseThrow(() -> new NotFoundException("Wedding not found"));

        validateFile(file);

        // wedding storage limit
        long newTotalBytes = wedding.getTotalBytes() + file.getSize();
        if (newTotalBytes > MAX_WEDDING_SIZE) {
            throw new StorageLimitException("Wedding storage limit exceeded (max 200MB)");
        }

        // unique filename
        String originalFilename = file.getOriginalFilename();
        String extension = getExtension(originalFilename);
        String storedFilename = UUID.randomUUID().toString() + extension;
        String relativePath = "images/" + wedding.getCode() + "/" + storedFilename;

        try {
            Path uploadDir = Paths.get(uploadPath, wedding.getCode());
            Files.createDirectories(uploadDir);
            Path filePath = uploadDir.resolve(storedFilename);
            Files.write(filePath, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image file", e);
        }

        Image image = new Image();
        image.setOriginalFilename(originalFilename);
        image.setStoredFilename(storedFilename);
        image.setRelativePath(relativePath);
        image.setMimeType(file.getContentType());
        image.setSizeBytes((int) file.getSize());
        image.setUploadedAt(LocalDateTime.now());
        image.setEvent(wedding);
        image.setUploader(uploader);

        image = imageRepository.save(image);

        wedding.setTotalBytes((int) newTotalBytes);
        weddingEventRepository.save(wedding);

        // Build response with known uploader username to avoid lazy loading
        String imageUrl = baseUrl + "/images/" + wedding.getCode() + "/" + storedFilename;
        return new ImageResponse(
                image.getId(),
                originalFilename,
                imageUrl,
                file.getContentType(),
                (int) file.getSize(),
                image.getUploadedAt(),
                uploader.getUsername()
        );
    }

    public List<ImageResponse> getImagesByWeddingId(Integer weddingId) {
        if (!weddingEventRepository.existsById(weddingId)) {
            throw new NotFoundException("Wedding not found");
        }

        return imageRepository.findByEventId(weddingId).stream()
                .map(this::toResponse)
                .toList();
    }

    public ImageResponse getImageById(Integer weddingId, Integer imageId) {
        Image image = imageRepository.findByIdWithUploader(imageId)
                .orElseThrow(() -> new NotFoundException("Image not found"));

        if (!image.getEvent().getId().equals(weddingId)) {
            throw new NotFoundException("Image not found in this wedding");
        }

        return toResponse(image);
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new InvalidFileException("File is required");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new InvalidFileException("File size exceeds limit (max 5MB)");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType.toLowerCase())) {
            throw new InvalidFileException("Invalid file type. Allowed: JPG, JPEG, PNG");
        }
    }

    private String getExtension(String filename) {
        if (filename == null) return ".jpg";
        int dotIndex = filename.lastIndexOf('.');
        return dotIndex > 0 ? filename.substring(dotIndex) : ".jpg";
    }

    private ImageResponse toResponse(Image image) {
        String imageUrl = baseUrl + "/images/" + image.getEvent().getCode() + "/" + image.getStoredFilename();
        String uploaderUsername = image.getUploader() != null ? image.getUploader().getUsername() : null;

        return new ImageResponse(
                image.getId(),
                image.getOriginalFilename(),
                imageUrl,
                image.getMimeType(),
                image.getSizeBytes(),
                image.getUploadedAt(),
                uploaderUsername
        );
    }
}
