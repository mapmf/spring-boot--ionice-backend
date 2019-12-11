package com.marcos.cursomc.services;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.marcos.cursomc.services.exceptions.FileException;

@Service
public class ImageService {

	public BufferedImage getJpgImageFormat(MultipartFile multipartFile) {

		try {
			String ext = FilenameUtils.getExtension(multipartFile.getOriginalFilename());

			if (!ext.equals("png") && !ext.equals("jpg")) {
				throw new FileException("Somente imagens PNG e JPG são permitidas");
			}

			BufferedImage img = ImageIO.read(multipartFile.getInputStream());

			if (ext.equals("png")) {
				img = pngToJpg(img);
			}

			return img;

		} catch (IOException e) {
			throw new FileException("Erro ao ler arquivo");
		}
	}

	public BufferedImage pngToJpg(BufferedImage img) {

		BufferedImage jpgImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

		jpgImage.createGraphics().drawImage(img, 0, 0, Color.WHITE, null);

		return jpgImage;
	}

	public InputStream getInputStream(BufferedImage bufferedImage, String extension) {

		try {
			
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(bufferedImage, extension, os);

			return new ByteArrayInputStream(os.toByteArray());

		} catch (IOException e) {
			throw new FileException("Erro ao ler o arquivo");
		}
	}
}
