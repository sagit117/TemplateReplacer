package ru.axel.template.replacer;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.axel.creator.instances.CreateInstanceException;
import ru.axel.creator.instances.CreatorInstances;
import ru.axel.fileloader.FileLoader;
import ru.axel.fileloader.FileLoaderImpl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TemplateReplacer {
    private static final Logger logger = LoggerFactory.getLogger(TemplateReplacer.class);
    private static Class<? extends FileLoader> fileLoaderClass = FileLoaderImpl.class;

    public static void setFileLoader(Class<? extends FileLoader> fileLoaderClass) {
        TemplateReplacer.fileLoaderClass = fileLoaderClass;
    }

    private static @NotNull List<String> getFileLines(
        String path
    ) throws IOException, InvocationTargetException, InstantiationException, IllegalAccessException, CreateInstanceException {
        FileLoader loader = CreatorInstances.createInstance(fileLoaderClass, path);
        return loader.getFileData();
    }
    private static @NotNull List<String> getFileLines(
        URL path
    ) throws IOException, InvocationTargetException, InstantiationException, IllegalAccessException, CreateInstanceException {
        FileLoader loader = CreatorInstances.createInstance(fileLoaderClass, path);
        System.out.println(loader.getMineFile());
        return loader.getFileData();
    }

    public static @NotNull String templating(
        URL path, HashMap<String, Object> data
    ) throws IOException, InvocationTargetException, InstantiationException, IllegalAccessException, CreateInstanceException {
        StringBuilder body = new StringBuilder();

        var lines = getFileLines(path);
        logger.debug("Файл загружен, содержит " + lines.size() + " строк.");

        Pattern pattern = Pattern.compile("[{]{2}([\\w|d]+)[}]{2}");

        lines.forEach(line -> {
            Matcher matcher = pattern.matcher(line);

            if (matcher.find()) {
                String replacementLine = line;
                logger.debug("Строка для разбора: " + line);

                for (int i = 1; i < matcher.groupCount() + 1; i++) {
                    logger.debug("Найден паттерн: " + matcher.group(i));
                    var value = data.get(matcher.group(i));
                    logger.debug("Найдено значение для группы " + i + " " + value);

                    replacementLine = replacementLine
                        .replaceAll("\\{\\{" + matcher.group(i) + "}}", value != null ? value.toString() : "");
                }

                body.append(replacementLine);
            } else {
                body.append(line);
            }
        });

        return body.toString();
    }
}
