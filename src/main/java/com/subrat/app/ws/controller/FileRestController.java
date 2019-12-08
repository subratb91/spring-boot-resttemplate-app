package com.subrat.app.ws.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.subrat.app.ws.model.UploadFileResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("files")
public class FileRestController {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${rest.endpoint.file.app.base.url}")
	private String fileRestEndpointBaseUrl;

	@PostMapping(value = "uploadFile", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<UploadFileResponse> uploadFile(@RequestParam("file") MultipartFile file) {
		log.info("--------Entering - uploadFile------");
		String url = fileRestEndpointBaseUrl + "/uploadFile";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("file", file.getResource());
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

		ResponseEntity<UploadFileResponse> responseEntity = restTemplate.postForEntity(url, requestEntity,
				UploadFileResponse.class);
		log.info("--------Exiting - uploadFile------");
		return responseEntity;
	}

	@PostMapping(value = "uploadFile/exchange", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<UploadFileResponse> uploadFileUsingExchange(@RequestParam("file") MultipartFile file) {
		log.info("--------Entering - uploadFileUsingExchange------");

		String url = fileRestEndpointBaseUrl + "/uploadFile";
		log.info("--------url------" + url);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("file", file.getResource());

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

		ResponseEntity<UploadFileResponse> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
				UploadFileResponse.class);

		log.info("--------Exiting - uploadFileUsingExchange------");
		return responseEntity;
	}

	@PostMapping(value = "uploadMultipleFiles", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<List<UploadFileResponse>> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
		log.info("--------Entering - uploadMultipleFiles------");
		String url = fileRestEndpointBaseUrl + "/uploadMultipleFiles";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		for (MultipartFile file : files) {
			body.add("files", file.getResource());
		}

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

		ResponseEntity<List<UploadFileResponse>> responseEntity = restTemplate.exchange(url, HttpMethod.POST,
				requestEntity, new ParameterizedTypeReference<List<UploadFileResponse>>() {
				});
		log.info("--------Exiting - uploadMultipleFiles------");
		return responseEntity;
	}

	@GetMapping(value = "downloadFile/{fileId}")
	public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
		log.info("--------Entering - downloadFile------");
		String url = fileRestEndpointBaseUrl + "/downloadFile/" + fileId;

		ResponseEntity<Resource> responseEntity = restTemplate.getForEntity(url, Resource.class);

		log.info("--------Exiting - downloadFile------");
		return responseEntity;
	}

	@GetMapping(value = "downloadFile/exchange/{fileId}")
	public ResponseEntity<Resource> downloadFileUsingExchange(@PathVariable Long fileId) {
		log.info("--------Entering - downloadFileUsingExchange------");
		String url = fileRestEndpointBaseUrl + "/downloadFile/" + fileId;

		ResponseEntity<Resource> responseEntity = restTemplate.exchange(url, HttpMethod.GET, null, Resource.class);

		log.info("--------Exiting - downloadFileUsingExchange------");
		return responseEntity;
	}

	@PutMapping(value = "updateFile", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
	public ResponseEntity<UploadFileResponse> updateFile(@RequestParam("file") MultipartFile file,
			@RequestParam("fileId") Long fileId) {
		log.info("--------Entering - updateFile------");

		String url = fileRestEndpointBaseUrl + "/updateFile";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);

		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("file", file.getResource());
		body.add("fileId", fileId);
		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

		ResponseEntity<UploadFileResponse> responseEntity = restTemplate.exchange(url, HttpMethod.PUT, requestEntity,
				UploadFileResponse.class);

		log.info("--------Exiting - updateFile------");
		return responseEntity;
	}
	
	@DeleteMapping(value = "deleteFile/{fileId}")
	public void deleteFile(@PathVariable("fileId") Long fileId) {
		log.info("--------Entering - deleteFile------");

		String url = fileRestEndpointBaseUrl + "/deleteFile/"+ fileId;
		
		restTemplate.delete(url);

		log.info("--------Exiting - deleteFile------");
	}

}
