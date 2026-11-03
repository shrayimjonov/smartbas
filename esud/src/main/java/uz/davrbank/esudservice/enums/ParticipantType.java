package uz.davrbank.esudservice.enums;

import org.springframework.util.StringUtils;

import java.util.Arrays;

public enum ParticipantType {
    CLAIMANT,
    DEFENDANT,
    REPRESENTING_ORG,
    PROSECUTOR,
    ADVOCATE,
    EXPERT,
    OTHER,
    REPRESENTATIVE,
    WITNESS,
    JUDGE,
    CHAIRMAN,
    SECRETARY,
    SPEAKER,
    SPECIALIST,
    TRANSLATOR;

    public static ParticipantType findParticipantTypeByName(final String name) {
        if (!StringUtils.hasText(name)) {
            return null;
        }

        return Arrays.stream(values())
                .filter(lang -> lang.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
