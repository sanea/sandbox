package ru.yandex.component.downloader.impl;

import ru.yandex.component.downloader.DownloadResponse;
import ru.yandex.component.downloader.Status;

public class HTTPDownloadResponse implements DownloadResponse {
	private Status status;
	private String errorMessage;
	private String content;

	public void setStatus(Status status) {
		this.status = status;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public Status getStatus() {
		return status;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

	@Override
	public String getContent() {
		return content;
	}

	@Override
	public String toString() {
		return "HTTPDownloadResponse [status=" + status + ", errorMessage=" + errorMessage + ", content=" + content + "]";
	}

}
