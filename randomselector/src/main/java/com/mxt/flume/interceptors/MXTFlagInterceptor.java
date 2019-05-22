package com.mxt.flume.interceptors;

import org.apache.flume.Context;
import org.apache.flume.Event;
import org.apache.flume.interceptor.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static  com.mxt.flume.interceptors.MXTFlagInterceptor.Constats.*;

/**
 * @author Administrator
 * @date 2018/11/6
 * @description :
 */
public class MXTFlagInterceptor implements Interceptor {

    private Logger logger = LoggerFactory.getLogger(MXTFlagInterceptor.class);

    private  final boolean perserve;
    private final  String header;
    private String values = null;

    private MXTFlagInterceptor(boolean perserve,String header){

        this.perserve = perserve;
        this.header = header;
        values = "MXT";
    }



    public Event intercept(Event event) {
        Map<String,String> headers = event.getHeaders();

        if(perserve && headers.containsKey(header)){
            return  event;
        }
        if(values != null){
            headers.put(header,values);
        }
        return  event;
    }


    public void initialize() {

    }


    public void close() {

    }


    public List<Event> intercept(List<Event> events) {

        for(Event e :events){
            intercept(e);
        }
        return  events;
    }

    public static class FlagBuider implements  Interceptor.Builder{

        private boolean perserce = PERSERVE_DEFAULT;
        private String header = HEADER_KEY ;
        public Interceptor build() {
            return new MXTFlagInterceptor(perserce,header);
        }

        public void configure(Context context) {
            perserce = context.getBoolean(PERSERVEXISTING,PERSERVE_DEFAULT);
            header = context.getString(HEADER_KEY,HEADER_DEFAULT);
        }
    }

    public static  class Constats{
        public static  boolean PERSERVE_DEFAULT= true;
        public   static  String PERSERVEXISTING = "preserveExisting";

        public static String HEADER_KEY = "header_key";
        public static String HEADER_DEFAULT = "flag";


    }
}
