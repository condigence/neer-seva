package com.condigence.imageservice.service;

import com.condigence.imageservice.dto.ImageSummary;
import com.condigence.imageservice.entity.Image;
import com.condigence.imageservice.repository.ImageRepository;

import com.condigence.imageservice.util.AppProperties;
import com.condigence.imageservice.util.ImageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ImageService {

    @Autowired
    ImageRepository imageRepository; // repository expected when prod profile active

    private final Path rootLocation;

    @Autowired
    public ImageService(AppProperties properties) {
        String configured = properties.getResolvedLocation();
        this.rootLocation = Paths.get(configured);
        // Ensure directory exists
        try {
            Files.createDirectories(this.rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize storage location: " + this.rootLocation, e);
        }
        logger.info("ImageService rootLocation={}", this.rootLocation);
    }

    public static final Logger logger = LoggerFactory.getLogger(ImageService.class);


    public Image store(MultipartFile file, String moduleName, String imgPath) throws IOException {
        ImageUtil imgutil = new ImageUtil();
        // Ensure path exists (imgPath could be from AppProperties.getLocation())
        Path dir = this.rootLocation;
        if (imgPath != null && !imgPath.isBlank()) {
            dir = Paths.get(imgPath.replace('\\', '/'));
        }
        Files.createDirectories(dir);

        String originalFilename = file.getOriginalFilename();
        String fileName = StringUtils.cleanPath((originalFilename == null) ? "unknown" : originalFilename);

        // Create a unique stored filename to avoid collisions on disk
        String baseUnique = (fileName.length() >= 3 ? fileName.substring(0, 3) : fileName) + "_" + imgutil.getDateTimeFormatter();
        // keep extension if present
        String ext = "";
        int dot = fileName.lastIndexOf('.');
        if (dot >= 0) {
            ext = fileName.substring(dot);
        }
        String storedFileName = baseUnique + "_" + UUID.randomUUID().toString().replaceAll("-", "") + ext;

        // Sanitize filename: allow alphanumeric, dot, underscore, hyphen; replace others with '_'
        storedFileName = storedFileName.replaceAll("[^A-Za-z0-9._-]", "_");

        Path destination = dir.resolve(storedFileName);
        // Copy and replace if something unexpected exists with same name
        Files.copy(file.getInputStream(), destination, StandardCopyOption.REPLACE_EXISTING);

        Image image = new Image();
        // Store the actual stored filename in the DB 'name' column so reads use this filename
        image.setName(storedFileName);
        image.setType(file.getContentType());
        // imagePath removed: do not persist filesystem path in DB
        image.setImageSize(file.getSize());
        image.setImageName(baseUnique);
        image.setModuleName(moduleName);

        Image savedImage = imageRepository.save(image);
        logger.info("Saved image metadata id={}", savedImage.getId());
        return savedImage;
    }

    public Image getImage(Long id) {
        return imageRepository.findByImageId(id).orElse(null);
    }

    public Optional<Image> getImageByName(String name) {
        return imageRepository.getByName(name);
    }

    public byte[] readImageBytesByPath(String imageName, String baseDirRaw) throws IOException {
        if (imageName == null || imageName.isBlank()) return null;
        Path imagesBase = (baseDirRaw == null || baseDirRaw.isBlank()) ? this.rootLocation : Paths.get(baseDirRaw.replace('/', java.io.File.separatorChar));
        Path imgPath = imagesBase.resolve(java.nio.file.Paths.get(imageName).getFileName().toString()).normalize();
        if (!Files.exists(imgPath) || !imgPath.startsWith(imagesBase)) {
            return null;
        }
        return Files.readAllBytes(imgPath);
    }

    public Page<ImageSummary> getImageSummaries(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Image> all = imageRepository.findAll(pageable).getContent();
        List<ImageSummary> summaries = all.stream()
                .map(img -> new ImageSummary(img.getId(), img.getName(), img.getImageName(), img.getImageSize(), img.getModuleName(), img.getType()))
                .collect(Collectors.toList());
        return new PageImpl<>(summaries, pageable, imageRepository.count());
    }

    // Helper to build ImageDTO from Image and optionally bytes
    public com.condigence.imageservice.dto.ImageDTO toDto(Image image, byte[] bytes) {
        com.condigence.imageservice.dto.ImageDTO dto = new com.condigence.imageservice.dto.ImageDTO();
        if (image == null) return dto;
        dto.setId(image.getId());
        dto.setName(image.getName());
        dto.setImageName(image.getImageName());
        dto.setImageSize(image.getImageSize());
        dto.setModuleName(image.getModuleName());
        dto.setType(image.getType());
        if (bytes != null && bytes.length > 0) dto.setPic(java.util.Base64.getEncoder().encodeToString(bytes));
        else dto.setPic("");
        return dto;
    }

}
