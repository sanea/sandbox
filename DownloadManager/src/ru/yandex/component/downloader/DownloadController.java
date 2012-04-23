package ru.yandex.component.downloader;

import java.util.concurrent.ExecutorService;

public interface DownloadController {
	public void setDownloadRequest(DownloadRequest downloadRequest);

	public void setExecutorService(ExecutorService executor);

	public void startDownload();

	public void stopDownload();

	public Status getStatus();

	public DownloadResponse getContent();
}
