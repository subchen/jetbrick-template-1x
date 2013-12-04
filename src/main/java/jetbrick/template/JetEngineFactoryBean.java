/**
 * jetbrick-template
 * http://subchen.github.io/jetbrick-template/
 *
 * Copyright 2010-2013 Guoqiang Chen. All rights reserved.
 * Email: subchen@gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jetbrick.template;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Properties;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

/**
 * <p>你可以按照以下方式配置JetEngine在Spring上下文的实例(单例) </p>
 * <p><strong>注意：</strong>当同时指定configFile和configProperties时，configProperties中的配置会<strong>覆盖</strong>configFile中的配置</p>
 *
 * <pre>
 * &lt;bean id="jetEngine" class="jetbrick.template.JetEngineFactoryBean" /&gt;
 *
 * &lt;bean id="jetEngine" class="jetbrick.template.JetEngineFactoryBean"&gt;
 *     &lt;property name="configFile" value="classpath:META-INF/jetbrick-template.properties" /&gt;
 * &lt;/bean&gt;
 *
 * &lt;bean id="jetEngine" class="jetbrick.template.JetEngineFactoryBean"&gt;
 *     &lt;property name="configFile" value="file:/path/to/jetbrick-template.properties" /&gt;
 * &lt;/bean&gt;
 *
 * &lt;bean id="jetEngine" class="jetbrick.template.JetEngineFactoryBean"&gt;
 *     &lt;property name="configProperties"&gt;
 *         &lt;props&gt;
 *             &lt;prop key="compile.debug"&gt;true&lt;/prop&gt;
 *             ...
 *         &lt;/props&gt;
 *     &lt;/property&gt;
 * &lt;/bean&gt;
 * </pre>
 *
 * @author 应卓(yingzhor@gmail.com)
 */
public class JetEngineFactoryBean implements FactoryBean<JetEngine>, InitializingBean {
    private JetEngine singleton = null;
    private Resource configFile = null;
    private Properties configProperties = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (configFile == null && configProperties == null) {
            singleton = JetEngine.create();
            return;
        }

        Properties effectProps = new Properties();
        if (configFile != null) {
            check(configFile.getFile());
            effectProps.load(configFile.getInputStream());
        }
        if (configProperties != null) {
            effectProps.putAll(configProperties);
        }

        singleton = JetEngine.create(effectProps);
    }

    // 配置有效性检验，如果配置不正确抛出异常使spring容器启动快速失败
    private void check(File file) throws Exception {
        // 配置文件存在但是是一个目录不是文件
        if (file.exists() && file.isDirectory()) {
            String msg = "config file exists but it is a directory.";
            throw new FileNotFoundException(msg);
        }
    }

    @Override
    public JetEngine getObject() throws Exception {
        return singleton;
    }

    @Override
    public Class<?> getObjectType() {
        return JetEngine.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

    public Resource getConfigFile() {
        return configFile;
    }

    public void setConfigFile(Resource configFile) {
        this.configFile = configFile;
    }

    public Properties getConfigProperties() {
        return configProperties;
    }

    public void setConfigProperties(Properties configProperties) {
        this.configProperties = configProperties;
    }
}
