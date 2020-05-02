package com.nunes.sam.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.nunes.sam.services.exceptions.FileException;

@Service
public class S3Service { // classe pra disponibilizar servicos relacionas ao bucket da amazon

	private Logger LOG = LoggerFactory.getLogger(S3Service.class);

	@Autowired
	private AmazonS3 s3client;

	@Value("${s3.bucket}")
	private String bucketName;

	// uri pra retornar o endereco web que foi gerado
	public URI uploadFile(MultipartFile multipartFile) { // esse eh o tipo correspondente a um arquivo q envia em
															// requisicao
		try {
			String fileName = multipartFile.getOriginalFilename();
			InputStream is = multipartFile.getInputStream();
			// elemento basico de leitura do java io a partir de uma origem
			String contentType = multipartFile.getContentType(); // informacao do tipo do arquivo enviado
			return this.uploadFile(is, fileName, contentType);
			
		} catch (IOException e) {
			throw new FileException("Erro de IO : "+e.getMessage());
		}
	}

	public URI uploadFile(InputStream is, String fileName, String contentType) {
		try {
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentType(contentType);

			LOG.info("Iniciando upload...");
			s3client.putObject(bucketName, fileName, is, meta);
			LOG.info("Uploado finalizado");

			return s3client.getUrl(bucketName, fileName).toURI();

		} catch (URISyntaxException e) {
			throw new FileException("Erro ao converter url para uri");
		}
	}
}
