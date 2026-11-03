package uz.davrbank.usermanualservice.config;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.options.MutableDataSet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Author: Shukhratjon Rayimjonov
 * Date: 24.11.2025
 * Time: 16.42
 * Project: usermanual-service
 */

@Configuration
public class MarkdownConfiguration {

    @Bean
    public Parser markdownParser() {
        MutableDataSet options = new MutableDataSet();
        return Parser.builder(options).build();
    }

    @Bean
    public HtmlRenderer htmlRenderer() {
        return HtmlRenderer.builder().build();
    }
}
