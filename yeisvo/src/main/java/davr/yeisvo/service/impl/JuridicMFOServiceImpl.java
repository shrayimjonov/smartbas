package davr.yeisvo.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;
import davr.yeisvo.config.OracleConfig;
import davr.yeisvo.dto.request.JuridicClientRequest;
import davr.yeisvo.dto.response.BankBranchResponse;
import davr.yeisvo.dto.response.GetMfoJuridicResponse;
import davr.yeisvo.dto.response.JuridicClient;
import davr.yeisvo.entity.postgres.JuridicINNEntity;
import davr.yeisvo.repository.postgres.JuridicINNRepository;
import davr.yeisvo.service.BankBranchService;
import davr.yeisvo.service.JuridicMFOService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.List;

@Service
@AutoJsonRpcServiceImpl
@Slf4j
public class JuridicMFOServiceImpl implements JuridicMFOService {

    private final BankBranchService bankBranchService;
    private final ObjectMapper objectMapper;

    private final JuridicINNRepository juridicINNRepository;
    private static final Logger LOG = LoggerFactory.getLogger(JuridicMFOServiceImpl.class);

    private final OracleConfig oracleDatabaseConfig;

    public JuridicMFOServiceImpl(BankBranchService bankBranchService, ObjectMapper objectMapper, JuridicINNRepository juridicINNRepository, @Qualifier("oracleConfig") OracleConfig oracleDatabaseConfig) {
        this.bankBranchService = bankBranchService;
        this.objectMapper = objectMapper;
        this.juridicINNRepository = juridicINNRepository;
        this.oracleDatabaseConfig = oracleDatabaseConfig;
    }

    /**
     * bu method INN lar listini olib keladi , active  =  true, false, full bo'lishi mumkin
     * @param request
     * @return
     */
    @Override
    @Transactional
    public GetMfoJuridicResponse getJuridicMFO(JuridicClientRequest request) {

        LOG.info("REQUEST: Getting getMFOJuridic of Period : {}, {}", request.getBankId(), request.getActive());

        final String jdbcUrl = oracleDatabaseConfig.getUrl();
        final String username = oracleDatabaseConfig.getUsername();
        final String password = oracleDatabaseConfig.getPassword();

        try (Connection conn = DriverManager.getConnection(jdbcUrl, username, password)) {

            CallableStatement cstmt = conn.prepareCall(
                    "{ ? = call CLIENTS_TRANZ.GET_MFO_JURIDIC(?, ?) }"
            );

            // OUT parameter
            cstmt.registerOutParameter(1, Types.CLOB);

            // IN parameters
            cstmt.setString(2, request.getBankId());
            cstmt.setString(3, request.getActive());

            cstmt.execute();

            Clob clob = cstmt.getClob(1);

            String jsonResult = clob.getSubString(1, (int) clob.length());
            cstmt.close();

            GetMfoJuridicResponse response = objectMapper.readValue(
                    jsonResult,
                    new TypeReference<GetMfoJuridicResponse>() {
                    }
            );
            save(response);
            return response;

        } catch (Exception e) {
            LOG.error(e.getMessage());
            return new GetMfoJuridicResponse();
//            throw new GlobalExceptionMessage("There is a problem connecting to the database. method: getMFoJuridic");
        }
    }


    public void save(GetMfoJuridicResponse response) {
        if (response == null) {
            log.warn("Response is empty, skipping save");
            return;
        }

        for (JuridicClient datum : response.getData()) {
            JuridicINNEntity exchange = datum.exchange();
            if (!juridicINNRepository.existsByClientcbIdAndBranchClient(exchange.getClientcbId(), exchange.getBranchClient())) {
                juridicINNRepository.save(exchange);
            } else {
                juridicINNRepository.updateByBranchClientAndClientcbId(exchange);
            }
        }
        log.info("Saved juridic INN records");
    }


    /**
     * bu method auto update qilish uchun kerak ,
     * external service dan datalarni olib kelib ozimizni db ni update qiladi
     */
    @Scheduled(cron = "0 0 0 * * *")
//    @Scheduled(cron = "0 */1 * * * *")
    public void defaultUpdateINN() {
        log.info("Starting scheduled INN update...");
        List<BankBranchResponse> responseList = bankBranchService.getAll();

        for (BankBranchResponse branch : responseList) {
            JuridicClientRequest request = new JuridicClientRequest(branch.getMfo(), "full");
            try {
                getJuridicMFO(request);
                log.info("Saved client:");

            } catch (Exception e) {
                log.error("Failed to update INN for bankId={}: {}", branch.getMfo(), e.getMessage());
            }
        }
        log.info("Scheduled INN update completed.");
    }
}
