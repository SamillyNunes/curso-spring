package com.nunes.sam.services;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class S3Service { //classe pra disponibilizar servicos relacionas ao bucket da amazon 
	
	private Logger LOG = LoggerFactory.getLogger(S3Service.class);
	
	@Autowired
	private AmazonS3 s3client;
	
	@Value("${s3.bucket}")
	private String bucketName;
	
	 
	public void uploadFile(String localFilePath) {
		
		try {
			File file = new File(localFilePath);
			LOG.info("Iniciando upload...");
			s3client.putObject(new PutObjectRequest(bucketName, "teste.jpg", file));
			LOG.info("Uploado finalizado");
		} 
		catch(AmazonServiceException e) {// usando essa excecao para o caso de usuario errado, etc
			LOG.info("AmazonServiceException: "+e.getErrorMessage());
			LOG.info("Status code: "+e.getErrorCode());
		}
		catch(AmazonClientException e) {// usando essa excecao para o caso de usuario errado, etc
			LOG.info("AmazonClientException: "+e.getMessage());
		}
	}
}
