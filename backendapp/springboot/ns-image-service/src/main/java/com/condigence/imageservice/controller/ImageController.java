package com.condigence.imageservice.controller;


import com.condigence.imageservice.dto.ImageDTO;
import com.condigence.imageservice.dto.ImageSummary;
import com.condigence.imageservice.entity.Image;
import com.condigence.imageservice.service.ImageService;
import com.condigence.imageservice.util.AppProperties;
import com.condigence.imageservice.util.CustomErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;

@RestController
@RequestMapping("/neerseva/api/v1/images")
@CrossOrigin(origins = "*")
@SuppressWarnings("unused") // controller methods are invoked by Spring via reflection
public class ImageController {

    private final ImageService imageService;
    private final AppProperties app;
    private final Environment env;

    public static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    public ImageController(ImageService imageService, AppProperties app, Environment env) {
        this.imageService = imageService;
        this.app = app;
        this.env = env;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uplaodImage(@RequestParam("myFile") MultipartFile file,
                                         @RequestParam("moduleName") String moduleName) throws Exception {
        String imagePath = app.getResolvedLocation();
        Image savedImageObj;
        try {
            // save image into db and directory
            savedImageObj = imageService.store(file, moduleName, imagePath);
        } catch (Exception e) {
            logger.warn("FAIL to upload {}: {}", file.getOriginalFilename(), e.getMessage());
            throw e;
        }

        // Build DTO and populate metadata from savedImageObj
        ImageDTO dto = new ImageDTO();
        dto.setId(savedImageObj.getId());
        // name in DB is the stored filename; original filename is available from MultipartFile
        dto.setName(savedImageObj.getName());
        dto.setImageName(savedImageObj.getImageName());
        dto.setImageSize(savedImageObj.getImageSize());
        dto.setModuleName(savedImageObj.getModuleName());
        dto.setType(savedImageObj.getType());

        // Read the stored file and set pic as Base64 string
        try {
            String baseDirRaw = app.getResolvedLocation();
            if (baseDirRaw == null || baseDirRaw.isBlank()) {
                baseDirRaw = "D:/gitrepo/neer-seva/backendapp/springboot/ns-image-service/neerseva-images";
            }
            java.nio.file.Path imagesBase = java.nio.file.Paths.get(baseDirRaw.replace('/', java.io.File.separatorChar));
            java.nio.file.Path imgPath = imagesBase.resolve(savedImageObj.getName()).normalize();
            if (java.nio.file.Files.exists(imgPath) && imgPath.startsWith(imagesBase)) {
                byte[] bytes = java.nio.file.Files.readAllBytes(imgPath);
                if (bytes.length > 0) {
                    String base64 = Base64.getEncoder().encodeToString(bytes);
                    dto.setPic(base64);
                } else {
                    dto.setPic("");
                }
            } else {
                logger.warn("Uploaded file was saved but not found on disk for id={} name={}", savedImageObj.getId(), savedImageObj.getName());
                dto.setPic("");
            }
        } catch (Exception ex) {
            logger.warn("Failed to read stored image file for id={}: {}", savedImageObj.getId(), ex.getMessage());
            dto.setPic("");
        }

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getImageWithId(@PathVariable("id") Long id) {
        Image image = null;
        try {
            String dsUrl = env.getProperty("spring.datasource.url", "<not configured>");
            String[] activeProfiles = env.getActiveProfiles();
            String active = activeProfiles.length > 0 ? String.join(",", activeProfiles) : "<none>";

            image = imageService.getImage(id);
        } catch (Exception e) {
            logger.error("Error fetching image id={}", id, e);
        }
        if (null == image) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.status(HttpStatus.OK).body(image);
    }

    @GetMapping("/name/{imageName}")
    public ResponseEntity<?> getImageWithName(@PathVariable("imageName") String imageName) {
        Image image = null;
        try {
            image = imageService.getImageByName(imageName).orElse(null);
        } catch (Exception e) {
            logger.error("Error fetching image by name={}", imageName, e);
        }
        if (null == image) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(image);
    }

    /**
     * Return image bytes (Content-Type set from stored MIME type). Use this to serve the actual image.
     */
    @GetMapping(value = "/{id}/data")
    public ResponseEntity<?> getImageData(@PathVariable("id") Long id) {
        try {
            // Get metadata from DB
            Image image = imageService.getImage(id);
            if (image == null) return ResponseEntity.notFound().build();

            // Resolve base images directory from AppProperties (normalized, forward-slash style)
            String baseDirRaw = app.getResolvedLocation();
            if (baseDirRaw == null || baseDirRaw.isBlank()) {
                // fallback to original default if AppProperties didn't provide it
                baseDirRaw = "D:/gitrepo/neer-seva/backendapp/springboot/ns-image-service/neerseva-images";
            }
            // Normalize to system path separators and build Path
            java.nio.file.Path imagesBase = java.nio.file.Paths.get(baseDirRaw.replace('/', java.io.File.separatorChar));

            // Sanitize filename (prevent path traversal) and build path
            String rawName = image.getName();
            if (rawName == null || rawName.isBlank()) return ResponseEntity.notFound().build();
            String safeName = java.nio.file.Paths.get(rawName).getFileName().toString();

            java.nio.file.Path imgPath = imagesBase.resolve(safeName).normalize();
            if (!java.nio.file.Files.exists(imgPath) || !imgPath.startsWith(imagesBase)) {
                logger.warn("Image file not found or outside base directory: {}", imgPath);
                return ResponseEntity.notFound().build();
            }

            byte[] bytes = java.nio.file.Files.readAllBytes(imgPath);
            if (bytes.length == 0) return ResponseEntity.notFound().build();

            // Convert to base64 string
            String base64 = Base64.getEncoder().encodeToString(bytes);

            // Build DTO
            ImageDTO dto = new ImageDTO();
            dto.setId(image.getId());
            dto.setName(image.getName());
            dto.setImageName(image.getImageName());
            dto.setImageSize(image.getImageSize());
            dto.setModuleName(image.getModuleName());
            dto.setType(image.getType());
            dto.setPic(base64);

            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error streaming image data for id={}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping(value = "/{id}/raw")
    public ResponseEntity<?> getImageRaw(@PathVariable("id") Long id) {
        try {
            Image image = imageService.getImage(id);
            if (image == null) return ResponseEntity.notFound().build();

            String baseDirRaw = app.getResolvedLocation();
            if (baseDirRaw == null || baseDirRaw.isBlank()) baseDirRaw = "D:/gitrepo/neer-seva/backendapp/springboot/ns-image-service/neerseva-images";
            java.nio.file.Path imagesBase = java.nio.file.Paths.get(baseDirRaw.replace('/', java.io.File.separatorChar));

            String rawName = image.getName();
            if (rawName == null || rawName.isBlank()) return ResponseEntity.notFound().build();
            String safeName = java.nio.file.Paths.get(rawName).getFileName().toString();

            java.nio.file.Path imgPath = imagesBase.resolve(safeName).normalize();
            if (!java.nio.file.Files.exists(imgPath) || !imgPath.startsWith(imagesBase)) {
                logger.warn("Image file not found or outside base directory: {}", imgPath);
                return ResponseEntity.notFound().build();
            }

            byte[] bytes = java.nio.file.Files.readAllBytes(imgPath);
            if (bytes.length == 0) return ResponseEntity.notFound().build();

            String contentType = (image.getType() != null && !image.getType().isBlank()) ? image.getType() : "application/octet-stream";
            return ResponseEntity.ok()
                    .header("Content-Type", contentType)
                    .header("Content-Length", String.valueOf(bytes.length))
                    .header("Content-Disposition", "inline; filename=\"" + safeName + "\"")
                    .body(bytes);
        } catch (Exception e) {
            logger.error("Error streaming raw image for id={}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /all - return all images from the database
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllImages(@RequestParam(value = "page", defaultValue = "0") int page,
                                          @RequestParam(value = "size", defaultValue = "50") int size) {
        try {
            Page<ImageSummary> summaries = imageService.getImageSummaries(page, size);
            if (summaries == null || summaries.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(summaries);
        } catch (Exception e) {
            logger.error("Error fetching all images (paged)", e);
            return new ResponseEntity<>(new CustomErrorType("Failed to fetch images"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
