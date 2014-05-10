package jetbrick.template;

public class JetxGenerateAppTest {

    //@Test
    public void executeAll() {
        String[] args = new String[] { "-all", "-src", "src/test/resources", "-d", "c:/temp", "-config", "src/test/resources/jetbrick-template.properties" };
        JetxGenerateApp.main(args);
    }

    //@Test
    public void execute() {
        String[] args = new String[] { "-src", "src/test/resources", "-d", "c:/temp", "-config", "src/test/resources/jetbrick-template.properties" };
        JetxGenerateApp.main(args);
    }

    //@Test
    public void help() {
        String[] args = new String[] { "-help" };
        JetxGenerateApp.main(args);
    }

    //@Test
    public void args() {
        String[] args = new String[] { "-all", "-src", "src/test/resources" };
        JetxGenerateApp.main(args);
    }
}
