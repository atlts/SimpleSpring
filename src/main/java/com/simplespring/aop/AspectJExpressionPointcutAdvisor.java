package com.simplespring.aop;

import org.aopalliance.aop.Advice;
import com.simplespring.aop.PointcutAdvisor;

/**
 * 切点和通知的结合体，利用其判断相应的类和方法是否时切点，若是则可以将通知织入
 */
public class AspectJExpressionPointcutAdvisor implements PointcutAdvisor {
    private AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    private Advice advice;

    /**
     * 通过xml文件中的属性自动注入了
     * @param expression
     */
    public void setExpression(String expression) {
        pointcut.setExpression(expression);
    }



    @Override
    public Pointcut getPoint() {
        return pointcut;
    }
    public void setAdvice(Advice advice) {
        this.advice = advice;
    }
    @Override
    public Advice getAdvice() {
        return advice;
    }
}
