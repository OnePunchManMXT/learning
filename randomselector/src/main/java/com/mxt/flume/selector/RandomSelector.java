package com.mxt.flume.selector;

import org.apache.flume.Channel;
import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.FlumeException;
import org.apache.flume.channel.AbstractChannelSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Administrator
 * @date 2018/10/27
 * @description :
 */
public class RandomSelector extends AbstractChannelSelector {

    @SuppressWarnings("unused")
    private static final Logger LOG = LoggerFactory.getLogger(RandomSelector.class);
    private Map<String, Channel> channels;
    private List<Channel> optionalChannels = new ArrayList<Channel>();

    public List<Channel> getRequiredChannels(Event event) {
        String randomStr = channels.keySet().toArray(new String[0])[(int) (Math.random() * channels.entrySet().size())];
        System.out.println(channels.keySet());
        System.out.println(randomStr);
        Channel channel = channels.get(randomStr);
        ArrayList<Channel> requiredChannel = new ArrayList<Channel>();
        requiredChannel.add(channel);
        return requiredChannel;
    }

    public List<Channel> getOptionalChannels(Event event) {
        return optionalChannels;
    }

    public void configure(Context context) {
        channels = getChannelNameMap();
    }
}
