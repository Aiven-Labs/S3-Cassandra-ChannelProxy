# S3-Cassandra-ChannelProxy
A Cassandra ChannelProxyFactory to read/write AWS S3 storage

This is an implementation of the proposed Cassandra ChannelProxyFactory (CEP-36[^cep]) to build proxies to AWS S3 storage.

## Building 
To build this project you will need the jar(s) from building the Cassandra code with CEP-36 [^C5]  and you need to build the included submodule `s3fs-nio`.  The Cassandra jar and source jar should be placed in the `libs/org/apache/apache-cassandra/5.0-alpha1-SNAPSHOT` subdirectory. 

### Building `s3fs-nio`

```
cd s3fs-nio
./gradlew
./publishToMavenLocal
```


[^cep]: https://cwiki.apache.org/confluence/display/CASSANDRA/CEP-36%3A+A+Configurable+ChannelProxy+to+alias+external+storage+locations
[^C5]: https://github.com/claudenw/cassandra/tree/channel_proxy_factory

