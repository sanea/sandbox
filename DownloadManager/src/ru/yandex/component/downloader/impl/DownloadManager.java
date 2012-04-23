package ru.yandex.component.downloader.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ru.yandex.component.downloader.DownloadController;
import ru.yandex.component.downloader.DownloadRequest;
import ru.yandex.component.downloader.model.DownloadResponse;
import ru.yandex.component.downloader.model.Status;

public class DownloadManager {
	
	private final ExecutorService executor = Executors.newFixedThreadPool(10);

	private Map<String, DownloadController> downloadControllerMap = new HashMap<String, DownloadController>();

	public void startHTTPDownload(String urlString) {
		
		Properties prop = new Properties();
		prop.setProperty(DownloadRequest.URL_PROPERTY, urlString);
		
		DownloadRequest downloadRequest = new DownloadRequest(prop);
		
		DownloadController downloadController = new HTTPDownloadController(executor, downloadRequest);
		downloadControllerMap.put(urlString, downloadController);
		downloadController.startDownload();
	}

	public DownloadController getController(String url) {
		return downloadControllerMap.get(url);
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

					downloadManager.getController(url).stopDownload();

					// System.out.println("stop " + url);
				} else if (command[0].equalsIgnoreCase("status")) {

					Status status = downloadManager.getController(url).getStatus();
					System.out.println(status);

					// System.out.println("status " + url);
				} else if (command[0].equalsIgnoreCase("content")) {

					DownloadResponse dr = downloadManager.getController(url).getContent();
					System.out.println(dr);

				} else {
					System.out.println("Unknown command");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}