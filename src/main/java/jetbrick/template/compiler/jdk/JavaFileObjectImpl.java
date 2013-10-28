package jetbrick.template.compiler.jdk;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import javax.tools.*;
import jetbrick.template.utils.*;

/**
 * {@link FileObject}和{@link JavaFileObject}的一个实现，它能持有java源代码或编译后的class。这个类可以用于：
 * <ol>
 * <li>存放需要传递给编译器的源码，这时使用的是{@link JavaFileObjectImpl#JavaFileObjectImpl(String, CharSequence)}构造器。</li>
 * <li>存放编译器编译完的byte code，这是使用的是{@link JavaFileObjectImpl#JavaFileObjectImpl(String, JavaFileObject.Kind)}</li>
 * </ol>
 */
final class JavaFileObjectImpl extends SimpleJavaFileObject {
    /** 如果kind == CLASS, 存储byte code，可以通过{@link #openInputStream()}得到 */
    private UnsafeByteArrayOutputStream byteCode;

    /** 如果kind == SOURCE, 存储源码 */
    private final CharSequence source;

    /**
     * 创建持有源码的实例
     */
    JavaFileObjectImpl(final String baseName, final CharSequence source) {
        super(toURI(baseName + ".java"), Kind.SOURCE);
        this.source = source;
    }

    /**
     * 创建持有二进制byte code的实例
     */
    JavaFileObjectImpl(final String name, final Kind kind) {
        super(toURI(name), kind);
        source = null;
    }

    @Override
    public CharSequence getCharContent(final boolean ignoreEncodingErrors) throws UnsupportedOperationException {
        if (source == null) {
            throw new UnsupportedOperationException("getCharContent()");
        }
        return source;
    }

    @Override
    public InputStream openInputStream() {
        return new UnsafeByteArrayInputStream(byteCode.toByteArray());
    }

    @Override
    public OutputStream openOutputStream() {
        return (byteCode = new UnsafeByteArrayOutputStream());
    }

    protected static URI toURI(String uri) {
        try {
            return new URI(uri);
        } catch (URISyntaxException e) {
            throw ExceptionUtils.uncheck(e);
        }
    }
}
