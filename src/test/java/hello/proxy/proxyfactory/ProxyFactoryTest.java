package hello.proxy.proxyfactory;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ConcreteService;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;

@Slf4j
public class ProxyFactoryTest {

    @Test
    @DisplayName("인터페이스가 있으면 JDK 동적 프록시 사용")
    void interfaceProxy() {
        ServiceInterface target = new ServiceImpl(); //인터페이스 존재
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeAdvice());
        ServiceInterface proxy = (ServiceInterface)proxyFactory.getProxy();

        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        proxy.save();

        //AopUtils에서 제공하는 메서드는 ProxyFactory 통해서 만들어진 객체만 검증 가능
        log.info("AOP 프록시 여부={}, ", AopUtils.isAopProxy(proxy));
        log.info("JDK 동적 프록시 여부={}, ", AopUtils.isJdkDynamicProxy(proxy));
        log.info("CGLIB 여부={}, ", AopUtils.isCglibProxy(proxy));
        //테스트 결과
        Assertions.assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        Assertions.assertThat(AopUtils.isJdkDynamicProxy(proxy)).isTrue();
        Assertions.assertThat(AopUtils.isCglibProxy(proxy)).isFalse();
    }

    @Test
    @DisplayName("구체 클래스만 있으면 CGLIB 사용")
    void concreteProxy() {
        ConcreteService target = new ConcreteService(); //구체 클래스
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.addAdvice(new TimeAdvice());
        ConcreteService proxy = (ConcreteService)proxyFactory.getProxy();

        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        proxy.call();

        //AopUtils에서 제공하는 메서드는 ProxyFactory 통해서 만들어진 객체만 검증 가능
        log.info("AOP 프록시 여부={}, ", AopUtils.isAopProxy(proxy));
        log.info("JDK 동적 프록시 여부={}, ", AopUtils.isJdkDynamicProxy(proxy));
        log.info("CGLIB 여부={}, ", AopUtils.isCglibProxy(proxy));
        //테스트 결과
        Assertions.assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        Assertions.assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
        Assertions.assertThat(AopUtils.isCglibProxy(proxy)).isTrue();
    }

    @Test
    @DisplayName("proxyTargetClass옵션을 사용하면 인터페이스가 있어도 'CGLIB 사용'을 강제함. 즉, 클래스 기반 프록시 사용")
    void proxyTargetClass() {
        ServiceInterface target = new ServiceImpl(); //인터페이스 존재
        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(true); //targetClass 기반으로 프록시 생성 여부
        proxyFactory.addAdvice(new TimeAdvice());
        ServiceInterface proxy = (ServiceInterface)proxyFactory.getProxy();

        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        proxy.save();

        //AopUtils에서 제공하는 메서드는 ProxyFactory 통해서 만들어진 객체만 검증 가능
        log.info("AOP 프록시 여부={}, ", AopUtils.isAopProxy(proxy));
        log.info("JDK 동적 프록시 여부={}, ", AopUtils.isJdkDynamicProxy(proxy));
        log.info("CGLIB 여부={}, ", AopUtils.isCglibProxy(proxy));
        //테스트 결과
        Assertions.assertThat(AopUtils.isAopProxy(proxy)).isTrue();
        Assertions.assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse();
        Assertions.assertThat(AopUtils.isCglibProxy(proxy)).isTrue();
    }
}
