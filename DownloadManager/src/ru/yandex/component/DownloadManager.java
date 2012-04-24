package ru.yandex.component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import ru.yandex.component.controller.impl.HttpDownloadController;
import ru.yandex.component.downloader.DownloadController;
import ru.yandex.component.downloader.model.DownloadResponse;
import ru.yandex.component.downloader.model.Protocol;
import ru.yandex.component.downloader.request.impl.HttpDownloadRequest;

public class DownloadManager {
	
	private final static Map<Protocol, DownloadController> controllers = new HashMap<Protocol, DownloadController>();
	
	public DownloadManager() {
		controllers.put(Protocol.http, new HttpDownloadController());
	}
	
	public Protocol getUrlProtocol(String urlString) {
		
		String[] protocolAdress = urlString.split("://");
		
		if(protocolAdress.length != 2) {
			System.out.println("Invalid resource name, expected [http/ftp]://[resource name]");
			return null;
		}
		
		return Protocol.valueOf(protocolAdress[0]);
	}
	
	public void startDownload(String urlString) {
		
		Protocol protocol = getUrlProtocol(urlString);
		
		if(protocol == null) {
			System.out.println("Invalid protocol, expected http or ftp");
			return;
		}
		
		switch (protocol) {
		case http:
			DownloadController downloadController = controllers.get(protocol);
			downloadController.startDownload(new HttpDownloadRequest(urlString));
			break;

		case ftp:
			System.out.println("Ftp protocol not implemented yet");

		default:
			System.out.println("Unknown protocol");
			break;
		}
	}

	private DownloadController getControllerByURL(final String url) {
		
		Protocol protocol = getUrlProtocol(url);
		
		if(protocol == null) return null;
		
		return controllers.get(protocol);
	}
	
	private boolean stopDownload(String url) {
		
		DownloadController controller = getControllerByURL(url);
		
		if(controller == null) return false;
		
		return controller.stopDownload(url);
	}
	
	private DownloadResponse getResponse(String url) {
		
		DownloadController controller = getControllerByURL(url);
		
		if(controller == null) return null;
		
		return controller.getResponse(url);
	}
	
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

					downloadManager.startDownload(url);
					System.out.println("started [" + url + "]");
					
				} else if (command[0].equalsIgnoreCase("stop")) {

					downloadManager.stopDownload(url);
					System.out.println("started [" + url + "]");
					
				} else if (command[0].equalsIgnoreCase("status")) {

					DownloadResponse dr = downloadManager.getResponse(url);
					System.out.println(dr.getStatus());
					
				} else if (command[0].equalsIgnoreCase("content")) {

					DownloadResponse dr = downloadManager.getResponse(url);
					System.out.println(dr);
					
				} else {
					System.out.println("Unknown command");
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void closeConnection() {
		controllers.get(Protocol.http).shutdownNow();
	}
}