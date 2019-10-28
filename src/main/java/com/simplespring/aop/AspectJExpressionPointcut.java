package com.simplespring.aop;


import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.PointcutPrimitive;
import org.aspectj.weaver.tools.ShadowMatch;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * 就是个判断expression是否能匹配到对应的类和方法的
 */
public class AspectJExpressionPointcut implements Pointcut, ClassFilter,MethodMatcher {
    private PointcutParser pointcutParser;//用来帮助判断匹配的工具类
    private String expression;//切点通知的表达式
    private PointcutExpression pointcutExpression;//分析expression得到的类，可以对相应的方法和类进行判断
    private static final Set<PointcutPrimitive>DEFAULT_SUPPORTED_PRIMITIVES=new HashSet<>();
    static{
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.ARGS);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.REFERENCE);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.THIS);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.TARGET);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.WITHIN);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ANNOTATION);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_WITHIN);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ARGS);
        DEFAULT_SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_TARGET);
    }
    public AspectJExpressionPointcut(){
        this(DEFAULT_SUPPORTED_PRIMITIVES);
    }
    public AspectJExpressionPointcut(Set<PointcutPrimitive> supportedPrimitives){
        pointcutParser = PointcutParser.
                 getPointcutParserSupportingSpecifiedPrimitivesAndUsingContextClassloaderForResolution(supportedPrimitives);
    }
    /**
     * 使用 AspectJ Expression 匹配类
     * @param targetClass
     * @return成功匹配返回 true，否则返回 false
     */
    @Override
    public Boolean matchers(Class targetClass) throws Exception {
        checkReadyToMatch();
        return pointcutExpression.couldMatchJoinPointsInType(targetClass);
    }

    /**
     * 判断相应的expression是否匹配相应的方法
     * @param method
     * @param beanClass
     * @return
     */
    @Override
    public Boolean matchers(Method method, Class beanClass) {
        checkReadyToMatch();
        ShadowMatch shadowMatch = pointcutExpression.matchesMethodExecution(method);

        if(shadowMatch.alwaysMatches()){
            return true;
        }else if(shadowMatch.neverMatches()){//这里应该可以删掉
            return false;
        }
        return false;
    }

    /**
     * 将expression转化为pointcutExpression
     */
    private void checkReadyToMatch(){
        if(getExpression() == null){
            throw new IllegalStateException("no expression for property");
        }
        if(pointcutExpression == null){
            pointcutExpression = pointcutParser.parsePointcutExpression(expression);
        }
    }
    @Override
    public ClassFilter getClassFilter() {
        return this;
    }

    @Override
    public MethodMatcher getMethodMatcher() {
        return this;
    }



    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }


}
