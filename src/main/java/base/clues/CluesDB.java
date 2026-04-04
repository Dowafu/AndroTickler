// From the CLUES project - https://github.com/darkmentorllc/CLUES_Schema

package base.clues;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.json.JSONArray;
import org.json.JSONObject;

import initialization.TicklerConst;
import initialization.TicklerVars;




public class CluesDB {
    private static Map<String, CluesCustomUUID> uuidIndex = new HashMap<>();

    public CluesDB() throws IOException {
        loadFromFile();
    }

    private void loadFromFile() throws IOException {
        Path cluesfile = Paths.get(TicklerVars.ticklerDir, TicklerConst.CLUESDB_DIR, "CLUES_data.json");
        String content = Files.readString(cluesfile, StandardCharsets.UTF_8);
        JSONArray jsonArray = new JSONArray(content);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            CluesCustomUUID entry = new CluesCustomUUID();
            entry.UUID = obj.optString("UUID", null);
            entry.company = obj.optString("company", null);
            entry.UUID_name = obj.optString("UUID_name", null);
            entry.UUID_purpose = obj.optString("UUID_purpose", null);
            entry.parent_UUID = obj.optString("parent_UUID", null);

            JSONArray usageArray = obj.optJSONArray("UUID_usage_array");
            if (usageArray != null) {
                List<String> usageList = new ArrayList<>();
                for (int j = 0; j < usageArray.length(); j++) {
                    usageList.add(usageArray.getString(j));
                }
                entry.UUID_usage_array = usageList;
            }

            JSONArray evidenceArray = obj.optJSONArray("evidence_array");
            if (evidenceArray != null) {
                List<CluesEvidence> evidenceList = new ArrayList<>();
                for (int j = 0; j < evidenceArray.length(); j++) {
                    JSONObject evObj = evidenceArray.getJSONObject(j);
                    CluesEvidence ev = new CluesEvidence();
                    ev.URL = evObj.optString("URL", null);
                    ev.description = evObj.optString("description", null);
                    ev.submitter = evObj.optString("submitter", null);
                    evidenceList.add(ev);
                }
                entry.evidence_array = evidenceList;
            }

            if (entry.UUID != null) {
                uuidIndex.put(normalizeUuid(entry.UUID), entry);
            }
        }
    }

    private static String normalizeUuid(String uuid) {
		Pattern pattern  = Pattern.compile("\\b(?:[0-9A-Fa-f]{8}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{4}-[0-9A-Fa-f]{12})\\b");
        // |[0-9A-Fa-f]{32}
        Matcher matcher = pattern.matcher(uuid);
        if (matcher.find()) {
            return matcher.group(0).toLowerCase();
        }
        return null;
    }

    public CluesCustomUUID lookupUUID(String uuid) {

        return uuidIndex.get(normalizeUuid(uuid));
    }

    public static boolean checkCluesDb() {
        Path cluesfile = Paths.get(TicklerVars.ticklerDir, TicklerConst.CLUESDB_DIR, "CLUES_data.json");
        return Files.exists(cluesfile);
    }

    public static void downloadCluesDb() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(TicklerConst.CLUESDB_URL))
            .build();
        HttpResponse<String> response =
            client.send(request, BodyHandlers.ofString());
        System.out.println(response.statusCode());
        // System.out.println(response.body());


        if (response.statusCode() != 200) {
            throw new IOException("HTTP error: " + response.statusCode());
        }

        // Store response in file
        Path cluesdir = Paths.get(TicklerVars.ticklerDir, TicklerConst.CLUESDB_DIR);
        Files.createDirectories(cluesdir);
        Path cluesfile = cluesdir.resolve("CLUES_data.json");
        try (FileOutputStream out = new FileOutputStream(cluesfile.toFile())) {
            out.write(response.body().getBytes(StandardCharsets.UTF_8));
        }
    }

}