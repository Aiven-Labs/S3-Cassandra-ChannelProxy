/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package oy.aiven.cassandra;

import static org.carlspring.cloud.storage.s3fs.S3Factory.ACCESS_KEY;
import static org.carlspring.cloud.storage.s3fs.S3Factory.SECRET_KEY;

import java.io.IOException;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.cassandra.io.util.ChannelProxy;
import org.apache.cassandra.io.util.ChannelProxyFactory;
import org.apache.cassandra.io.util.File;

public class S3ChannelProxyFactory extends ChannelProxyFactory {

    private final FileSystem fileSystem;
    private final String source;

    private static Map<String, ?> propertyMap() {
        Map<String, Object> result = new HashMap<>();
        result.put(ACCESS_KEY, "access key");
        result.put(SECRET_KEY, "secret key");
        return Collections.unmodifiableMap(result);
    }

    protected S3ChannelProxyFactory(Map<String, String> args) throws IOException {
        super(null, null);
        this.readerFactory = this::readerProxy;
        this.writerFactory = this::writerProxy;

        fileSystem = FileSystems.newFileSystem(URI.create("s3:///"), propertyMap(),
                Thread.currentThread().getContextClassLoader());
        source = args.get("source");
    }

    private ChannelProxy readerProxy(File file) {
        if (file.path().startsWith(source)) {
            Path path = fileSystem.getPath(file.path().substring(source.length()));
            try {
                return new ChannelProxy(file, FileChannel.open(path, StandardOpenOption.READ));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return new ChannelProxy(file);
    }

    private ChannelProxy writerProxy(File file) {
        if (file.path().startsWith(source)) {

            ChannelProxy result = null;
            Path path = fileSystem.getPath(file.path().substring(source.length()));
            try {
                if (path.toFile().exists()) {
                    result = new ChannelProxy(file, FileChannel.open(path, StandardOpenOption.WRITE));
                } else {
                    result = new ChannelProxy(file,
                            FileChannel.open(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return result;
        }
        return new ChannelProxy(file);
    }
}
