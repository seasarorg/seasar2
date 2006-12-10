/*
 * Copyright 2004-2006 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.seasar.framework.unit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.Parameterized.Parameters;
import org.seasar.framework.container.S2Container;
import org.seasar.framework.container.factory.S2ContainerFactory;
import org.seasar.framework.util.ResourceUtil;

/**
 * @author taedium
 * 
 */
public class Seasar2 extends Runner {

    public static final String S2JUNIT4_CONFIG_KEY = "org.seasar.framework.unit.s2junit4.config";

    public static final String S2JUNIT4_CONFIG_PATH = "s2junit4config.dicon";

    protected static S2Container configurationContainer;

    protected static Provider provider;

    static {
        configure();
    }

    private final Runner delegate;

    public Seasar2(final Class<?> clazz) throws Exception {
        delegate = createTestClassRunner(clazz);
    }

    protected Runner createTestClassRunner(Class<?> clazz) throws Exception {
        return getProvider().createTestClassRunner(clazz);
    }

    protected static Provider getProvider() {
        return provider;
    }

    protected static void setProvider(final Provider p) {
        provider = p;
    }

    public static void configure() {
        final String configFile = System.getProperty(S2JUNIT4_CONFIG_KEY,
                S2JUNIT4_CONFIG_PATH);
        configure(configFile);
    }

    public static void configure(final String configFile) {
        if (provider == null) {
            provider = new DefaultProvider();
        }

        if (ResourceUtil.isExist(configFile)) {
            configurationContainer = S2ContainerFactory.create(configFile);
            Configurator configurator;
            if (configurationContainer.hasComponentDef(Configurator.class)) {
                configurator = (Configurator) configurationContainer
                        .getComponent(Configurator.class);
            } else {
                configurator = new DefaultConfigurator();
            }
            configurator.configure(configurationContainer);
        }
    }

    public static void dispose() {
        S2TestClassMethodsRunner.dispose();
        provider = null;
        if (configurationContainer != null) {
            configurationContainer.destroy();
        }
        configurationContainer = null;
    }

    @Override
    public Description getDescription() {
        return delegate.getDescription();
    }

    @Override
    public void run(RunNotifier notifier) {
        delegate.run(notifier);
    }

    public interface Configurator {
        void configure(S2Container configurationContainer);
    }

    public static class DefaultConfigurator implements Configurator {

        public void configure(final S2Container configurationContainer) {
            if (configurationContainer.hasComponentDef(Provider.class)) {
                provider = (Provider) configurationContainer
                        .getComponent(Provider.class);
            }
            if (configurationContainer
                    .hasComponentDef(S2TestClassMethodsRunner.Provider.class)) {
                S2TestClassMethodsRunner
                        .setProvider((S2TestClassMethodsRunner.Provider) configurationContainer
                                .getComponent(S2TestClassMethodsRunner.Provider.class));
            }
        }
    }

    public interface Provider {
        Runner createTestClassRunner(Class<?> clazz) throws Exception;
    }

    public static class DefaultProvider implements Provider {

        public Runner createTestClassRunner(final Class<?> clazz)
                throws Exception {

            if (hasParameterAnnotation(clazz)) {
                return new S2Parameterized(clazz);
            }
            return new S2TestClassRunner(clazz, new S2TestClassMethodsRunner(
                    clazz));
        }

        protected boolean hasParameterAnnotation(final Class<?> clazz) {
            for (Method each : clazz.getMethods()) {
                if (Modifier.isStatic(each.getModifiers())) {
                    Annotation[] annotations = each.getAnnotations();
                    for (Annotation annotation : annotations) {
                        if (annotation.annotationType() == Parameters.class)
                            return true;
                    }
                }
            }
            return false;
        }
    }
}
