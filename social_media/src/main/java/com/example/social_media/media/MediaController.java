package com.example.social_media.media;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/media")
public class MediaController {
    private final MediaStorageService mediaStorageService;
    private final MediaRepository mediaRepository;

    public MediaController(MediaStorageService mediaStorageService, MediaRepository mediaRepository) {
        this.mediaStorageService = mediaStorageService;
        this.mediaRepository = mediaRepository;
    }

    @PostMapping("/upload")
    public ResponseEntity<Media> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String type) throws IOException {
        
        String subDirectory = type.toLowerCase(); // profile, post, or chat
        String filePath = mediaStorageService.storeFile(file, subDirectory);

        Media media = new Media();
        media.setFileName(file.getOriginalFilename());
        media.setFilePath(filePath);
        media.setFileType(file.getContentType());
        media.setFileSize(file.getSize());

        mediaRepository.save(media);
        return ResponseEntity.ok(media);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) throws IOException {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));

        byte[] fileContent = mediaStorageService.loadFile(media.getFilePath());
        ByteArrayResource resource = new ByteArrayResource(fileContent);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(media.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + media.getFileName() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFile(@PathVariable Long id) throws IOException {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("File not found"));

        mediaStorageService.deleteFile(media.getFilePath());
        mediaRepository.delete(media);

        return ResponseEntity.ok().build();
    }
} 