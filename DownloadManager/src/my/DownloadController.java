package my;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadController {

	private static enum Command {get, stop, status};
	
	public static void main(String[] args) throws Exception {

		ExecutorService executor = Executors.newFixedThreadPool(10);
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		Map<String,String> status = new ConcurrentHashMap<String,String>();
		String input = null;

		System.out.println("command url:");
		
		while((input = br.readLine()) != null) {
			try {
			String[] commandUrl = input.split(" ");
			
			if(commandUrl.length != 2) throw new Exception("Invalid command, expected smth like [get/stop/status http://google.com/]");
			
			Command command = Command.valueOf(commandUrl[0]);
			String url = commandUrl[1];
			
			switch (command) {
			case get:
				Callable<String> dr = new DownloadRequest(url, status);
				executor.submit(dr);
				break;

			case stop:
				break;
			
			case status:
				System.out.println("Current status: " + status.get(url));
			default:
				break;
			}
			
		} catch (IOException ioe) {
			System.out.println("IO error trying to read your name!");
			System.exit(1);
		}
	}
	}
}
