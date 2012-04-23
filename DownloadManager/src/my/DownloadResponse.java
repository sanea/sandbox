package my;

public class DownloadResponse {

	private Status status = null;
	private String errorMessage = null;
	private String content = null;

	public void setStatus(Status started) {
		this.status = started;
	}

	public void setErrorMessage(String string) {
		this.errorMessage = string;
	}

	public void setContent(String string) {
		this.content = string;
	}

	@Override
	public String toString() {
		return "DownloadResponse [status=" + status + ((errorMessage != null) ? ", errorMessage="
				+ errorMessage : ", content=" + content)  + "]";
	}
}
