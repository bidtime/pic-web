package org.bidtime.pic.bean;

public class MovePicBean {

  String[] archvUrl;

  String subDir;

  Boolean hashStore;

  public String[] getArchvUrl() {
    return archvUrl;
  }

  public void setArchvUrl(String[] archvUrl) {
    this.archvUrl = archvUrl;
  }

  public String getSubDir() {
    return subDir;
  }

  public void setSubDir(String subDir) {
    this.subDir = subDir;
  }

  public Boolean getHashStore() {
    return hashStore;
  }

  public void setHashStore(Boolean hashStore) {
    this.hashStore = hashStore;
  }

}
