# RocketMQ-TailerProducer
## configuration sample file
```
test.sources=tailSource
test.channels=fileChannel
test.sinks=rocketMQSink
test.sources.tailSource.type=org.apache.flume.source.taildir.rotate.TaildirSource
test.sources.tailSource.positionFile=/dianyi/data/tail_positon.json
test.sources.tailSource.filegroups=log
test.sources.tailSource.filegroups.log=/dianyi/log/flume-test/rotate.log.*
test.sources.tailSource.batchSize=5
test.sources.tailSource.backoffSleepIncrement=1000
test.sources.tailSource.maxBackoffSleep=5000
test.sources.tailSource.idleTimeout=3600000
test.sources.tailSource.cachePatternMatching=false

test.sources.tailSource.channels=fileChannel

test.channels.fileChannel.type = file
test.channels.fileChannel.checkpointDir = /dianyi/data/checkpoint/test
test.channels.fileChannel.dataDirs = /dianyi/data/flume/data/filechannel/test
test.channels.fileChannel.capacity = 10000
test.channels.fileChannel.transactionCapacity = 200

test.sinks.rocketMQSink.type=com.ndpmedia.flume.sink.rocketmq.RocketMQSink
test.sinks.rocketMQSink.namesrvAddr=172.30.30.125:9876
test.sinks.rocketMQSink.topic=T_FLUME_TEST
test.sinks.rocketMQSink.producerGroup=PG_FLUME_TEST
test.sinks.rocketMQSink.asyn=true
test.sinks.rocketMQSink.channel=fileChannel
```

/opt/apache-flume-1.6.0-bin/bin/flume-ng agent -c /opt/apache-flume-1.6.0-bin/conf/ -f flume-linux-conf.properties -n test -Denable_ssl=true -Drocketmq.namesrv.domain=172.30.30.125 -Dlog.home=/dianyi/log/logs -Dflume.root.logger=INFO,console
