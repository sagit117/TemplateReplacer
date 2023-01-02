package ru.axel.template.replacer;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TemplateReplacer {
    private static final Pattern pattern = Pattern.compile("[{]{2}([\\w|d]+)[}]{2}");
    public static @NotNull String templating(
        @NotNull List<String> lines,
        HashMap<String, Object> data
    ) {
        final StringBuilder body = new StringBuilder();

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

    public static @NotNull String templating(
        String line,
        HashMap<String, Object> data
    ) {
        final Matcher matcher = pattern.matcher(line);
        String replacementLine = line;

        if (matcher.find()) {
            for (int i = 1; i < matcher.groupCount() + 1; i++) {
                final var value = data.get(matcher.group(i));

                replacementLine = replacementLine
                    .replaceAll("\\{\\{" + matcher.group(i) + "}}", value != null ? value.toString() : "");
            }
        }

        return replacementLine;
    }
}
