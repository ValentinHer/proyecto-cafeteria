package com.valentin.reservacion_citas.domain.service;

import org.springframework.web.multipart.MultipartFile;

public interface CloudStorageService {
	void uploadImage(byte[] file, String filename);
	String getImagePresignedUrl(String filename);
	void deleteImage(String filename);
}
