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

import jetbrick.template.*;
import jetbrick.template.utils.ExceptionUtils;
import jetbrick.template.utils.UnsafeCharArrayWriter;

public abstract class JetTagContext implements JetContextAware {
    public static final Class<?>[] CLASS_ARRAY = { JetTagContext.class };
    private final JetPageContext ctx;

    public JetTagContext(JetPageContext ctx) {
        this.ctx = ctx;
    }

    public String getBodyContent() {
        UnsafeCharArrayWriter out = new UnsafeCharArrayWriter();
        String encoding = ctx.getEngine().getConfig().getOutputEncoding();
        try {
            render(ctx.getContext(), JetWriter.create(out, encoding));
        } catch (Throwable e) {
            throw ExceptionUtils.uncheck(e);
        }
        return out.toString();
    }

    public void writeBodyContent() {
        try {
            render(ctx.getContext(), ctx.getWriter());
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

    @Override
    public JetContext getContext() {
        return ctx.getContext();
    }

    // 专门给 tag 内部用的 ctx
    protected JetPageContext getPageContext(JetWriter out) {
        if (out == ctx.getWriter()) {
            return ctx;
        } else {
            return new JetPageContext(ctx.getTemplate(), ctx.getContext(), out);
        }
    }

    protected abstract void render(final JetContext context, final JetWriter out) throws Throwable;
}
