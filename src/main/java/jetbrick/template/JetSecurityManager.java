/**
 * jetbrick-template
 * http://subchen.github.io/jetbrick-template/
 *
 * Copyright 2010-2014 Guoqiang Chen. All rights reserved.
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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 对在模板中调用类，方法和访问字段进行安全管理.
 *
 * @since 1.2.0
 * @author Guoqiang Chen
 */
public interface JetSecurityManager {

    /**
     * 初始化 JetSecurityManager.
     */
    public void initialize(JetEngine engine);

    public void checkMemberAccess(Class<?> klass);

    public void checkMemberAccess(Method method);

    public void checkMemberAccess(Field field);
}
