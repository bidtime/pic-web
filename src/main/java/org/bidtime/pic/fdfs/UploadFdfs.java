package org.bidtime.pic.fdfs;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.cleverframe.fastdfs.client.DefaultStorageClient;
import org.cleverframe.fastdfs.client.DefaultTrackerClient;
import org.cleverframe.fastdfs.client.StorageClient;
import org.cleverframe.fastdfs.client.TrackerClient;
import org.cleverframe.fastdfs.conn.DefaultCommandExecutor;
import org.cleverframe.fastdfs.model.StorePath;
import org.cleverframe.fastdfs.pool.ConnectionPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 通过UploadFdfs将图片上传至fastdfs
 * @author riverbo
 * @since 2018.04.13
 */
public class UploadFdfs {
	
	private static final Logger log = LoggerFactory.getLogger(UploadFdfs.class);

	/**
	 * upload
	 * @param fileName
	 * @return
	 * @author riverbo
	 * @since 2018.04.13
	 */
	public static String upload(String fileName) {
		// 连接池
		ConnectionPools pools = ConnectionPools.getInstance();
		ConnectionPool connectionPool = pools.get();
		Set<String> trackerSet = new HashSet<String>();
		trackerSet.add(pools.getPar().getTrackerServer());
		// 命令执行器
		DefaultCommandExecutor commandExecutor = new DefaultCommandExecutor(trackerSet, connectionPool);
		// Tracker客户端
		TrackerClient trackerClient = new DefaultTrackerClient(commandExecutor);
		// Storage客户端
		StorageClient storageClient = new DefaultStorageClient(commandExecutor, trackerClient);

		// Tracker客户端 - 获取Storage服务器节点信息
		// StorageNode storageNode = trackerClient.getStorageNode("group1");

		// 获取文件信息
//		 StorageNodeInfo storageNodeInfo =
//		 trackerClient.getFetchStorage("group1",
//		 "M00/00/00/wKg4i1gxz_6AIPPsAAiCQSk77jI661.png");
		String url = null;
		// 上传文件
		File file = new File(fileName);
		try {
			FileInputStream fileInputStream = FileUtils.openInputStream(file);
			StorePath storePath = storageClient.uploadFile("group1", fileInputStream, file.length(), "txt");
			// StorePath{group='group1',
			// path='M00/00/00/wKhk6Vq_RgaASB4mAAAAEu6v6vY694.txt'}
			System.out.println(storePath);
			url = pools.getPar().getHttpServer() + storePath.getFullPath();
			// FilePath fp = JSON.parseObject(storePath, FilePath.class, new
			// Feature[0]);
			// 下载文件
			// DownloadFileWriter downloadFileWriter = new
			// DownloadFileWriter("F:\\123.xlsx");
			// String filePath = storageClient.downloadFile("group1",
			// "M00/00/00/wKgKgFg02TaAY3mTADCUhuWQdRc53.xlsx",
			// downloadFileWriter);
		} catch (Exception e) {
			log.error("upload: {}", e.getMessage());
		} finally {
			// 关闭连接池
			connectionPool.close();
		}
		return url;
	}
}
