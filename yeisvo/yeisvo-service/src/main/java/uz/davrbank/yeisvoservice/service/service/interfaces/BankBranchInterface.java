package uz.davrbank.yeisvoservice.service.service.interfaces;

import uz.davrbank.yeisvoservice.aggregate.dto.response.spravochniki.BankBranchRes;

import java.util.List;

public interface BankBranchInterface {
   List<BankBranchRes> getBranches(String bankType);

}
