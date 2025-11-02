package com.condigence.imageservice.controller;


import com.condigence.imageservice.entity.Image;
import com.condigence.imageservice.service.ImageService;
import com.condigence.imageservice.util.AppProperties;
import com.condigence.imageservice.util.CustomErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
        Image image = new Image();
        image.setModuleName(moduleName);
        // We store image binary on the filesystem; imagePath contains the file location
        try {
            // save image into db and directory
            savedImageObj = imageService.store(file, moduleName, imagePath);
        } catch (Exception e) {
            logger.warn("FAIL to upload {}: {}", file.getOriginalFilename(), e.getMessage());
            throw e;
        }
        // Now return back the saved image metadata
        image.setId(savedImageObj.getId());
        image.setType(file.getContentType());
        image.setName(file.getOriginalFilename());
        image.setImageName(savedImageObj.getImageName());
        image.setImageSize(savedImageObj.getImageSize());
        image.setModuleName(savedImageObj.getModuleName());
        return ResponseEntity.status(HttpStatus.OK).body(image);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getImageWithId(@PathVariable("id") Long id) {
        Image image = null;
        try {
            String dsUrl = env.getProperty("spring.datasource.url", "<not configured>");
            String[] activeProfiles = env.getActiveProfiles();
            String active = activeProfiles.length > 0 ? String.join(",", activeProfiles) : "<none>";
            logger.info("getImageWithId diagnostics: id={}, datasourceUrl={}, activeProfiles={}", id, dsUrl, active);

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
            Image image = imageService.getImage(id);
            if (image == null) return ResponseEntity.notFound().build();
            byte[] bytes = imageService.readImageBytes(image);
            if (bytes == null) return ResponseEntity.notFound().build();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(image.getType() != null ? image.getType() : "application/octet-stream"));
            headers.setContentLength(bytes.length);
            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Error streaming image data for id={}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /all - return all images from the database
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllImages() {
        try {
            List<Image> images = imageService.getAlI();
            if (images == null || images.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(images);
        } catch (Exception e) {
            logger.error("Error fetching all images", e);
            return new ResponseEntity<>(new CustomErrorType("Failed to fetch images"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
