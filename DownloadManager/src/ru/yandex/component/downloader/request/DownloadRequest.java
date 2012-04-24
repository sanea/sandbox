package ru.yandex.component.downloader.request;

import java.util.concurrent.Callable;

import ru.yandex.component.downloader.model.DownloadResponse;

public abstract class DownloadRequest  implements Callable<DownloadResponse> {

	public final String url;
	
	public DownloadRequest(final String _url) {
		url = _url;
	}
}
