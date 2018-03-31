package org.bidtime.pic.fdfs;

import org.apache.commons.pool2.impl.GenericKeyedObjectPoolConfig;
import org.bidtime.pic.utils.PropEx;
import org.cleverframe.fastdfs.pool.ConnectionPool;
import org.cleverframe.fastdfs.pool.PooledConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConnectionPools {

	private static final Logger log = LoggerFactory.getLogger(ConnectionPools.class);
	
    private PooledConnectionFactory pooledConnectionFactory;
    private GenericKeyedObjectPoolConfig conf;

	public ConnectionPools() {
		// 连接创建工厂（用户新建连接）
		pooledConnectionFactory = new PooledConnectionFactory(500, 500);
		// 连接池配置
		conf = new GenericKeyedObjectPoolConfig();
		conf.setMaxTotal(200);
		conf.setMaxTotalPerKey(200);
		conf.setMaxIdlePerKey(100);
	}

	private static ConnectionPools instance;
	
	private static CfgPar par;

	static {
		par = getProps("fdfs_client.conf");
	}
	
	private static CfgPar getProps(String props) {
		CfgPar c = new ConnectionPools().new CfgPar();
		PropEx p = new PropEx();
		try {
			p.loadOfSrc(props);
			c.setTrackerServer(p.getString("tracker_server"));
			c.setHttpServer(p.getString("http_server"));
		} catch (Exception e) {
			log.error("getProps: {}", e.getMessage());
		} finally {
			p = null;
		}
		return c;
	}
	
	
	class CfgPar {
		private String trackerServer;
		
		private String httpServer;

		public String getHttpServer() {
			return httpServer;
		}

		public void setHttpServer(String httpServer) {
			this.httpServer = httpServer;
		}

		public String getTrackerServer() {
			return trackerServer;
		}

		public void setTrackerServer(String trackerServer) {
			this.trackerServer = trackerServer;
		}
		
//		public String getUrl(String group, String path) {
//			StringBuilder sb = new StringBuilder();
//			sb.append(this.httpServer);
//			sb.append(group);
//			sb.append(path);
//			return sb.toString();
//		}
	}
	
    public static ConnectionPools getInstance() {
        if (instance == null) {
            synchronized (ConnectionPools.class) {
                if (instance == null) {
                    instance = new ConnectionPools();
                }
            }
        }
        return instance;
    }
    
	public ConnectionPool get() {
		// 连接池
		ConnectionPool connectionPool = new ConnectionPool(pooledConnectionFactory, conf);
		return connectionPool;
	}
	
	public CfgPar getPar() {
		return par;
	}
	
//	public void setCfgPar(CfgPar par) {
//		this.par = par;		
//	}

}
