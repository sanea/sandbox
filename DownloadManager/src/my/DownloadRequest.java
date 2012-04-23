package my;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.Callable;

public class DownloadRequest implements Callable<DownloadResponse> {

	private final String urlString;
	
	public DownloadRequest(String _urlString) {
		urlString = _urlString;
	}
	
	@Override
	public DownloadResponse call() {
		
		StringBuilder sb = new StringBuilder();
		DownloadResponse dr = new DownloadResponse();
		
		try {
			URL oracle = new URL(urlString);
			URLConnection yc = oracle.openConnection();
			dr.setStatus(Status.started);
			BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
			
			String inputLine;
			
			while ((inputLine = in.readLine()) != null) {
				sb.append(inputLine);
				Thread.sleep(1000);
			}
			in.close();
			dr.setContent(sb.toString());
		} catch (IOException | InterruptedException e) {
			dr.setErrorMessage(e.getMessage());
		} finally {
			dr.setStatus(Status.done);
		}
		
		return dr;
	}
}
