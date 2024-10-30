package hello.proxy.jdkdynamic;

import hello.proxy.jdkdynamic.code.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

@Slf4j
public class JdkDynamicProxyTest {

    @Test
    void dynamicA() {
        AInterface target = new AImpl();
        TimeInvocationHandler handler = new TimeInvocationHandler(target); //동적프록시에 적용할 핸들러 로직

        /**
         * 동적으로 Proxy 객체 생성 - JDK에서 제공
         */
        AInterface proxy = (AInterface)Proxy.newProxyInstance(AInterface.class.getClassLoader()
                , new Class[]{AInterface.class}
                , handler); //AInterface 타입으로 동적으로 프록시 생성
        proxy.call();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());
    }

    @Test
    void dynamicB() {
        BInterface target = new BImpl();
        TimeInvocationHandler handler = new TimeInvocationHandler(target);

        /**
         * 동적으로 Proxy 객체 생성 - JDK에서 제공
         */
        BInterface proxy = (BInterface)Proxy.newProxyInstance(BInterface.class.getClassLoader()
                , new Class[]{BInterface.class}
                , handler); //BInterface 타입
        proxy.call();
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());
    }
}
