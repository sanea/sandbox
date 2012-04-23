package my;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class DownloadController {

	private static enum Command {get, stop, status};
	
	private final static ExecutorService executor = Executors.newFixedThreadPool(10);
	private final static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

	private final static Map<String,Future<DownloadResponse>> futures = new ConcurrentHashMap<String,Future<DownloadResponse>>();
	
	public static void main(String[] args) throws Exception {

		String input = null;

		System.out.println("command url:");
		
		while((input = br.readLine()) != null) {
			try {
			String[] commandUrl = input.split(" ");
			
			if(commandUrl.length != 2) throw new Exception("Invalid command, expected smth like [get/stop/status http://google.com/]");
			
			Command command = Command.valueOf(commandUrl[0]);
			String url = commandUrl[1];
			
			Future<DownloadResponse> feature = null;

			switch (command) {
			case get:
				Callable<DownloadResponse> downloadResponse = new DownloadRequest(url);
				feature = executor.submit(downloadResponse);
				futures.put(url, feature);
				break;

			case stop:
				
				if(!futures.containsKey(url)) {
					System.out.println("Download of the resource with url " + url + " has not been sheduled");
					break;
				}
				
				feature = futures.get(url);
				feature.cancel(true);
				break;
				
			case status:
				
				if(!futures.containsKey(url)) {
					System.out.println("Download of the resource with url " + url + " has not been sheduled");
					break;
				}
				
				feature = futures.get(url);
				
				if(feature.isDone()) {
					DownloadResponse dr = null;
					try {
						dr = feature.get();
						System.out.println("Content of the resource " + url);
						System.out.println(dr);
					} catch (InterruptedException | CancellationException e) {
						System.out.println("Downloading of resource " +url + " has been canceled");
					}
				} else if (feature.isCancelled()) {
					System.out.println("Downloading of resource " +url + " has been canceled");
				} else {
					System.out.println("Downloading of resource " +url + " is in progress");
				}
				break;
				
			default:
				System.out.println("Unknown command");
				break;
			}
			
		} catch (IOException ioe) {
			System.out.println("IO error");
			System.exit(1);
		}
	}
	}
}
