import ru.axel.template.replacer.Main;
import ru.axel.template.replacer.TemplateReplacer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class TestMain {
    public static void test() throws URISyntaxException, IOException {
        final URL url = Main.class.getResource("/templates/index.html");

        HashMap<String, Object> data = new HashMap<>();
        data.put("title", "test title");
        data.put("script", "<script src=\"\"/>");

        assert url != null;
        var template = TemplateReplacer.templating(Files.readAllLines(Path.of(url.toURI())), data);

        System.out.println(template);
    }
}
