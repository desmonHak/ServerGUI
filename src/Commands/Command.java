package src.Commands;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Command {
    public String     comamand;
    public String           id;
    public List<String> params;

    JFrame     windows;

    public Command(String comamand, JFrame windows) {
        this.comamand = comamand;

        String[] commands = this.comamand.split(":\\s*");
        for (String command : commands) {
            // Analizar cada comando
            Pattern commandPattern = Pattern.compile("(\\w+)<(.*)>");
            Matcher matcher = commandPattern.matcher(command);
            if (matcher.find()) {
                this.id = matcher.group(1);
                params = getCompositeParameters(matcher.group(2));
            }
        }
        this.windows = windows;
    }

    @Override
    public String toString() {
        return "%s<%s>".formatted(this.id, this.params);
    }

    private List<String> getCompositeParameters(String params) {
        List<String> paramList = new ArrayList<>();
        StringBuilder currentParam = new StringBuilder();
        int parenthesesCount = 0;

        for (char c : params.toCharArray()) {
            if (c == ',' && parenthesesCount == 0) {
                paramList.add(currentParam.toString().trim());
                currentParam = new StringBuilder();
            } else {
                currentParam.append(c);
                if (c == '(') parenthesesCount++;
                if (c == ')') parenthesesCount--;
            }
        }
        paramList.add(currentParam.toString().trim());
        return  paramList;
    }

    public Objects exec(){
        return null;
    }
}
