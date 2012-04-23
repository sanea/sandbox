package ru.yandex.component.downloader.impl;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import ru.yandex.component.downloader.DownloadController;
import ru.yandex.component.downloader.DownloadRequest;
import ru.yandex.component.downloader.DownloadResponse;
import ru.yandex.component.downloader.Status;

public class HTTPDownloadController implements DownloadController {

	private Future<DownloadResponse> future;
	private DownloadRequest downloadRequest;
	private ExecutorService executor;

	@Override
	public void setDownloadRequest(DownloadRequest downloadRequest) {
		this.downloadRequest = downloadRequest;
	}

	@Override
	public void setExecutorService(ExecutorService executor) {
		this.executor = executor;
	}

	@Override
	public void startDownload() {
		future = executor.submit(downloadRequest);
	}

	@Override
	public void stopDownload() {
		future.cancel(true);
	}

	@Override
	public Status getStatus() {
		if (future.isDone()) {
			if (getContent().getStatus() == Status.error) {
				return Status.error;
			} else if (future.isCancelled()) {
				return Status.cancelled;
			} else {
				return Status.finished;
			}
		} else {
			return Status.started;
		}
	}

	@Override
	public DownloadResponse getContent() {
		try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			HTTPDownloadResponse downlResp = new HTTPDownloadResponse();
			downlResp.setErrorMessage(e.toString());
			downlResp.setStatus(Status.error);
			return downlResp;
		}
	}

}
