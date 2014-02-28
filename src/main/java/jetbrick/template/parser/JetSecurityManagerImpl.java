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
package jetbrick.template.parser;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.AccessControlException;
import java.util.*;
import jetbrick.template.JetEngine;
import jetbrick.template.JetSecurityManager;
import jetbrick.template.utils.IoUtils;
import jetbrick.template.utils.StringUtils;

/**
 * 默认实现的安全管理器.
 *
 * @since 1.2.0
 * @author Guoqiang Chen
 */
public class JetSecurityManagerImpl implements JetSecurityManager {
    private Set<String> blacklist;
    private Set<String> whitelist;

    @Override
    public void initialize(JetEngine engine) {
        blacklist = new HashSet<String>();
        whitelist = new HashSet<String>();

        String file = engine.getConfig().getSecurityManagerFile();
        if (file != null) {
            String fileContent = IoUtils.toString(new File(file), "utf-8");
            String[] lines = StringUtils.split(fileContent, '\n');
            for (String line : lines) {
                addList(line);
            }
        }
        List<String> namelist = engine.getConfig().getSecurityManagerNamelist();
        for (String line : namelist) {
            addList(line);
        }
    }

    private void addList(String line) {
        line = line.trim();
        if (line.length() == 0) {
            return;
        }
        char c = line.charAt(0);
        if (c == '+') {
            whitelist.add(line.substring(1));
        } else if (c == '-') {
            blacklist.add(line.substring(1));
        } else {
            whitelist.add(line);
        }
    }

    /**
     * 是否允许进行访问.
     *
     * <p>
     * 其中参数 name 的格式.
     * <ul>
     * <li>java.lang：整个 package</li>
     * <li>java.lang.String：整个 Class</li>
     * <li>java.lang.String.&lt;init&gt;：构造函数</li>
     * <li>java.lang.Integer.MAX_VALUE ：字段</li>
     * <li>java.lang.Integer.valueOf：方法</li>
     * </ul>
     * </p>
     * <p>默认是白名单，前面加 + 代表白名单，- 代表黑名单</p>
     */
    private void checkMemberAccess(String fullName) {
        String name = fullName;
        do {
            if (whitelist.contains(name)) {
                return;
            }
            if (blacklist.contains(name)) {
                throw new AccessControlException("access denied for \'" + fullName + "\' because of \'" + name + "\' is in blacklist.");
            }

            int ipos = name.lastIndexOf('.');
            if (ipos == -1) break;
            name = name.substring(0, ipos);
        } while (true);
    }

    @Override
    public void checkMemberAccess(Class<?> klass) {
        checkMemberAccess(klass.getName());
    }

    @Override
    public void checkMemberAccess(Method method) {
        checkMemberAccess(method.getDeclaringClass().getName() + '.' + method.getName());
    }

    @Override
    public void checkMemberAccess(Field field) {
        checkMemberAccess(field.getDeclaringClass().getName() + '.' + field.getName());
    }
}
