package com;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.cleverframe.fastdfs.client.DefaultStorageClient;
import org.cleverframe.fastdfs.client.DefaultTrackerClient;
import org.cleverframe.fastdfs.client.StorageClient;
import org.cleverframe.fastdfs.client.TrackerClient;
import org.cleverframe.fastdfs.conn.DefaultCommandExecutor;
import org.cleverframe.fastdfs.model.StorePath;
import org.cleverframe.fastdfs.pool.ConnectionPool;
import org.cleverframe.fastdfs.pool.PooledConnectionFactory;

/**
 * client test
 *
 * @author Happy Fish / YuQing
 * @version Version 1.18
 */
public class TestClient3 {

  /**
   * entry point
   *
   * @param args comand arguments
   *             <ul><li>args[0]: config filename</li></ul>
   *             <ul><li>args[1]: local filename to upload</li></ul>
   */
  public static void main(String args[]) {
//    if (args.length < 2) {
//      System.out.println("Error: Must have 2 parameters, one is config filename, "
//        + "the other is the local filename to upload");
//      return;
//    }

    //String conf_filename = args[0];
    String local_filename = "/root/1.txt";//args[1];
    testIt(local_filename);
  }
  
  private static void testIt(String local_filename) {
	// 连接创建工厂（用户新建连接）
	  PooledConnectionFactory pooledConnectionFactory = new PooledConnectionFactory(500, 500);
	  // 连接池配置
	  GenericKeyedObjectPoolConfig conf = new GenericKeyedObjectPoolConfig();
	  conf.setMaxTotal(200);
	  conf.setMaxTotalPerKey(200);
	  conf.setMaxIdlePerKey(100);
	  
	  // 连接池
	  ConnectionPool connectionPool = new ConnectionPool(pooledConnectionFactory, conf);
	  Set<String> trackerSet = new HashSet<String>();
	  trackerSet.add("192.168.100.233:22122");
	  // 命令执行器
	  DefaultCommandExecutor commandExecutor = new DefaultCommandExecutor(trackerSet, connectionPool);
	  // Tracker客户端
	  TrackerClient trackerClient = new DefaultTrackerClient(commandExecutor);
	  // Storage客户端
	  StorageClient storageClient = new DefaultStorageClient(commandExecutor, trackerClient);

	  // Tracker客户端 - 获取Storage服务器节点信息
	  //StorageNode storageNode = trackerClient.getStorageNode("group1");

	  // 获取文件信息
	  //StorageNodeInfo storageNodeInfo = trackerClient.getFetchStorage("group1", "M00/00/00/wKg4i1gxz_6AIPPsAAiCQSk77jI661.png");

	  // 上传文件
	  File file = new File(local_filename);
	  try {
		  FileInputStream fileInputStream = FileUtils.openInputStream(file);
		  StorePath storePath = storageClient.uploadFile("group1", fileInputStream, file.length(), "txt");
		  // StorePath{group='group1', path='M00/00/00/wKhk6Vq_RgaASB4mAAAAEu6v6vY694.txt'}
		  System.out.println(storePath);
		  // 下载文件
		  //DownloadFileWriter downloadFileWriter = new DownloadFileWriter("F:\\123.xlsx");
		  //String filePath = storageClient.downloadFile("group1", "M00/00/00/wKgKgFg02TaAY3mTADCUhuWQdRc53.xlsx", downloadFileWriter);
	
		  // ... 更多操作
	  } catch (Exception e) {
	  } finally {
	  // 关闭连接池
		  connectionPool.close();
	  }
  }
}