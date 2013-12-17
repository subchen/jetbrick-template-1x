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

/**
 * #for 内部状态指示器.
 * 
 * @author Guoqiang Chen
 */
public interface JetForStatus {

    /**
     * foreach 计数器，从 1 开始
     */
    public int getIndex();

    /**
     * 获取循环总数.
     * 
     * <p>如果对 Iterator 进行循环，或者对非 Collection 的 Iterable 进行循环，则返回 -1。<p>
     * 
     * @since 1.1.3
     */
    public int getSize();

    /**
     * 是否第一个元素.
     * 
     * @since 1.1.3
     */
    public boolean isFirst();

    /**
     * 是否最后一个元素.
     * 
     * @since 1.1.3
     */
    public boolean isLast();
}
