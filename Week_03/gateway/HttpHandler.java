package gateway;

import gateway.filter.impl.HttpRequestHeaderFilter;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.ReferenceCountUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class    HttpHandler extends ChannelInboundHandlerAdapter {

    private static Logger logger = LoggerFactory.getLogger(HttpHandler.class);

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws IOException {
        try {
            //logger.info("channelRead流量接口请求开始，时间为{}", startTime);
            FullHttpRequest fullRequest = (FullHttpRequest) msg;

            HttpRequestHeaderFilter headerFilter = new HttpRequestHeaderFilter();
            //调用过滤器接口，添加请求参数
            headerFilter.filter(fullRequest,ctx);

            String uri = fullRequest.uri();
            //logger.info("接收到的请求url为{}", uri);
            if (uri.contains("/test")) {
                handler(fullRequest, ctx);
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private void handler(FullHttpRequest fullRequest, ChannelHandlerContext ctx) throws IOException, URISyntaxException {

        URIBuilder uriBuilder = new URIBuilder("http://localhost:8881");
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(fullRequest.uri());
        //获取请求参数，添加到请求后端服务的uri中
        queryStringDecoder.parameters().entrySet().forEach( entry ->{
            uriBuilder.addParameter(entry.getKey(), entry.getValue().get(0));
            System.out.println("key : " + entry.getKey() + "  value : " + entry.getValue().get(0));
        });


        FullHttpResponse response = null;

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        CloseableHttpResponse responseHttpClient = null;
        try {
            responseHttpClient = httpClient.execute(httpGet);

            String responsValue = null;
            if (responseHttpClient.getStatusLine().getStatusCode() == 200) {
                responsValue = EntityUtils.toString(responseHttpClient.getEntity(), "UTF-8");
            }

            response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(responsValue.getBytes("UTF-8")));
            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-Length", response.content().readableBytes());

        } catch (Exception e) {
            logger.error("处理测试接口出错", e);
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
        } finally {
            if (fullRequest != null) {
                if (!HttpUtil.isKeepAlive(fullRequest)) {
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    response.headers().set(CONNECTION, KEEP_ALIVE);
                    ctx.write(response);
                }
            }

            if (responseHttpClient != null) {
                responseHttpClient.close();
            }
            httpClient.close();
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
