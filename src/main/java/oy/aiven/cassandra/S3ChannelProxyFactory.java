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
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.cassandra.io.util.RelocatingFileSystemMapper;

public class S3ChannelProxyFactory extends RelocatingFileSystemMapper {

    private static Map<String, ?> propertyMap() {
        Map<String, Object> result = new HashMap<>();
        result.put(ACCESS_KEY, "access key");
        result.put(SECRET_KEY, "secret key");
        return Collections.unmodifiableMap(result);
    }

    private static Path createPath() throws IOException {
        FileSystem fileSystem = FileSystems.newFileSystem(URI.create("s3:///"), propertyMap(),
                Thread.currentThread().getContextClassLoader());
        return fileSystem.getPath(fileSystem.getSeparator());

    }

    public S3ChannelProxyFactory(Map<String, String> args) throws IOException {
        super(createPath(), args.get("keyspace"), args.get("table"));
    }
}
