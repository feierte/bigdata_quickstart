package org.sperri.zookeeper;

/**
 * @author jie zhao
 * @date 2019/11/27 20:21
 */
public abstract class Constants {

    /** zookeeper集群地址 */
    public static final String ZK_CLUSTER_HOSTS = "192.168.116.129,192.168.116.130,192.168.116.131";

    /** zookeeper端口 */
    public static final String ZK_CLUSTER_PORT = "2181";

    /** 与zookeeper连接的字符串 */
    public static final String ZK_CONNECTION_STRING = "192.168.116.129:2181,192.168.116.130:2181,192.168.116.131:2181";

    /** 会话超时时间 */
    public static final int SESSION_TIMEOUT = 30000;
}
