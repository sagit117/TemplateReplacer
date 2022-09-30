package ru.axel.template.replacer;

import ru.axel.creator.instances.CreateInstanceException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) throws IOException, InvocationTargetException, InstantiationException, IllegalAccessException, CreateInstanceException {
        var file = Main.class.getResource("/templates/index.html");

        HashMap<String, Object> data = new HashMap<>();
        data.put("title", "test title");
        data.put("script", "<script src=\"\"/>");

        var template = TemplateReplacer.templating(file, data);

        System.out.println(template);
    }
}