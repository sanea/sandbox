package ru.yandex.component.downloader.impl;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import ru.yandex.component.downloader.DownloadController;
import ru.yandex.component.downloader.DownloadRequest;
import ru.yandex.component.downloader.model.DownloadResponse;
import ru.yandex.component.downloader.model.Status;

public class HTTPDownloadController implements DownloadController {

	private Future<DownloadResponse> future;
	private final DownloadRequest downloadRequest;
	private final ExecutorService executor;

	public HTTPDownloadController(ExecutorService _executor, DownloadRequest _downloadRequest) {
		this.executor = _executor;
		this.downloadRequest = _downloadRequest;
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
		try {
			if (future.isDone()) {
				if (getContent().getStatus() == Status.error) {
					return Status.error;
				}
				return Status.finished;
			} else {
				return Status.started;
			}
		} catch (CancellationException e) {
			return Status.cancelled;
		}
	}

	@Override
	public DownloadResponse getContent() {
		try {
			return future.get();
		} catch (InterruptedException | ExecutionException e) {
			DownloadResponse downlResp = new DownloadResponse();
			downlResp.setErrorMessage(e.toString());
			downlResp.setStatus(Status.error);
			return downlResp;
		}
	}

}
