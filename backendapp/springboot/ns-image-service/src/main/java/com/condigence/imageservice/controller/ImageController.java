package com.condigence.imageservice.controller;

import com.condigence.imageservice.dto.ImageDTO;
import com.condigence.imageservice.dto.ImageSummary;
import com.condigence.imageservice.entity.Image;
import com.condigence.imageservice.service.ExternalService;
import com.condigence.imageservice.service.ImageService;
import com.condigence.imageservice.util.AppProperties;
import com.condigence.imageservice.util.CustomErrorType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/neerseva/api/v1/images")
@CrossOrigin(origins = "*")
@SuppressWarnings("unused") // controller methods are invoked by Spring via reflection
@Tag(name = "Images", description = "Operations for uploading and retrieving images")
public class ImageController {

    private final ImageService imageService;
    private final ExternalService externalService;
    private final AppProperties app;
    private final Environment env;

    public static final Logger logger = LoggerFactory.getLogger(ImageController.class);

    public ImageController(ImageService imageService, ExternalService externalService, AppProperties app, Environment env) {
        this.imageService = imageService;
        this.externalService = externalService;
        this.app = app;
        this.env = env;
    }

    @Operation(summary = "Upload image", description = "Upload an image file and store its metadata and contents")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Image uploaded successfully",
                    content = @Content(schema = @Schema(implementation = ImageDTO.class))),
            @ApiResponse(responseCode = "500", description = "Server misconfiguration or storage error",
                    content = @Content(schema = @Schema(implementation = CustomErrorType.class)))
    })
    @PostMapping(value = "/upload")
    public ResponseEntity<?> uplaodImage(@RequestParam("myFile") MultipartFile file,
                                         @RequestParam("moduleName") String moduleName) throws Exception {
        String imagePath = app.getResolvedLocation();
        if (imagePath == null || imagePath.isBlank()) {
            logger.error("app.location is not configured (application.yml / APP_LOCATION / -Dapp.base.path)");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new CustomErrorType("Server misconfiguration: app.location not set"));
        }

        Image savedImageObj;
        try {
            // save image into db and directory
            savedImageObj = imageService.store(file, moduleName, imagePath);
        } catch (Exception e) {
            logger.warn("FAIL to upload {}: {}", file.getOriginalFilename(), e.getMessage());
            throw e;
        }

        // Build DTO using service helper (service will handle reading bytes)
        ImageDTO dto;
        try {
            byte[] bytes = imageService.readImageBytesByPath(savedImageObj.getName(), imagePath);
            dto = imageService.toDto(savedImageObj, bytes);
        } catch (Exception ex) {
            logger.warn("Failed to read stored image file for id={}: {}", savedImageObj.getId(), ex.getMessage());
            dto = imageService.toDto(savedImageObj, null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(dto);
    }

    @Operation(summary = "Get image metadata by id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Image metadata found",
                    content = @Content(schema = @Schema(implementation = Image.class))),
            @ApiResponse(responseCode = "404", description = "Image not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getImageWithId(@PathVariable("id") Long id) {
        Image image = null;
        try {
            image = imageService.getImage(id);
        } catch (Exception e) {
            logger.error("Error fetching image id={}", id, e);
        }
        if (null == image) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.status(HttpStatus.OK).body(image);
    }

    @Operation(summary = "Get image metadata by name")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Image metadata found",
                    content = @Content(schema = @Schema(implementation = Image.class))),
            @ApiResponse(responseCode = "404", description = "Image not found")
    })
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
     * Return image bytes wrapped in ImageDTO with base64 pic.
     */
    @Operation(summary = "Get image data as DTO", description = "Returns image metadata plus base64 encoded image data")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Image data found",
                    content = @Content(schema = @Schema(implementation = ImageDTO.class))),
            @ApiResponse(responseCode = "404", description = "Image or image data not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping(value = "/{id}/data")
    public ResponseEntity<?> getImageData(@PathVariable("id") Long id) {
        try {
            // Get metadata from DB
            Image image = imageService.getImage(id);
            if (image == null) return ResponseEntity.notFound().build();

            // Resolve base images directory from AppProperties (normalized, forward-slash style)
            String baseDirRaw = app.getResolvedLocation();
            if (baseDirRaw == null || baseDirRaw.isBlank()) {
                logger.error("app.location is not configured; cannot read image files from disk");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new CustomErrorType("Server misconfiguration: app.location not set"));
            }

            byte[] bytes = imageService.readImageBytesByPath(image.getName(), baseDirRaw);
            if (bytes == null || bytes.length == 0) return ResponseEntity.notFound().build();

            ImageDTO dto = imageService.toDto(image, bytes);

            return ResponseEntity.ok(dto);
        } catch (Exception e) {
            logger.error("Error streaming image data for id={}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Get raw image bytes", description = "Streams the original image bytes with appropriate content type")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Image bytes streamed"),
            @ApiResponse(responseCode = "404", description = "Image or image data not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping(value = "/{id}/raw")
    public ResponseEntity<?> getImageRaw(@PathVariable("id") Long id) {
        try {
            Image image = imageService.getImage(id);
            if (image == null) return ResponseEntity.notFound().build();

            String baseDirRaw = app.getResolvedLocation();
            if (baseDirRaw == null || baseDirRaw.isBlank()) {
                logger.error("app.location is not configured; cannot read image files from disk");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new CustomErrorType("Server misconfiguration: app.location not set"));
            }

            byte[] bytes = imageService.readImageBytesByPath(image.getName(), baseDirRaw);
            if (bytes == null || bytes.length == 0) return ResponseEntity.notFound().build();

            String contentType = (image.getType() != null && !image.getType().isBlank()) ? image.getType() : "application/octet-stream";
            return ResponseEntity.ok()
                    .header("Content-Type", contentType)
                    .header("Content-Length", String.valueOf(bytes.length))
                    .header("Content-Disposition", "inline; filename=\"" + java.nio.file.Paths.get(image.getName()).getFileName().toString() + "\"")
                    .body(bytes);
        } catch (Exception e) {
            logger.error("Error streaming raw image for id={}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /all - return all images from the database
     */
    @Operation(summary = "List images (paged)", description = "Return a paged list of image summaries")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Page of images returned"),
            @ApiResponse(responseCode = "204", description = "No images found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
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

    @Operation(summary = "Ping external service", description = "Example endpoint hitting an external service using RestTemplate")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "External service responded"),
            @ApiResponse(responseCode = "500", description = "External call error")
    })
    @GetMapping("/ping-external")
    public ResponseEntity<?> pingExternal() {
        return externalService.pingExternal();
    }
}
