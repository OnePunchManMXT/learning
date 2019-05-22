package com.mxt.flume.interceptors;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;

import static com.mxt.flume.interceptors.HostInterceptors.Constants.*;

/**
 * @author Administrator
 * @date 2018/11/6
 * @description :
 */
public class HostInterceptors implements Interceptor {

    private static  final Logger logger = LoggerFactory.getLogger(HostInterceptors.class);

    private  final boolean preserceExisting ;

    private  final  String header;
    private String host = null;


    private  HostInterceptors(boolean preserceExisting,String header,boolean useIP){
        this.preserceExisting = preserceExisting;
        this.header = header;
        InetAddress addr;
        try {
            addr = InetAddress.getLocalHost();
            if (useIP) {
                host = addr.getHostAddress();
            } else {
                host = addr.getCanonicalHostName();
            }
        } catch (UnknownHostException e) {
            logger.warn("Could not get local host address. Exception follows.", e);
        }

    }


    public Event intercept(Event event) {
        Map<String ,String> headers = event.getHeaders();
        if(preserceExisting && headers.containsKey(header)){
            return  event;
        }
        if(host != null){
            headers.put(header,host);
        }
        return  event;
    }


    public List<Event> intercept(List<Event> events) {
        for(Event e : events){
            intercept( e);
        }
        return  events;
    }

    public void initialize() {

    }

    public void close() {

    }

    public static class MxtBuilder implements  Interceptor.Builder{

        private  boolean preserveExisting = PRESERVE_DFLT;
        private  boolean user_ip = USE_IP_DFLT;
        private  String  header = HOST;

        public Interceptor build() {
            return  new HostInterceptors(preserveExisting,header,user_ip);
        }

        public void configure(Context context) {
            preserveExisting = context.getBoolean(PRESERVE,PRESERVE_DFLT);
            user_ip = context.getBoolean(USE_IP,USE_IP_DFLT);
            header = context.getString(HOST_HEADER,HOST);
        }
    }

    public static class Constants {
        public static String HOST = "host";

        public static String PRESERVE = "preserveExisting";
        public static boolean PRESERVE_DFLT = false;

        public static String USE_IP = "useIP";
        public static boolean USE_IP_DFLT = true;

        public static String HOST_HEADER = "hostHeader";
    }

}

