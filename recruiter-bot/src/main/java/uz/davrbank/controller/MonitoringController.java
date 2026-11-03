package uz.davrbank.controller;

import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@RestController
public class MonitoringController {

    private final MetricsEndpoint metrics;
    private static final Path LOG = Path.of("logs/app.log");

    public MonitoringController(MetricsEndpoint metrics) {
        this.metrics = metrics;
    }

    @GetMapping("/api/monitor")
    public Map<String, Object> monitor() {
        return Map.of(
                "cpu", value("system.cpu.usage"),
                "heap", value("jvm.memory.used", "area", "heap"),
                "nonHeap", value("jvm.memory.used", "area", "nonheap"),
                "threads", value("jvm.threads.live"),
                "uptime", value("process.uptime")
        );
    }


    private Double value(String name, String... tags) {
        List<String> formattedTags = null;

        if (tags.length >= 2) {
            formattedTags = List.of(tags[0] + ":" + tags[1]);
        }

        MetricsEndpoint.MetricDescriptor metric =
                metrics.metric(name, formattedTags);

        if (metric == null || metric.getMeasurements().isEmpty())
            return 0.0;

        return metric.getMeasurements().get(0).getValue();
    }



    @GetMapping("/api/logs")
    public List<String> getLogs() throws Exception {
        if (!Files.exists(LOG))
            return List.of("Log fayl topilmadi!");

        return Files.readAllLines(LOG);
    }
}
