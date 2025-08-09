package com.valentin.reservacion_citas.domain.service.implementation;

import com.valentin.reservacion_citas.domain.service.CloudStorageService;
import com.valentin.reservacion_citas.web.exception.CloudStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.time.Duration;

@Service
public class S3StorageServiceImpl implements CloudStorageService {

	@Value("${aws.bucket-name}")
	private String bucketName;

	private final S3Client s3Client;
	private final S3Presigner s3Presigner;

	private static final Logger logger = LoggerFactory.getLogger(S3StorageServiceImpl.class);

	@Autowired
	public S3StorageServiceImpl(S3Client s3Client, S3Presigner s3Presigner) {
		this.s3Client = s3Client;
		this.s3Presigner = s3Presigner;
	}

	@Override
	public void uploadImage(byte[] file, String filename) {
		if (file == null) {
			throw new IllegalArgumentException("El archivo esta vacío");
		}

		try {
			// PutObjectRequest que contiene el nombre del bucket y el identificador (nombre)
			// que va a tener el archivo en el bucket
			PutObjectRequest putObject = PutObjectRequest.builder()
														 .bucket(bucketName)
														 .key(filename)
														 .build();

			s3Client.putObject(putObject, RequestBody.fromBytes(file));

			logger.info("Archivo {} subido exitosamente", filename);

		} catch (S3Exception e) {
			logger.error("Error de S3 al subir archivo {}: {}", filename, e.getMessage(), e);
			throw new CloudStorageException("Error al subir el archivo a AWS S3: " + e.getMessage(), e);
		} catch (SdkException e) {
			logger.error("Error del SDK de AWS al subir archivo {}: {}", filename, e.getMessage(), e);
			throw new CloudStorageException("Error de comunicacion con aws: " + e.getMessage(), e);
		}
	}

	@Override
	public String getImagePresignedUrl(String filename) {

		if (filename == null || filename.isEmpty()) {
			throw new IllegalArgumentException("Nombre de archivo no proporcionado");
		}

		try {
			if(!objectExists(filename)){
				logger.error("El archivo {} no existe en S3", filename);
				throw new CloudStorageException("El archivo no existe en S3");
			}

			// Obtener objeto (archivo) del bucket
			GetObjectRequest objectRequest = GetObjectRequest.builder()
															 .bucket(bucketName)
															 .key(filename)
															 .build();

			// Configurar objeto con firma solicitado
			GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
																			.signatureDuration(Duration.ofHours(1))
																			.getObjectRequest(objectRequest)
																			.build();

			// Obtener objeto firmado
			PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);

			String urlImage = presignedRequest.url().toString();

			logger.info("Url firmado del archivo {} obtenido con exito: {}", filename, urlImage);

			return urlImage;
		} catch (S3Exception e) {
			String errorMessage = String.format("Error de S3 al obtener la url firmada para el archivo %s: %s", filename, e.getMessage());
			logger.error(errorMessage, e);

			throw new CloudStorageException("Error al obtener la url firmada de AWS S3: " + e.getMessage(), e);
		} catch (SdkException e) {
			String errorMessage = String.format("Error del SDK de AWS al obtener la url firmada para el archivo %s: %s", filename, e.getMessage());
			logger.error(errorMessage, e);

			throw new CloudStorageException("Error de comunicación con AWS: " + e.getMessage(), e);
		}
	}

	@Override
	public void deleteImage(String filename) {

		if (filename == null || filename.isEmpty()) {
			throw new IllegalArgumentException("Nombre de archivo no proporcionado");
		}

		try {
			DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
																		 .bucket(bucketName)
																		 .key(filename)
																		 .build();

			s3Client.deleteObject(deleteObjectRequest);

			logger.info("Archivo {} eliminado exitosamente en S3", filename);

		} catch (S3Exception e) {
			String errorMessage = String.format("Error en S3 al eliminar el archivo %s: %s", filename, e.getMessage());
			logger.error(errorMessage, e);

			throw new CloudStorageException("Error al eliminar el archivo en AWS S3: " + e.getMessage(), e);
		} catch (SdkException e) {
			String errorMessage = String.format("Error del SDK de AWS al eliminar el archivo %s: %s", filename, e.getMessage());
			logger.error(errorMessage, e);

			throw new CloudStorageException("Error en la comunicacion con AWS: " + e.getMessage(), e);
		}
	}

	private boolean objectExists(String filename) {
		try {
			HeadObjectRequest headObjectRequest = HeadObjectRequest.builder()
																   .bucket(bucketName)
																   .key(filename)
																   .build();

			s3Client.headObject(headObjectRequest);
			return true;
		} catch (S3Exception e) {
			if(e.statusCode() == 404){
				return false;
			}
			throw e;
		}
	}
}
