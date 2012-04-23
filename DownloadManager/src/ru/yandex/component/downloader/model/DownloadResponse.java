package ru.yandex.component.downloader.model;

public class DownloadResponse {
	
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

	public Status getStatus() {
		return status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getContent() {
		return content;
	}

	public String toString() {
		return "HTTPDownloadResponse [status=" + status + ", errorMessage=" + errorMessage + ", content=" + content + "]";
	}
}
