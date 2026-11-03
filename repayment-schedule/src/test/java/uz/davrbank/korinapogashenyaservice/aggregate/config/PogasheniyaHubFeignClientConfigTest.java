package uz.davrbank.korinapogashenyaservice.aggregate.config;

import feign.Request;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PogasheniyaHubFeignClientConfigTest {

    private final PogasheniyaHubFeignClientConfig config = new PogasheniyaHubFeignClientConfig();

    @Test
    void shouldConfigureThirtyMinuteReadTimeoutForHubClient() {
        Request.Options options = config.hubClientRequestOptions();

        assertThat(options.connectTimeoutMillis())
                .isEqualTo(PogasheniyaHubFeignClientConfig.HUB_CONNECT_TIMEOUT_MILLIS);
        assertThat(options.readTimeoutMillis())
                .isEqualTo(PogasheniyaHubFeignClientConfig.HUB_READ_TIMEOUT_MILLIS);
        assertThat(options.isFollowRedirects()).isTrue();
    }
}
