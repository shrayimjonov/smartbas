package uz.davrbank.yeisvoservice.service.service.interfaces;

import uz.davrbank.yeisvoservice.aggregate.dto.request.JuridicClientRequest;
import uz.davrbank.yeisvoservice.aggregate.dto.response.ResponseInnDTO;
import uz.davrbank.yeisvoservice.aggregate.dto.response.report.ResponseDTO;

import java.util.List;


public interface JuridicMFOInterface {

    ResponseDTO<List<ResponseInnDTO>> getJuridicMFO(JuridicClientRequest request);


}