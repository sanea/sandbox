package ru.yandex.component.downloader;

public interface DownloadResponse {
	public Status getStatus();

	public String getErrorMessage();

	public String getContent();
}
