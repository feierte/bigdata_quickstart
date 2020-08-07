package org.sperri.hadoop.client;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.sperri.hadoop.config.HadoopConfiguration;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author jie zhao
 * @date 2020/3/26 15:06
 */
public class TestClient {

    public static void main(String[] args) throws URISyntaxException, IOException, InterruptedException {
        Configuration conf = new Configuration();
        conf.set("dfs.replication", "1");
        // conf.addResource();
        FileSystem fileSystem = FileSystem.get(URI.create(HadoopConfiguration.HDFS_PATH), conf, "root");
        System.out.println(fileSystem.getWorkingDirectory().getName());
        System.out.println(fileSystem.exists(new Path(HadoopConfiguration.HDFS_PATH + "/hbase/WALs")));

        Path newPath = new Path(HadoopConfiguration.HDFS_PATH + "/tmp");

        boolean isDelete = fileSystem.deleteOnExit(newPath);
        System.out.println(isDelete);

        /*boolean newFile = fileSystem.createNewFile(newPath);
        System.out.println(newFile);
        RemoteIterator<LocatedFileStatus> remoteIterator =
                fileSystem.listFiles(new Path(HadoopConfiguration.HDFS_PATH + "/hbase/WALs"), false);
        while (remoteIterator.hasNext()) {
            System.out.println(remoteIterator.next().isDirectory());
        }*/
    }
}
