package gateway.filter.impl;

import gateway.filter.HttpRequestFilter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

import java.io.IOException;

public class HttpRequestHeaderFilter implements HttpRequestFilter {



    @Override
    public void filter(FullHttpRequest fullRequest, ChannelHandlerContext ctx) throws IOException {
        System.out.println("HttpRequestHeaderFilter test .... ");

        String uri = fullRequest.getUri();
        fullRequest.setUri(uri + "?nio=liuhaoping");

    }
}
