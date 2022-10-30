package ru.axel.template.replacer;

import org.jetbrains.annotations.NotNull;
import ru.axel.fileloader.FileLoader;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TemplateReplacer {
    private static @NotNull List<String> getFileLines(
        String path
    ) throws IOException, URISyntaxException {
        final FileLoader loader = new FileLoader(path);
        return loader.getFileData();
    }
    private static @NotNull List<String> getFileLines(
        URL path
    ) throws IOException, URISyntaxException {
        final FileLoader loader = new FileLoader(path);
        return loader.getFileData();
    }

    public static @NotNull String templating(
        URL path,
        HashMap<String, Object> data
    ) throws IOException, URISyntaxException {
        final StringBuilder body = new StringBuilder();
        final var lines = getFileLines(path);
        final Pattern pattern = Pattern.compile("[{]{2}([\\w|d]+)[}]{2}");

        lines.forEach(line -> {
            final Matcher matcher = pattern.matcher(line);

            if (matcher.find()) {
                String replacementLine = line;

                for (int i = 1; i < matcher.groupCount() + 1; i++) {
                    final var value = data.get(matcher.group(i));

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
