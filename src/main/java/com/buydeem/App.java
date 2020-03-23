package com.buydeem;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class App {
    public static void main(String[] args) {
        //静态代理 写一个类实现接口，然后持有目标类的引用，重写目标方法
        System.out.println("======== 静态代理 ========");
        UserDaoImpl userDao = new UserDaoImpl();
        StaticUserDao staticUserDao = new StaticUserDao(userDao);
        staticUserDao.save();
        // jdk动态代理 需要目标类实现接口,代理接口上的方法来实现.如果被代理的方法不再接口上,无法实现
        System.out.println("========　jdk动态代理　========");
        // loader -> 代理类的类加载器
        // interfaces -> 代理类要实现的接口列表
        // h -> 代理处理逻辑
        UserDao jdkProxy = (UserDao)Proxy.newProxyInstance(UserDaoImpl.class.getClassLoader(),UserDaoImpl.class.getInterfaces(), new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("开启事务");
                method.invoke(userDao,args);
                System.out.println("提交事务");
                return null;
            }
        });
        System.out.println(jdkProxy.getClass().getName());
        jdkProxy.save();
        // cglib
        System.out.println("========　Cglib动态代理　========");
        MethodInterceptor interceptor = new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                System.out.println("开启事务");
                userDao.save();
                System.out.println("提交事务");
                return null;
            }
        };
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(userDao.getClass());
        enhancer.setCallback(interceptor);
        UserDao cglibUserDao = (UserDao) enhancer.create();
        System.out.println(cglibUserDao.getClass().getName());
        cglibUserDao.save();
    }
}
