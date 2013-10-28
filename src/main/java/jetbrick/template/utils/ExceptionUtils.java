package jetbrick.template.utils;

public final class ExceptionUtils {

    public static RuntimeException uncheck(Throwable e) {
        if (e instanceof RuntimeException) {
            return (RuntimeException) e;
        }
        return new RuntimeException(e);
    }
}
