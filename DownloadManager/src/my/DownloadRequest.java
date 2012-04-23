package my;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.concurrent.Callable;

public class DownloadRequest implements Callable<String> {

	private static enum Status {not_started, started, done};
	
	private final String urlString;
	private final Map<String,String> status;
	
	public DownloadRequest(String _urlString, Map<String,String> _status) {
		status = _status;
		urlString = _urlString;
	}
	
	@Override
	public String call() throws Exception {
		URL oracle = new URL(urlString);
		URLConnection yc = oracle.openConnection();
		status.put(urlString, Status.started.toString());
		BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
		String inputLine;
		StringBuilder sb = new StringBuilder();
		while ((inputLine = in.readLine()) != null) {
			sb.append(inputLine);
			Thread.sleep(1000);
		}
		in.close();
		status.put(urlString, Status.done.toString());
		return sb.toString();
	}
}
