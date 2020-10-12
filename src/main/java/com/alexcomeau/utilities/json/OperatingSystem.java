package com.alexcomeau.utilities.json;

public class OperatingSystem {
	
	private boolean active;
	private boolean downloaded;
	private boolean checkVersion;
	private String osName;
	private boolean isIsoDir;
	private String lastVersion;
	private String url;
	private String file;
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public void setDownloaded(boolean downloaded) {
		this.downloaded = downloaded;
	}
	
	public void setCheckVersion(boolean checkVersion) {
		this.checkVersion = checkVersion;
	}
	
	public void setOsName(String osName) {
		this.osName = osName;
	}
	
	public void setIsIsoDir(boolean isIsoDir) {
		this.isIsoDir = isIsoDir;
	}
	
	public void setLastVersion(String lastVersion) {
		this.lastVersion = lastVersion;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	public void setFile(String file) {
		this.file = file;
	}
	
	
	public boolean getActive() {
		return this.active;
	}
	
	public boolean getDownloaded() {
		return this.downloaded;
	}
	
	public boolean getCheckVersion() {
		return this.checkVersion;
	}
	
	public String getOsName() {
		return this.osName;
	}
	
	public boolean getIsIsoDir() {
		return this.isIsoDir;
	}
	
	public String getLastVersion() {
		return this.lastVersion;
	}
	
	
	public String getUrl() {
		return this.url;
	}
	
	public String getFile() {
		return this.file;
	}
}
