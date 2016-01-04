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
     * ��ȡMessageQueue����
     * @return
     */
    public static MessageQueue getMessageQueue(){
        if(messageQueue == null){
            messageQueue = new MessageQueue();
        }
        return messageQueue;
    }

    /**
     * ����messageQueue����
     */
    public void closeMessageQueue(){
        messageQueue = null;
    }

    /**
     * ��queue�����������Ϣ
     * @param messageStr
     * @return
     */
    public Boolean offerQueue(String messageStr){
        return queue.offer(messageStr);
    }

    /**
     * ��queue�����л�ȡ��Ϣ
     * @return
     */
    public String pollQueue(){
        return queue.poll();
    }

    /**
     * �ж϶���queue�Ƿ�Ϊ��
     * @return ��Ϊtrue
     */
    public Boolean isQueueEmpty(){
        return queue.isEmpty();
    }
}
