package ru.yandex.component.downloader;

import ru.yandex.component.downloader.model.DownloadResponse;
import ru.yandex.component.downloader.model.Status;

public interface DownloadController {
	
	public void startDownload();

	public void stopDownload();

	public Status getStatus();

	public DownloadResponse getContent();
}
