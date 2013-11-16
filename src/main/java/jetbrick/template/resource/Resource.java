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
package jetbrick.template.resource;

public abstract class Resource {
    private final String name;
    private final String qualifiedClassName;

    public Resource(String name) {
        this.name = name;
        this.qualifiedClassName = doGetQualifiedClassName();
    }

    public String getName() {
        return name;
    }

    public String getQualifiedClassName() {
        return qualifiedClassName;
    }

    public String getPackageName() {
        int pos = qualifiedClassName.lastIndexOf('.');
        return pos < 0 ? null : qualifiedClassName.substring(0, pos);
    }

    public String getClassName() {
        int pos = qualifiedClassName.lastIndexOf('.');
        return pos < 0 ? qualifiedClassName : qualifiedClassName.substring(pos + 1);
    }

    public abstract String getAbsolutePath();

    public abstract long lastModified(); // 读取文件最后修改时间

    public abstract char[] getSource(); // 读取模板内容

    public abstract char[] getSource(String encoding); // 读取非模板的资源

    // 返回一个用于生成Template类的完整类名
    private String doGetQualifiedClassName() {
        char[] cs = name.toCharArray();
        for (int i = 0; i < cs.length; i++) {
            char c = cs[i];
            if (c == '/' || c == '\\') {
                cs[i] = '.';
            } else if (c < '0' || (c > '9' && c < 'A') || (c > 'Z' && c < 'a') || c > 'z') {
                cs[i] = '_';
            }
        }
        if (cs.length > 0 && cs[0] == '.') {
            return new String(cs, 1, cs.length - 1); // 去掉第一个 "."
        } else {
            return new String(cs);
        }
    }
}
