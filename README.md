# S3-Cassandra-ChannelProxy
A Cassandra ChannelProxyFactory to read/write AWS S3 storage

## Building 
To build this project you will need the jar from building the Cassandra code with CEP-36 [^1]  and you need to build the included submodule `s3fs-nio`.  The Cassandra lib and source lib should be placed in the `lib` subdirectory.  You may need to add 'lib' as a library.

### Building `s3fs-nio`

```
cd s3fs-nio
./gradlew
./publishToMavenLocal
```


[^1]: https://github.com/claudenw/cassandra/tree/channel_proxy_factory

