package uz.davrbank.getoutsideloanservice.aggregate.service;

import java.io.IOException;
import java.util.List;

public interface FileGenerator<T> {

    byte[] generate(List<T> data, String username, String reportPeriod) throws IOException;
}
