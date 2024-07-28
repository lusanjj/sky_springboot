package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.time.LocalDateTime;

import static com.sky.constant.AutoFillConstant.*;

/**
 * ClassName: AutoFillAspect
 * Package:com.sky.aspect
 * Description:自定义切面类，实现公共字段自动填充处理逻辑
 * 切面 = 切入点+通知
 * @Author Shane Liu
 * @Create 2024-07-27 10:38 p.m.
 * @Version 1.0
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {
    /*
        切入点，对哪些类的哪些方法进行拦截。
        * com.sky.mapper.*.*(..))
        任何返回类型 包名.任何类，任何方法名，任意参数。

        && @annotation(com.sky.annotation.AutoFill)
        与此同时，还要确保有autofill的注解
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill) ")
    public void autoFillPointCut(){}


    /*
     通知，我们要确保当使用insert 和 update 之前就要让公共字段赋值
     通知类型为before（前置通知），参数要带入切入点符合的方法
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){ //前置通知的方法中需要连接点，哪个方法被拦截到了，以及它们的参数信息
        log.info("开始进行公共字段自动填充。。。");

        //1 获取到当前被拦截的方法上的数据库操作类型
        //方法签名对象
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获得方法注解对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        //获得数据库操作类型
        OperationType operationType = autoFill.value();




        //2获取到当前被拦截的方法的参数--实体对象，employee dish...
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0){
            return;
        }
        Object entity = args[0];

        //3准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        //4根据当前不同的操作类型，为对应的属性通过反射来赋值
        if(operationType == OperationType.INSERT){
            //为四个公共字段都要赋值

            try {
                Method setCreateTime = entity.getClass().getDeclaredMethod(SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(SET_UPDATE_USER, Long.class);

            //通过反射为对象属性赋值

                setCreateTime.invoke(entity,now);
                setCreateUser.invoke(entity,currentId);
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);


            } catch (Exception e) {
                e.printStackTrace();
            }

        }else if(operationType == OperationType.UPDATE){
            //为两个公共字段赋值

            try {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(SET_UPDATE_USER, Long.class);

                //通过反射为对象属性赋值
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
