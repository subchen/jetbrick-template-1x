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
package jetbrick.template.compiler.spi.memory;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.tools.JavaFileObject;
import jetbrick.template.utils.ExceptionUtils;
import jetbrick.template.utils.IoUtils;

/** {@link ClassLoader}的一个实现，它map类名和JavaFileObjectImpl的实例。本类在{@link MemoryJdkCompiler}和{@link FileManagerImpl}中被使用。 */
final class ClassLoaderImpl extends ClassLoader {
    private final Map<String, JavaFileObjectImpl> classes = new HashMap<String, JavaFileObjectImpl>();

    ClassLoaderImpl(final ClassLoader parentClassLoader) {
        super(parentClassLoader);
    }

    protected void add(String qualifiedClassName, JavaFileObjectImpl javaFile) {
        classes.put(qualifiedClassName, javaFile);
    }

    @Override
    protected Class<?> findClass(final String qualifiedClassName) throws ClassNotFoundException {
        JavaFileObject file = classes.remove(qualifiedClassName);
        if (file != null) {
            InputStream is = null;
            try {
                is = file.openInputStream();
                byte[] bytes = IoUtils.toByteArray(is);
                return defineClass(qualifiedClassName, bytes, 0, bytes.length);
            } catch (IOException e) {
                throw ExceptionUtils.uncheck(e);
            } finally {
                IoUtils.closeQuietly(is);
            }
        }
        // Workaround in Java 6. see http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6434149
        try {
            return Class.forName(qualifiedClassName);
        } catch (ClassNotFoundException e) {
            // do nothing
        }
        return super.findClass(qualifiedClassName);
    }
}
