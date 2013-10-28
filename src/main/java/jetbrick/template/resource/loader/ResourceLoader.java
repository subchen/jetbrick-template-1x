package jetbrick.template.resource.loader;

import jetbrick.template.resource.Resource;

public interface ResourceLoader {

    public void initialize(String basepath, String encoding);

    public Resource load(String name);
}
