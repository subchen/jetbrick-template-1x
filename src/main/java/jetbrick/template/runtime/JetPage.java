package jetbrick.template.runtime;

public abstract class JetPage {

    //  入口函数
    public abstract void render(JetRuntimeContext context) throws Throwable;

    public abstract String getName();
}
