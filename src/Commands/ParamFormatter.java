package src.Commands;

import java.util.ArrayList;
import java.util.List;

public class ParamFormatter {
    private final List<String> params;

    ParamFormatter(List<String> params) {
        this.params = params;
    }

    public String asString(int index) {
        String param = params.get(index);
        if (param.startsWith("\"") && param.endsWith("\"")) {
            return param.substring(1, param.length() - 1);
        }
        return param;
    }

    public int asInt(int index) {
        return Integer.parseInt(params.get(index));
    }

    public boolean asBoolean(int index) {
        return Boolean.parseBoolean(params.get(index));
    }

    public float asFloat(int index) {
        return Float.parseFloat(params.get(index));
    }

    public ArrayList<String> asList(int index) {
        String param = params.get(index).replaceAll("[()]", ""); // Eliminar paréntesis
        String[] parts = param.split(","); // Dividir por comas
        ArrayList<String> result = new ArrayList<>();
        for (String part : parts) {
            result.add(part.trim());
        }
        return result;
    }


}
