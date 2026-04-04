
// From the CLUES project - https://github.com/darkmentorllc/CLUES_Schema

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import initialization.TicklerConst;
import initialization.TicklerVars;


public class CluesCustomUUID {
    public String UUID;
    public String company;
    public String UUID_name;
    public String UUID_purpose;
    public List<String> UUID_usage_array;
    public List<Evidence> evidence_array;
    public String parent_UUID;
}

public class CluesEvidence {
    public String URL;
    public String description;
    public String submitter;
}

public class CluesDB {
    private final Map<String, CustomUUID> uuidIndex = new HashMap<>();

    private Clues(List<CustomUUID> entries) {
        for (CustomUUID e : entries) {
            if (e.UUID != null) {
                uuidIndex.put(normalizeUuid(e.UUID), e);
            }
        }
    }

    public static Clues loadFromFile(String path) throws IOException {
        String json = Files.readString(Path.of(path), StandardCharsets.UTF_8);
        JSONArray arr = new JSONArray(json);

        List<CustomUUID> entries = new ArrayList<>();

        for (int i = 0; i < arr.length(); i++) {
            JSONObject o = arr.getJSONObject(i);

            CustomUUID cu = new CustomUUID();
            cu.UUID = o.getString("UUID");
            cu.company = o.optString("company", null);
            cu.UUID_name = o.optString("UUID_name", null);
            cu.UUID_purpose = o.optString("UUID_purpose", null);
            cu.parent_UUID = o.optString("parent_UUID", null);

            // UUID_usage_array
            JSONArray usageArr = o.optJSONArray("UUID_usage_array");
            if (usageArr != null) {
                List<String> usage = new ArrayList<>();
                for (int j = 0; j < usageArr.length(); j++) {
                    usage.add(usageArr.getString(j));
                }
                cu.UUID_usage_array = usage;
            }

            // evidence_array
            JSONArray evArr = o.optJSONArray("evidence_array");
            if (evArr != null) {
                List<Evidence> evidences = new ArrayList<>();
                for (int j = 0; j < evArr.length(); j++) {
                    JSONObject ev = evArr.getJSONObject(j);
                    Evidence e = new Evidence();
                    e.URL = ev.optString("URL", null);
                    e.description = ev.optString("description", null);
                    e.submitter = ev.optString("submitter", null);
                    evidences.add(e);
                }
                cu.evidence_array = evidences;
            }

            entries.add(cu);
        }

        return new Clues(entries);
    }

    public CustomUUID lookupUUID(String uuid) {
        if (uuid == null) return null;
        return uuidIndex.get(normalizeUuid(uuid));
    }

    private static String normalizeUuid(String uuid) {
        // Lowercase + remove dashes so 32/36-char variants match
        return uuid.toLowerCase(Locale.ROOT).replace("-", "");
    }


    // public void downloadCluesDb() throws IOException, InterruptedException {
    //     HttpClient client = HttpClient.newHttpClient();
    //     HttpRequest request = HttpRequest.newBuilder()
    //         .uri(URI.create(TicklerConst.CLUESDB_URL))
    //         .build();
    //     HttpResponse<String> response =
    //         client.send(request, BodyHandlers.ofString());
    //     System.out.println(response.statusCode());
    //     System.out.println(response.body());


    //     if (response.statusCode() != 200) {
    //         throw new IOException("HTTP error: " + response.statusCode());
    //     }

    //     // Store response in file
    //     Path cluesdir = Paths.get(TicklerVars.TicklerDir, TicklerConst.CLUESDB_DIR);
    //     Files.createDirectories(cluesdir);
    //     Path cluesfile = cluesdir.resolve("CLUES_data.json");
    //     try (FileOutputStream out = new FileOutputStream(file.toFile())) {
    //         out.write(response.body());
    //     }
    // }

}