package ru.yandex.component.downloader;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.yandex.component.downloader.impl.HTTPDownloadController;
import ru.yandex.component.downloader.impl.HTTPDownloadRequest;

public class DownloadManager {
	private final ExecutorService executor = Executors.newFixedThreadPool(10);

	private Map<String, DownloadController> downloadControllerMap = new HashMap<String, DownloadController>();

	public void startHTTPDownload(String urlString) {
		DownloadRequest downloadRequest = new HTTPDownloadRequest();
		Properties prop = new Properties();
		prop.setProperty(HTTPDownloadRequest.URL_PROPERTY, urlString);
		downloadRequest.setProperties(prop);
		DownloadController downloadController = new HTTPDownloadController();
		downloadController.setDownloadRequest(downloadRequest);
		downloadController.setExecutorService(executor);
		downloadControllerMap.put(urlString, downloadController);
		downloadController.startDownload();
	}

	public Map<String, DownloadController> getDownloadControllerMap() {
		return downloadControllerMap;
	}

	public void closeConnection() {
		executor.shutdownNow();
	}

	// test for HTTP
	public static void main(String[] args) throws Exception {

		DownloadManager downloadManager = new DownloadManager();

		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String input = null;

		System.out.println("command url:");

		while ((input = br.readLine()) != null) {
			try {

				if (input.equalsIgnoreCase("exit")) {
					downloadManager.closeConnection();
					System.exit(0);
				}

				String[] command = input.split(" ");

				if (command.length != 2) {
					throw new Exception("Invalid command, expected smth like [get/stop/status http://google.com/] or exit");
				}

				String url = command[1];

				if (command[0].equalsIgnoreCase("get")) {

					downloadManager.startHTTPDownload(url);

					// System.out.println("get " + url);
				} else if (command[0].equalsIgnoreCase("stop")) {

					downloadManager.getDownloadControllerMap().get(url).stopDownload();

					// System.out.println("stop " + url);
				} else if (command[0].equalsIgnoreCase("status")) {

					Status status = downloadManager.getDownloadControllerMap().get(url).getStatus();
					System.out.println(status);

					// System.out.println("status " + url);
				} else if (command[0].equalsIgnoreCase("content")) {

					DownloadResponse dr = downloadManager.getDownloadControllerMap().get(url).getContent();
					System.out.println(dr);

					// System.out.println("content " + url);
				} else if (command[0].equalsIgnoreCase("list")) {
					System.out.println(downloadManager.getDownloadControllerMap().entrySet());
					
				} else {
					System.out.println("Unknown command");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}