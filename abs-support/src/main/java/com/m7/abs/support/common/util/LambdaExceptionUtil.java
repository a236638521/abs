package com.m7.abs.support.common.util;

import com.m7.abs.support.core.exception.ConsumerExecuteException;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhuhf
 */
public class LambdaExceptionUtil {
    private static final Logger logger = LoggerFactory.getLogger(LambdaExceptionUtil.class);

    public static <T> Consumer<? super T> consumerExceptionWrapper(Consumer<? super T> consumer) {
        return taskEvent -> {
            try {
                consumer.accept(taskEvent);
            } catch (Exception e) {
                logger.error("consumer 执行异常", e);
                throw new ConsumerExecuteException(e);
            }
        };
    }
}
