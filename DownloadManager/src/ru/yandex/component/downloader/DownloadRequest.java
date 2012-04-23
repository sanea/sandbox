package ru.yandex.component.downloader;

import java.util.Properties;
import java.util.concurrent.Callable;

public interface DownloadRequest extends Callable<DownloadResponse> {

	public void setProperties(Properties props);

}
