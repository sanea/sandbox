package ru.yandex.component.controller;

import ru.yandex.component.downloader.model.DownloadResponse;
import ru.yandex.component.downloader.request.DownloadRequest;

public interface DownloadController {
	
	public boolean stopDownload(String url);

	public boolean startDownload(DownloadRequest downloadRequest);

	public void shutdownNow();

	public DownloadResponse getResponse(String url);
}
