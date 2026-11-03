package uz.davrbank.bustreasury.service.cases.user;

import org.springframework.stereotype.Service;
import uz.davrbank.bustreasury.aggregate.model.Template;
import uz.davrbank.bustreasury.repository.TemplateRepository;
import uz.davrbank.bustreasury.util.CurrencyType;
import uz.davrbank.bustreasury.util.OperationType;

@Service
public class TemplateResolverService {

    private final TemplateRepository templateRepository;

    public TemplateResolverService(TemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    public Long resolve (CurrencyType currencyType , OperationType operations){
        return templateRepository.findByCurrencyAndOperation(currencyType,operations)
                .map(Template::getId)
                .orElseThrow(() -> new IllegalStateException("Template not found for currency: " + currencyType + " and operation: " + operations));
    }
}
