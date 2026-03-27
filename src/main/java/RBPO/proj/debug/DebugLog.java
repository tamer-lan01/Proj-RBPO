package RBPO.proj.debug;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashMap;
import java.util.Map;

public final class DebugLog {
    private static final Path LOG_PATH = Path.of("debug-a0eb27.log");
    private static final String SESSION_ID = "a0eb27";

    private DebugLog() {
    }

    public static void log(String runId, String hypothesisId, String location, String message, Map<String, Object> data) {
        try {
            Map<String, Object> payload = new LinkedHashMap<>();
            payload.put("sessionId", SESSION_ID);
            payload.put("runId", runId);
            payload.put("hypothesisId", hypothesisId);
            payload.put("location", location);
            payload.put("message", message);
            payload.put("data", data);
            payload.put("timestamp", System.currentTimeMillis());
            Files.writeString(
                    LOG_PATH,
                    toJson(payload) + "\n",
                    StandardOpenOption.CREATE,
                    StandardOpenOption.WRITE,
                    StandardOpenOption.APPEND
            );
        } catch (Exception ignored) {
            // Never break app flow due to debug logging
        }
    }

    private static String toJson(Object obj) {
        if (obj == null) return "null";
        if (obj instanceof String s) return "\"" + escape(s) + "\"";
        if (obj instanceof Number || obj instanceof Boolean) return String.valueOf(obj);
        if (obj instanceof Map<?, ?> m) {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            boolean first = true;
            for (Map.Entry<?, ?> e : m.entrySet()) {
                if (!(e.getKey() instanceof String)) continue;
                if (!first) sb.append(",");
                first = false;
                sb.append(toJson(e.getKey()));
                sb.append(":");
                sb.append(toJson(e.getValue()));
            }
            sb.append("}");
            return sb.toString();
        }
        return toJson(String.valueOf(obj));
    }

    private static String escape(String s) {
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
