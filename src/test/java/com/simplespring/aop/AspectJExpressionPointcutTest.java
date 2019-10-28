package com.simplespring.aop;

import com.simplespring.HelloService;
import com.simplespring.HelloServiceImp1;
import org.junit.Assert;
import org.junit.Test;

public class AspectJExpressionPointcutTest {
    @Test
    public void testClassFilter()throws Exception{
        String expression = "execution(* com.simplespring.*.*(..))";
        AspectJExpressionPointcut aspectJExpressionPointcut = new AspectJExpressionPointcut();
        aspectJExpressionPointcut.setExpression(expression);
        boolean matches = aspectJExpressionPointcut.matchers(HelloService.class);
        Assert.assertEquals(true,matches);
    }

    @Test
    public void testMethodMatcher() throws Exception{
        String expression = "execution(* com.simplespring.*.sayHelloWorld(..))";
        AspectJExpressionPointcut aspectJExpressionPointcut = new AspectJExpressionPointcut();
        aspectJExpressionPointcut.setExpression(expression);
        boolean matches = aspectJExpressionPointcut.matchers(HelloServiceImp1.class.getDeclaredMethod("sayHelloWorld"),HelloServiceImp1.class);
        Assert.assertEquals(true,matches);
    }
}
