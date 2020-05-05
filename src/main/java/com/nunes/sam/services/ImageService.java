package com.nunes.sam.services;

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

import com.nunes.sam.services.exceptions.FileException;

@Service
public class ImageService {
	
	public BufferedImage getJpgImageFromFile(MultipartFile multipartFile) { //buffered image eh um tipo de imagem do java em jpg (?)
		String ext = FilenameUtils.getExtension(multipartFile.getOriginalFilename()); //fileNameUtils eh pra extrair o nome e a extensao do arquivo facilmente
		
		if(!"png".equals(ext) && !"jpg".equals(ext)) {
			throw new FileException("Somente imagens PNG e JPG são permitidas");
		}
		
		try {
			BufferedImage img = ImageIO.read(multipartFile.getInputStream());
			
			if("png".equals(ext)) {
				img = pngToJpg(img);
			}
			
			return img;
			
		} catch (IOException e) {
			throw new FileException("Erro ao ler arquivo");
		}
	}

	public BufferedImage pngToJpg(BufferedImage img) {
		
		BufferedImage jpgImage = new BufferedImage(img.getWidth(),img.getHeight(),BufferedImage.TYPE_INT_RGB);
		jpgImage.createGraphics().drawImage(img, 0, 0, Color.WHITE, null); //o color.white eh pq alguns pngs tem fundo transparente e nesse caso preenche com fundo branco
		
		return null;
	}
	
	public InputStream getInputStream(BufferedImage img, String extension) {
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(img, extension, os);
			return new ByteArrayInputStream(os.toByteArray());
		}catch(IOException e) {
			throw new FileException("Erro ao ler arquivo");
		}
	}

}
