package org.sperri.hadoop.client;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;

import java.io.IOException;
import java.net.URI;

/**
 * @author jie zhao
 * @date 2020/3/26 15:53
 */
public class HdfsClientUtils {



    public static FileSystem getFileSystem(String url, Configuration conf, String username)
            throws IOException, InterruptedException {
        return FileSystem.get(URI.create(url), conf, username);
    }
}
