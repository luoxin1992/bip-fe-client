package cn.edu.xmu.bip.inject;

import com.airhacks.afterburner.injection.PresenterFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.Function;

/**
 * 自定义的afterburner.fx依赖注入实现类
 * <p>
 * 增强功能：
 * 1.支持存在循环依赖的Model或Service注入
 * 2.支持类型是接口的属性注入(需在该field上@Implement具体实现类)
 *
 * @author luoxin
 * @version 2017-8-3
 */
public class Injector implements PresenterFactory {
    private static final Logger LOGGER = LoggerFactory.getLogger(Injector.class);

    private static Map<Class<?>, Object> modelsAndServices = new WeakHashMap<>();
    private static Set<Object> presenters = Collections.newSetFromMap(new WeakHashMap<>());

    /**
     * 实例化presenter
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T instantiatePresenter(Class<T> clazz, Function<String, Object> injectionContext) {
        T presenter = registerExistingAndInject((T) defaultInstanceSupplier().apply(clazz));
        LOGGER.info("instantiate presenter {}", presenter);

        //injectionContext来自FXMLView的构造方法，用于View向Presenter注入参数
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Inject.class)) {
                Object value = injectionContext.apply(field.getName());
                if (value != null) {
                    injectIntoField(field, presenter, value);
                    LOGGER.info("inject {} into field {} in presenter {}", value, field, presenter);
                }
            }
        }
        return presenter;
    }

    /**
     * 销毁全部依赖
     */
    public static void forgetAll() {
        modelsAndServices.values().forEach(Injector::destroy);
        modelsAndServices.clear();
        presenters.forEach(Injector::destroy);
        presenters.clear();
    }

    private static Function<Class<?>, Object> defaultInstanceSupplier() {
        return clazz -> {
            try {
                return clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                LOGGER.error("instantiate class {} failed", clazz, e);
                throw new IllegalStateException(e);
            }
        };
    }

    private static <T> T registerExistingAndInject(T instance) {
        T presenter = injectAndInitialize(instance);
        presenters.add(presenter);
        return presenter;
    }

    private static <T> T injectAndInitialize(T product) {
        injectMembers(product);
        initialize(product);
        return product;
    }

    private static void injectMembers(Object instance) {
        Class<?> clazz = instance.getClass();
        injectMembers(clazz, instance);
    }

    private static void injectMembers(Class<?> clazz, Object instance) throws SecurityException {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Inject.class)) {
                Class<?> type = field.getType();
                if (isNotPrimitiveOrString(type)) {
                    //寻找实现类
                    if (type.isInterface() && field.isAnnotationPresent(Implement.class)) {
                        type = field.getAnnotation(Implement.class).name();
                        LOGGER.info("get implementation {} for {}", type, field.getType());
                    }
                    Object value = instantiateModelOrService(type);
                    injectIntoField(field, instance, value);
                }
            }
        }
        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null) {
            injectMembers(superclass, instance);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T instantiateModelOrService(Class<T> clazz) {
        T product = (T) modelsAndServices.get(clazz);
        if (product == null) {
            product = (T) defaultInstanceSupplier().apply(clazz);
            LOGGER.info("instantiate model or service {}", product);

            //实例化的Model或Service先放入缓冲区，再初始化其成员和调用@PostConstruct方法
            modelsAndServices.putIfAbsent(clazz, product);
            product = injectAndInitialize(product);
        }
        return clazz.cast(product);
    }

    private static void injectIntoField(Field field, Object instance, Object value) {
        boolean accessible = field.isAccessible();
        try {
            field.setAccessible(true);
            field.set(instance, value);
            LOGGER.info("inject field {} in {} with value {}", field.getName(), instance.getClass(), value);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            LOGGER.error("inject field {} in {} with value {} failed", field.getName(), instance.getClass(), value, e);
            throw new IllegalStateException(e);
        } finally {
            field.setAccessible(accessible);
        }
    }

    private static boolean isNotPrimitiveOrString(Class<?> type) {
        return !type.isPrimitive() && !type.isAssignableFrom(String.class);
    }

    /**
     * 调用instance中注解@PostConstruct的方法
     */
    private static void initialize(Object instance) {
        Class<?> clazz = instance.getClass();
        invokeMethodWithAnnotation(clazz, instance, PostConstruct.class);
    }

    /**
     * 调用instance中注解@PreDestroy的方法
     */
    private static void destroy(Object instance) {
        Class<?> clazz = instance.getClass();
        invokeMethodWithAnnotation(clazz, instance, PreDestroy.class);
    }

    private static void invokeMethodWithAnnotation(Class<?> clazz, Object instance,
                                                   Class<? extends Annotation> annotation)
            throws IllegalStateException, SecurityException {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(annotation)) {
                boolean accessible = method.isAccessible();
                try {
                    method.setAccessible(true);
                    method.invoke(instance);
                    LOGGER.info("invoke {} method {} in {}", annotation.getName(), method.getName(), clazz);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    LOGGER.error("invoke {} method {} in {} failed", annotation.getName(), method.getName(), clazz, e);
                    throw new IllegalStateException(e);
                } finally {
                    method.setAccessible(accessible);
                }
            }
        }
        Class<?> superclass = clazz.getSuperclass();
        if (superclass != null) {
            invokeMethodWithAnnotation(superclass, instance, annotation);
        }
    }
}
