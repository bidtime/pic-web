package org.bidtime.pic.utils;

import java.io.File;

public class FileMv {

  public static boolean moveFile(File s, File t) {
    try {
      return s.renameTo(t);
    } catch (Exception e) {
      //logger.error("moveFile", e);
      return false;
    }
  }
	
	public static boolean moveFile(String oldFile, String newFile) {
		// mkdirs(newFile);
		return moveFile(new File(oldFile), new File(newFile));		
	}
	
	private static String getAbsolutePath(String file) {
		int pos = file.lastIndexOf('/');
		if (pos>=0) {
			return file.substring(0, pos);
		} else {
			return file;
		}
	}
	
	public static boolean moveFile(String oldFile, String newFile, boolean mkDirs) {
		if ( mkDirs ) {
			mkdirs(getAbsolutePath(newFile));
		}
		return moveFile(oldFile, newFile);		
	}

	public static void mkdirs(String path) {
		File fp = new File(path);
		if ( fp.isFile() ) {
			String dir = fp.getAbsolutePath();
			fp = new File(dir);
		}
		// 创建目录
		if (!fp.exists()) {
			fp.mkdirs();// 目录不存在的情况下，创建目录。
		}
	}

	public static void mkdir(String path) {
		File fp = new File(path);
		if ( fp.isFile() ) {
			String dir = fp.getAbsolutePath();
			fp = new File(dir);
		}
		// 创建目录
		if (!fp.exists()) {
			fp.mkdir();// 目录不存在的情况下，创建目录。
		}
	}

	// public static void main(String [] args){
	// String filePath = "d:/test1/test2/test3";
	//
	// System.out.println("执行结束"+filePath);
	// }
	
}
