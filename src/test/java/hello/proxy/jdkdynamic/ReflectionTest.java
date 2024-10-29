package hello.proxy.jdkdynamic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

@Slf4j
public class ReflectionTest {

    @Test
    void reflection0() {

        Hello target = new Hello();

        /**
         * 공통로직1과 2는 호출 메소드만 다르고 그 외 코드가 모두 동일하다.
         */
        //공통로직1 시작
        log.info("start");
        String result1 = target.callA();
        log.info("result1={}", result1);
        //공통로직1 종료

        //공통로직2 시작
        target.callB();
        log.info("start");
        String result2 = target.callB();
        log.info("result2={}", result2);
        //공통로직2 종료
    }

    @Test
    void reflection1() throws Exception {

        //클래스정보
        Class classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");

        Hello target = new Hello();
        //callA 메서드 정보
        Method methodCallA = classHello.getMethod("callA");
        log.info("start");
        Object result1 = methodCallA.invoke(target);//invoke()로 동적 호출 가능
        log.info("result1={}", result1);

        //callB 메서드 정보
        Method methodCallB = classHello.getMethod("callB");
        log.info("start");
        Object result2 = methodCallB.invoke(target);//invoke()로 동적 호출 가능
        log.info("result2={}", result2);
    }

    @Test
    void reflection2() throws Exception {

        //클래스정보
        Class classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");

        Hello target = new Hello();
        //callA 메서드 정보
        Method methodCallA = classHello.getMethod("callA");
        dynamicCall(methodCallA, target);//공통화

        //callB 메서드 정보
        Method methodCallB = classHello.getMethod("callB");
        dynamicCall(methodCallB, target);//공통화
    }

    private void dynamicCall(Method method, Object target) throws Exception {
        log.info("start");
        //target.callA();//호출하는 메서드가 다름
        Object result = method.invoke(target);
        log.info("result={}", result);
    }

    @Slf4j
    static class Hello {

        public String callA() {
            log.info("callA");
            return "A";
        }

        public String callB() {
            log.info("callB");
            return "B";
        }

    }
}
