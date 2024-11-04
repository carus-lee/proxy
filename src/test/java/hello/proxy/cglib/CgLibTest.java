package hello.proxy.cglib;

import hello.proxy.cglib.code.TimeMethodInterceptor;
import hello.proxy.common.service.ConcreteService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.cglib.proxy.Enhancer;

@Slf4j
public class CgLibTest {

    @Test
    void cglib() {
        ConcreteService target = new ConcreteService();

        /**
         * cglib는 Enhancer를 사용하여 프록시를 생성
         */
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(ConcreteService.class);//클래스 지정
        enhancer.setCallback(new TimeMethodInterceptor(target));//클래스
        ConcreteService proxy = (ConcreteService)enhancer.create();//프록시 생성
        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        proxy.call();
    }
}
