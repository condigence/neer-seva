package com.condigence.neerseva.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.condigence.neerseva.util.AppProperties;
import com.condigence.neerseva.entity.Image;
import com.condigence.neerseva.services.ImageService;
import com.condigence.neerseva.util.CustomErrorType;
import com.condigence.neerseva.util.ImageUtil;


@RestController
@RequestMapping("/neerseva/api/image")
@CrossOrigin(origins = "*")
public class ImageController {
	
	
	@Autowired
	ImageService imageService;
	
	@Autowired
	public void setApp(AppProperties app) {
		this.app = app;
	}
	
	private AppProperties app;
	
	public static final Logger logger = LoggerFactory.getLogger(ImageController.class);

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@PostMapping("/upload")
	public ResponseEntity<?> uplaodImage(@RequestParam("myFile") MultipartFile file,
			@RequestParam("moduleName") String moduleName) throws Exception {
		String imagePath = app.getLocation();
		System.out.println(imagePath);
		String message = "";
		Image savedImageObj = null;
		Image image = new Image();
		image.setModuleName(moduleName);
		ImageUtil imgutil = new ImageUtil();
		byte[] _pic = imgutil.getImageWithFileName(file.getOriginalFilename(),imagePath);
		image.setPic(_pic);
		try {
			// save image into db and directory
			savedImageObj = imageService.store(file, moduleName,imagePath);

			message = "You successfully uploaded " + file.getOriginalFilename() + "!";
			//logger.info(message);
			_pic = imgutil.getImageWithFileName(file.getOriginalFilename(),imagePath);
		} catch (Exception e) {
			message = "FAIL to upload " + file.getOriginalFilename() + "!";
			logger.warn(message);
			throw new Exception(e);
		}
		// Now return back the saved image from directory
		if (null != _pic) {
			image.setPic(_pic);
			image.setId(savedImageObj.getId());
			image.setType(file.getContentType());
			image.setName(file.getOriginalFilename());
			image.setImageName(savedImageObj.getImageName());
			image.setImageSize(savedImageObj.getImageSize());
			image.setImagePath(savedImageObj.getImagePath());
			image.setModuleName(savedImageObj.getModuleName());
		} else {
			return new ResponseEntity(new CustomErrorType("Image Not Found || Uploaded image is not in correct format"),
					HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.status(HttpStatus.OK).body(image);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getImageWithId(@PathVariable("id") Long id) {
		Image image = null;
		try {
			String imagePath = app.getLocation();
			image = imageService.getImage(id,imagePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null == image) {
			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
		}
		
		return ResponseEntity.status(HttpStatus.OK).body(image);
	}


	@GetMapping("/name/{imageName}")
	public ResponseEntity<?> getImageWithName(@RequestParam("imageName") String imageName) {
		Image image = null;
		String imagePath = app.getLocation();
		try {
			image = imageService.getImageId(imageName,imagePath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (null == image) {
			return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.status(HttpStatus.OK).body(image);
	}
	
	
}
