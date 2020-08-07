package org.sperri.hbase.util;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jie zhao
 * @date 2020/4/15 16:53
 */
public class HBaseUtils {

    private static final Logger logger = LoggerFactory.getLogger(HBaseUtils.class);

    /** zookeeper集群地址 */
    private static final String ZK_CLUSTER_HOSTS = "10.0.0.190,10.0.0.191,10.0.0.192";
    //private static final String ZK_CLUSTER_HOSTS = "10.0.0.136";

    /** zookeeper端口 */
    private static final String ZK_CLUSTER_PORT = "2181";

    /**
     * HBase的全局连接
     */
    private static Connection connection;

    static {
        // 默认加载classpath下hbase-stie.xml
        Configuration configuration = HBaseConfiguration.create();
        configuration.set("hbase.zookeeper.quorum", ZK_CLUSTER_HOSTS);
        configuration.set("hbase.zookeeper.property.clientPort", ZK_CLUSTER_PORT);

        try {
            connection = ConnectionFactory.createConnection(configuration);
        } catch (IOException e) {
            logger.error("初始化HBase连接失败：" + e);
        }
    }

    /**
     * Can't be instantiated.
     */
    private HBaseUtils() {

    }

    /**
     * 返回HBase连接
     * @return
     */
    public static Connection getConnection() {
        return connection;
    }

    /**
     * 获得HBase数据库中所有的表名
     * @return
     * @throws IOException
     */
    public static TableName[] getTableNames() throws IOException {
        Admin admin = connection.getAdmin();
        return admin.listTableNames();
    }

    /**
     * 获取HBase数据库中所有表的定义
     * @return
     * @throws IOException
     */
    public static HTableDescriptor[] getTableDescriptors() throws IOException {
        Admin admin = connection.getAdmin();
        return admin.listTables();
    }


    /**
     * 获得某列数据
     * @param tableName
     * @param columnFamily
     * @param qualifier
     * @return
     * @throws IOException
     */
    public static Map<String, Map<String, String>> scan(String tableName, String columnFamily, String qualifier) throws IOException {
        Table table = connection.getTable(TableName.valueOf(tableName));
        Scan scan = new Scan();
        scan.setCaching(500);
        scan.setCacheBlocks(false);
        scan.addColumn(Bytes.toBytes(columnFamily), Bytes.toBytes(qualifier));
        ResultScanner resultScanner = table.getScanner(scan);
        return getResult(resultScanner);
    }

    /**
     * 获取scan结果
     * @param resultScanner
     * @return
     */
    private static Map<String, Map<String, String>> getResult(ResultScanner resultScanner) {
        Map<String, Map<String, String>> resultMap = new HashMap<>();
        for (Result result : resultScanner) {
            List<Cell> cells = result.listCells();
            Map<String, String> cellMap = new HashMap<>();
            for (Cell cell : cells) {
                cellMap.put(Bytes.toString(CellUtil.cloneQualifier(cell)), Bytes.toString(CellUtil.cloneValue(cell)));
            }
            resultMap.put(Bytes.toString(result.getRow()), cellMap);
        }
        return resultMap;
    }

    public static void main(String[] args)  {

        try {
            // 获得HBase中所有表名
            TableName[] tableNames = HBaseUtils.getTableNames();
            for (TableName tableName : tableNames) {
                System.out.println(tableName.getNameAsString());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //
        /*Admin admin = getConnection().getAdmin();
        HTableDescriptor tableDescriptor = admin.getTableDescriptor(TableName.valueOf("XINJIANGWUDONG.GEOSOUNDREALTIME"));
        System.out.println(tableDescriptor.getValue("1535341602000.:.7-4"));
        System.out.println(tableDescriptor.getNameAsString());
        Map<ImmutableBytesWritable, ImmutableBytesWritable> values = tableDescriptor.getValues();
        for (Map.Entry<ImmutableBytesWritable, ImmutableBytesWritable> entry : values.entrySet()) {
            String key = Bytes.toString(entry.getKey().get());
            String value = Bytes.toString(entry.getValue().get());
            System.out.println("key: " + key + " value: " + value);
        }*/
    }
}
