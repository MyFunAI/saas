package com.iaskdata.util;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * kafka工具类
 * @author jiangzhx@gmail.com
 * @date 2015年7月17日
 */
public class KafkaUtil {
    private static final Logger logger = LoggerFactory.getLogger("stdout");
    
    private static KafkaUtil instance;
    
    private static final ThreadPoolExecutor exec = (ThreadPoolExecutor) Executors.newFixedThreadPool(100);
    private static ProducerConfig config;
    
    private static Map<String, Producer<String, String>> producers = new ConcurrentHashMap<String, Producer<String,String>>();

    private KafkaUtil() { }

    public static final KafkaUtil getInstance() {
        if (null == instance) {
            syncInit();
        }
        return instance;
    }

    private static synchronized void syncInit() {
        if (instance == null) {
            Properties props = new Properties();

            props.put("metadata.broker.list", Constant.kafkaBroker);
            props.put("serializer.class", "kafka.serializer.StringEncoder");
            props.put("request.required.acks", "1");
            props.put("producer.type", "async");

            config = new ProducerConfig(props);

            ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(1);
            scheduledThreadPool.scheduleWithFixedDelay(new Runnable() {
                
                @Override
                public void run() {
                    logger.info("kafka thread pool: active count: {}, pool size: {}, queue size: {}", exec.getActiveCount(), exec.getPoolSize(), exec.getQueue().size());
                }
            }, 5, 60, TimeUnit.SECONDS);
            
            instance = new KafkaUtil();
        }
    }

    /**
     * 发送数据到对应的topic
     *
     * @param topic
     * @param key
     * @param data
     */
    public void send(final String topic,final String key, final String data) {
        exec.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Producer<String, String> producer = producers.get(topic);

                    if (null == producer) {
                        producer = new Producer<String, String>(config);
                        producers.put(topic, producer);
                    }

                    KeyedMessage<String, String> message = new KeyedMessage<String, String>(topic,key, data);
                    producer.send(message);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        });
    }

    /**
     * 发送数据到对应的topic
     *
     * @param topic
     * @param data
     */
    public void send(final String topic, final String data) {
        exec.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Producer<String, String> producer = producers.get(topic);

                    if (null == producer) {
                        producer = new Producer<String, String>(config);
                        producers.put(topic, producer);
                    }

                    KeyedMessage<String, String> message = new KeyedMessage<String, String>(topic, data);
                    producer.send(message);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        });
    }
}
