package com.quncao.common;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by shaohk on 2015/9/28.
 */
public class MessageQueue {

    private static MessageQueue messageQueue = null;
    private Queue<String> queue = new ConcurrentLinkedQueue<>();
    private MessageQueue(){}

    /**
     * 获取MessageQueue对象
     * @return
     */
    public static MessageQueue getMessageQueue(){
        if(messageQueue == null){
            messageQueue = new MessageQueue();
        }
        return messageQueue;
    }

    /**
     * 回收messageQueue对象
     */
    public void closeMessageQueue(){
        messageQueue = null;
    }

    /**
     * 向queue队列中添加消息
     * @param messageStr
     * @return
     */
    public Boolean offerQueue(String messageStr){
        return queue.offer(messageStr);
    }

    /**
     * 从queue队列中获取消息
     * @return
     */
    public String pollQueue(){
        return queue.poll();
    }

    /**
     * 判断队列queue是否为空
     * @return 空为true
     */
    public Boolean isQueueEmpty(){
        return queue.isEmpty();
    }
}
