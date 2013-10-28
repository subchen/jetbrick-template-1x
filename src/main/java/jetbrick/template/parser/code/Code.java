package jetbrick.template.parser.code;

/**
 * Visitor 模式的返回值，用来返回翻译成的源代码样式
 */
public interface Code {

    public static final int INDENT_SIZE = 2;

    public String getSource();

}
