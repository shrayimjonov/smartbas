package uz.davrbank.dto;


public interface DashboardDTO {
    Long getTotalDistinctUsers();               // Total distinct users

    Long getDailyDistinctUsers();               // Distinct users for the given day

    Long getDailyPassedTests();                 // Tests passed on the given day

    Long getDailyFailedTests();                 // Tests failed on the given day

    Long getTotalPassedTests();                 // Total passed tests

    Long getTotalFailedTests();                 // Total failed tests

    Long getDepartmentCount();                  //count number of departments
}


