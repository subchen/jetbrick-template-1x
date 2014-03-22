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
package jetbrick.template.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicLong;
import jetbrick.template.utils.UnsafeByteArrayInputStream;

/**
 * 以源码形式存在的资源.
 *
 * @since 1.1.3
 * @author Guoqiang Chen
 */
public class SourceCodeResource extends Resource {
    private static final String ENCODING = "utf-8";
    private static AtomicLong index = new AtomicLong();
    private final String source;

    public SourceCodeResource(String source) {
        super("/unknown/file." + index.incrementAndGet(), ENCODING);
        this.source = source;
    }

    @Override
    public String getAbsolutePath() {
        return "(unknown)";
    }

    @Override
    public long lastModified() {
        return 0;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new UnsafeByteArrayInputStream(source.getBytes(ENCODING));
    }

    @Override
    public char[] getSource() {
        return source.toCharArray();
    }

    @Override
    public char[] getSource(String encoding) {
        return source.toCharArray();
    }
}
