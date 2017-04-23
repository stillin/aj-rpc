package com.aj.serialize;

import java.io.IOException;

/**
 * Created by chaiaj on 2017/4/2.
 */
public interface Serializer {
    <T> byte[] serialize(T obj) throws IOException;
    <T> T deserialize(byte[] bytes, Class<T> cls) throws IOException;
}
