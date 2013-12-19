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

import java.io.*;
import javax.lang.model.SourceVersion;
import jetbrick.template.utils.ExceptionUtils;
import jetbrick.template.utils.IoUtils;

public abstract class Resource {
    private final String name;
    private final String encoding;
    private final String qualifiedClassName;

    public Resource(String name, String encoding) {
        this.name = name;
        this.encoding = encoding;
        this.qualifiedClassName = doGetQualifiedClassName();
    }

    /**
     * 标准的 name
     */
    public String getName() {
        return name;
    }

    /**
     * 默认编码方式
     */
    public String getEncoding() {
        return encoding;
    }

    /**
     * 完整的类名标识
     */
    public String getQualifiedClassName() {
        return qualifiedClassName;
    }

    /**
     * 包名
     */
    public String getPackageName() {
        int pos = qualifiedClassName.lastIndexOf('.');
        return pos < 0 ? null : qualifiedClassName.substring(0, pos);
    }

    /**
     * 类名
     */
    public String getClassName() {
        int pos = qualifiedClassName.lastIndexOf('.');
        return pos < 0 ? qualifiedClassName : qualifiedClassName.substring(pos + 1);
    }

    /**
     * 获取绝对路径
     */
    public abstract String getAbsolutePath();

    /**
     * 读取最后修改时间
     */
    public abstract long lastModified();

    /**
     * 获取输入流
     * @throws FileNotFoundException
     * @throws IOException
     */
    public abstract InputStream getInputStream() throws IOException;

    /**
     * 读取模板内容
     */
    public char[] getSource() {
        return getSource(encoding);
    }

    /**
     * 读取非模板的资源
     */
    public char[] getSource(String encoding) {
        InputStream is = null;
        try {
            is = getInputStream();
            if (is == null) return null;
            return IoUtils.toCharArray(is, encoding);
        } catch (IOException e) {
            throw ExceptionUtils.uncheck(e);
        } finally {
            IoUtils.closeQuietly(is);
        }
    }

    // 返回一个用于生成Template类的完整类名 (规则同JSP)
    private String doGetQualifiedClassName() {
        StringBuilder sb = new StringBuilder(name.length() + 16);

        String[] identifiers = name.split("/");
        for (int i = 1; i < identifiers.length; i++) { // 跳过第一个 "/"
            String identifier = identifiers[i];
            StringBuilder modifiedIdentifier = new StringBuilder(identifier.length() + 16);

            char c = identifier.charAt(0);
            if (c < 'A' || (c > 'Z' && c < 'a') || c > 'z') {
                modifiedIdentifier.append('_');
            }
            for (int j = 0; j < identifier.length(); j++) {
                c = identifier.charAt(j);
                if ((c >= '0' && c <= '9') || (c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
                    modifiedIdentifier.append(c);
                } else if (c == '.') {
                    modifiedIdentifier.append('_');
                } else {
                    modifiedIdentifier.append('_');
                    modifiedIdentifier.append(Character.forDigit(c >> 12 & 0xF, 16));
                    modifiedIdentifier.append(Character.forDigit(c >> 8 & 0xF, 16));
                    modifiedIdentifier.append(Character.forDigit(c >> 4 & 0xF, 16));
                    modifiedIdentifier.append(Character.forDigit(c & 0xF, 16));
                }
            }
            if (SourceVersion.isKeyword(modifiedIdentifier.toString())) {
                modifiedIdentifier.append('_');
            }
            if (sb.length() > 0) {
                sb.append('.');
            }
            sb.append(modifiedIdentifier);
        }
        return sb.toString();
    }
}
