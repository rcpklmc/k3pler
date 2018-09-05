package com.tht.k3pler;


import org.littleshoot.proxy.HttpFiltersAdapter;

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;

class FilteredResponse extends HttpFiltersAdapter {
    private HttpResponse httpResponse;
    private static HttpResponseStatus httpResponseStatus = HttpResponseStatus.FORBIDDEN;
    private static String blockedList[] = new String[]{"twitter", "google"};
    public FilteredResponse(HttpRequest originalRequest){
        super(originalRequest, null);
    }
    @Override
    public HttpResponse clientToProxyRequest(HttpObject httpObject) {
        Boolean block = false;
        for(String item : blockedList){
            if(originalRequest.getUri().contains(item)){
                block = true;
                break;
            }
        }
        if(block)
            return getBlockedResponse();
        return super.clientToProxyRequest(httpObject);
    }
    private HttpResponse getBlockedResponse(){
        httpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, httpResponseStatus);
        HttpHeaders.setHeader(httpResponse, HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
        return httpResponse;
    }
}