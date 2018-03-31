package com;

import org.bidtime.pic.fdfs.UploadFdfs;

/**
 * client test
 *
 * @author Happy Fish / YuQing
 * @version Version 1.18
 */
public class TestClient4 {

  /**
   * entry point
   *
   * @param args comand arguments
   *             <ul><li>args[0]: config filename</li></ul>
   *             <ul><li>args[1]: local filename to upload</li></ul>
   */
  public static void main(String args[]) {
    String local_filename = "/root/1.txt";
    String url = UploadFdfs.upload(local_filename);
    System.out.println("fullPath:　"+ url);
  }
  
}