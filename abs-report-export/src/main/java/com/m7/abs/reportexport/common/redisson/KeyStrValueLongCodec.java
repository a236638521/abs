package com.m7.abs.reportexport.common.redisson;

import io.netty.buffer.ByteBuf;
import org.apache.commons.lang3.StringUtils;
import org.redisson.client.codec.StringCodec;
import org.redisson.client.handler.State;
import org.redisson.client.protocol.Decoder;

import java.nio.charset.StandardCharsets;

/**
 * Long类型编解码
 */
public class KeyStrValueLongCodec extends StringCodec {

    public static final KeyStrValueLongCodec INSTANCE = new KeyStrValueLongCodec();

    private final Decoder<Object> valueDecoder = new Decoder<Object>() {
        @Override
        public Object decode(ByteBuf buf, State state) {
            if (buf == null) {
                return 0L;
            }
            String str = buf.toString(StandardCharsets.UTF_8);
            buf.readerIndex(buf.readableBytes());
            if (StringUtils.isEmpty(str)) {
                return 0L;
            }
            return Long.valueOf(str);
        }
    };

    @Override
    public Decoder<Object> getMapValueDecoder() {
        return valueDecoder;
    }
}
