package soap.humo_gateway.dto.annotations;

import lombok.RequiredArgsConstructor;
import lombok.var;
import soap.humo_gateway.dto.property.HumoCredentials;
import soap.humo_gateway.entity.Bins;
import soap.humo_gateway.exception.CardTypeInCorrect;
import soap.humo_gateway.service.BinsService;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;

@RequiredArgsConstructor
public class HumoRegexValidator implements ConstraintValidator<HumoRegex, Object> {
    private final HumoCredentials humoCredentials;
    private final BinsService binsService;

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        if (o == null || !o.toString().matches("\\d{16}"))
            throw new CardTypeInCorrect("Card is null");

        var bin = o.toString().substring(0, 8);

        if (!isValidCreditCardNumber(o.toString()))
            throw new CardTypeInCorrect("This card type incorrect. Checked by Luhn algorithm");

        if (binsService.findAllBanks().stream().noneMatch(z -> z.getBinCode().equals(bin)))
            throw new CardTypeInCorrect("This card bin not found from binList");

        if (binsService.findAllBanks().stream().noneMatch(Bins::getIsPossible))
            throw new CardTypeInCorrect("Card: " + o + " is Juridical card. You can't do operations with this card Type");

        return o.toString().matches(humoCredentials.getPanRegex());
    }

    public Boolean isValidCreditCardNumber(String cardNumber) {
        int[] cardIntArray = new int[cardNumber.length()];

        for (int i = 0; i < cardNumber.length(); i++) {
            char c = cardNumber.charAt(i);
            cardIntArray[i] = Integer.parseInt("" + c);
        }

        for (int i = cardIntArray.length - 2; i >= 0; i = i - 2) {
            int num = cardIntArray[i];
            num = num * 2;  // step 1

            if (num > 9) {
                num = num % 10 + num / 10;  // step 2
            }
            cardIntArray[i] = num;
        }
        int sum = sumDigits(cardIntArray);  // step 3
        return sum % 10 == 0;
    }

    public static int sumDigits(int[] arr) {
        return Arrays.stream(arr).sum();
    }
}
