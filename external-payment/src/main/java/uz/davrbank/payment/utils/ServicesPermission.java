package uz.davrbank.payment.utils;

import java.util.HashMap;
import java.util.List;


public class ServicesPermission {

    public static List<String> getUserService(String username) {
        var map = new HashMap<String, List<String>>();
        map.put("deals", List.of("469469101", "516105651", "814421601", "814421602", "814421603", "814421604", "814421605", "814421606", "814421607", "814421608"));
        map.put("davrsite", List.of("562996601"));
        map.put("probeautyaes", List.of("835394401"));
        map.put("HGT", List.of("865453202", "890982451"));
        return map.get(username);
    }
}
