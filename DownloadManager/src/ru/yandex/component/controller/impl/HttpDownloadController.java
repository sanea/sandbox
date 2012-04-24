package ru.yandex.component.controller.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import ru.yandex.component.controller.DownloadController;
import ru.yandex.component.downloader.model.DownloadResponse;
import ru.yandex.component.downloader.model.Status;
import ru.yandex.component.downloader.request.DownloadRequest;

public class HttpDownloadController implements DownloadController {

	private final static ExecutorService executor = Executors.newFixedThreadPool(10);
	private final static Map<String, Future<DownloadResponse>> downloads = new HashMap<String, Future<DownloadResponse>>();
	
	@Override
	public boolean startDownload(DownloadRequest downloadRequest) {
		
		if(downloadRequest == null || downloadRequest.url == null) return false;
		
		Future<DownloadResponse> future = executor.submit(downloadRequest);
		downloads.put(downloadRequest.url, future);
		
		return true;
	}
	
	@Override
	public boolean stopDownload(String url) {
		
		Future<DownloadResponse> future = downloads.get(url);
		
		if(future == null) return false;
		
		return future.cancel(true);
	}

	@Override
	public DownloadResponse getResponse(String url) {
		
		DownloadResponse response = new DownloadResponse();
		
		try {
			
			Future<DownloadResponse> future = downloads.get(url);
			
			if(future == null) {
				response.setStatus(Status.error);
				response.setErrorMessage("Download not found");
				return response;
			}
			
			if (future.isDone()) {
				return future.get();
			}
			
		} catch (CancellationException e) {
			response.setStatus(Status.cancelled);
		} catch (InterruptedException e) {
			response.setStatus(Status.error);
			response.setErrorMessage(e.getMessage());
		} catch (ExecutionException e) {
			response.setStatus(Status.error);
			response.setErrorMessage(e.getMessage());
		}
		
		return response;
	}

	@Override
	public void shutdownNow() {
		executor.shutdownNow();
	}
}
