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
import java.util.List;
import java.util.Optional;

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
		Path destination = dir.resolve(fileName);
		Files.copy(file.getInputStream(), destination);

		Image image = new Image();
		image.setName(fileName);
		image.setType(file.getContentType());
		// imagePath removed: do not persist filesystem path in DB
		image.setImageSize(file.getSize());
		image.setImageName((fileName.length() >= 3 ? fileName.substring(0, 3) : fileName) + "_" + imgutil.getDateTimeFormatter());
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

	/**
	 * Read the image bytes from filesystem for the given image.
	 * The file is expected to be stored under rootLocation with the original file name (image.getName()).
	 */
	public byte[] readImageBytes(Image image) throws IOException {
		if (image == null || image.getName() == null) return null;
		Path p = this.rootLocation.resolve(image.getName());
		if (!Files.exists(p)) return null;
		return Files.readAllBytes(p);
	}

    public List<Image> getAlI() {
        return imageRepository.findAll();
    }

    public Page<ImageSummary> getImageSummaries(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Image> all = imageRepository.findAll(pageable).getContent();
        List<ImageSummary> summaries = all.stream()
                .map(img -> new ImageSummary(img.getId(), img.getName(), img.getImageName(), img.getImageSize(), img.getModuleName(), img.getType()))
                .toList();
        return new PageImpl<>(summaries, pageable, imageRepository.count());
    }
}
