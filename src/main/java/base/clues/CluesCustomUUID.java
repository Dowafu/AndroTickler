package base.clues;

import java.util.List;

public class CluesCustomUUID {
    public String UUID;
    public String company;
    public String UUID_name;
    public String UUID_purpose;
    public List<String> UUID_usage_array;
    public List<CluesEvidence> evidence_array;
    public String parent_UUID;

    public void print() {
        System.out.println("        Clues-UUID: " + UUID);
        System.out.println("         - Company: " + company);
        System.out.println("         - Name: " + UUID_name);
        System.out.println("         - Purpose: " + UUID_purpose);
        System.out.println("         - Usage: " + (UUID_usage_array != null ? String.join(", ", UUID_usage_array) : "None"));
        // if (evidence_array != null) {
        //     System.out.println("    Evidence:");
        //     for (CluesEvidence ev : evidence_array) {
        //         System.out.println("    - URL: " + ev.URL);
        //         System.out.println("    - Description: " + ev.description);
        //         System.out.println("    - Submitter: " + ev.submitter);
        //     }
        // }
        if (parent_UUID != null) {
            System.out.println("         - Parent UUID: " + parent_UUID);
        }
    }
}
