package jetbrick.template.runtime;

public abstract class JetPage {

    //  入口函数
    public abstract void render(JetPageContext ctx) throws Throwable;

    public abstract String getName();
}
