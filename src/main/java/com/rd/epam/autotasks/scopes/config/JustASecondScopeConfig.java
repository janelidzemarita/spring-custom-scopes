package com.rd.epam.autotasks.scopes.config;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.Scope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for creating a custom "justASecond" scope in Spring.
 */
@Configuration
public class JustASecondScopeConfig {

    /**
     * Creates a BeanFactoryPostProcessor that registers the "justASecond" custom scope in the Spring application context.
     *
     * @return BeanFactoryPostProcessor instance
     */
    @Bean
    public static BeanFactoryPostProcessor beanFactoryPostProcessor() {
        return new BeanFactoryPostProcessor() {

            /**
             * Custom scope implementation for "justASecond".
             */
            class JustASecondScope implements Scope {

                private final Map<String, Object> scopedObjects = new HashMap<>();
                private final Map<String, Thread> threads = new HashMap<>();

                @Override
                public Object get(String s, ObjectFactory<?> objectFactory) {
                    if (threads.get(s) == null || !threads.get(s).isAlive()) {
                        scopedObjects.put(s, objectFactory.getObject());
                        threads.put(s, new Thread(new OneSecondRunnable(s)));
                        threads.get(s).start();
                    }
                    return scopedObjects.get(s);
                }

                @Override
                public Object remove(String s) {
                    return null;  // Not needed for this assignment
                }

                @Override
                public void registerDestructionCallback(String s, Runnable runnable) {
                    // Not needed for this assignment
                }

                @Override
                public Object resolveContextualObject(String s) {
                    return null;  // Not needed for this assignment
                }

                @Override
                public String getConversationId() {
                    return null;  // Not needed for this assignment
                }

                /**
                 * Runnable class to simulate the one-second expiration of bean instances.
                 */
                class OneSecondRunnable implements Runnable {

                    public String name;

                    public OneSecondRunnable(String name) {
                        this.name = name;
                    }

                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void postProcessBeanFactory(ConfigurableListableBeanFactory configurableListableBeanFactory) throws BeansException {
                configurableListableBeanFactory.registerScope("justASecond", new JustASecondScope());
            }
        };
    }
}
