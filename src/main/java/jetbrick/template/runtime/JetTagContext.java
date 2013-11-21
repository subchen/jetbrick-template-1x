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
package jetbrick.template.runtime;

import jetbrick.template.JetContext;
import jetbrick.template.JetEngine;
import jetbrick.template.utils.ExceptionUtils;
import jetbrick.template.utils.UnsafeCharArrayWriter;

public abstract class JetTagContext {
    public static final Class<?>[] CLASS_ARRAY = new Class<?>[] { JetTagContext.class };
    private final JetPageContext ctx;

    public JetTagContext(JetPageContext ctx) {
        this.ctx = ctx;
    }

    /**
     * 获取 <code>#tag</code> 的结果文本
     */
    public String getBodyContent() {
        UnsafeCharArrayWriter out = new UnsafeCharArrayWriter();
        String encoding = ctx.getEngine().getConfig().getOutputEncoding();
        try {
            render(JetWriter.create(out, encoding));
        } catch (Throwable e) {
            throw ExceptionUtils.uncheck(e);
        }
        return out.toString();
    }

    /**
     * 直接将 <code>#tag</code> 的内容输出到原始 JetWriter
     */
    public void writeBodyContent() {
        try {
            render(ctx.getWriter());
        } catch (Throwable e) {
            throw ExceptionUtils.uncheck(e);
        }
    }

    public JetPageContext getPageContext() {
        return ctx;
    }

    public JetEngine getEngine() {
        return ctx.getEngine();
    }

    public JetWriter getWriter() {
        return ctx.getWriter();
    }

    public JetContext getContext() {
        return ctx.getContext();
    }

    protected abstract void render(JetWriter out) throws Throwable;
}
