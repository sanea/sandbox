package ru.yandex.component.downloader.request.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import ru.yandex.component.downloader.model.DownloadResponse;
import ru.yandex.component.downloader.model.Status;
import ru.yandex.component.downloader.request.DownloadRequest;

public class HttpDownloadRequest extends DownloadRequest {

	public HttpDownloadRequest(String props) {
		super(props);
	}

	@Override
	public DownloadResponse call() throws Exception {
		StringBuilder sb = new StringBuilder();
		DownloadResponse downloadResponse = new DownloadResponse();
		try {
			URL urlObject = new URL(url);
			URLConnection uc = urlObject.openConnection();
			downloadResponse.setStatus(Status.started);
			BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));

			String inputLine = null;

			while ((inputLine = in.readLine()) != null) {
				sb.append(inputLine);
				Thread.sleep(10000);
			}
			in.close();
			downloadResponse.setContent(sb.toString());
			downloadResponse.setStatus(Status.finished);
		} catch (IOException | InterruptedException e) {
			downloadResponse.setErrorMessage(e.getMessage());
			downloadResponse.setStatus(Status.error);
		}
		return downloadResponse;
	}
}
