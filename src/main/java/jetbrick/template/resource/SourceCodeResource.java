package jetbrick.template.resource;

import java.util.Random;

public class SourceCodeResource extends Resource {
    private final String source;

    public SourceCodeResource(String source) {
        super("unknown_file_" + new Random().nextInt());
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
    public char[] getSource() {
        return source.toCharArray();
    }

    @Override
    public char[] getSource(String encoding) {
        return source.toCharArray();
    }
}
