package uz.davrbank.userinfobase.V2.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import uz.davrbank.userinfobase.V2.dto.LoanAttachmentDTO;
import uz.davrbank.userinfobase.V2.entity.LoanAttachment;
import uz.davrbank.userinfobase.V2.repository.LoanAttachmentRepository;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LoanAttachmentService {

    LoanAttachmentRepository loanAttachmentRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public LoanAttachment create(LoanAttachmentDTO loanAttachmentDTO) {
        LoanAttachment loanAttachment = loanAttachmentRepository.findFirstByLoanExtIdAndBranchIdAndAttachmentId(loanAttachmentDTO.getLoanExtId(), loanAttachmentDTO.getBranchId(), loanAttachmentDTO.getAttachmentId()).orElseGet(LoanAttachment::new);
        if (loanAttachment.getId() != null) {
            return loanAttachment;
        }
        loanAttachment.setAttachmentId(loanAttachmentDTO.getAttachmentId());
        loanAttachment.setLoanExtId(loanAttachmentDTO.getLoanExtId());
        loanAttachment.setBranchId(loanAttachmentDTO.getBranchId());
        return loanAttachmentRepository.saveAndFlush(loanAttachment);
    }

    public LoanAttachment get(String loanExtId, String branchId, UUID attachmentId) {
        return loanAttachmentRepository.findFirstByLoanExtIdAndBranchIdAndAttachmentId(loanExtId, branchId, attachmentId).orElse(null);
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public List<LoanAttachment> getList(String loanExtId, String branchId) {
        return loanAttachmentRepository.findAllWithAttachmentByLoanExtIdAndBranchId(loanExtId, branchId);
    }

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public List<LoanAttachmentRepository.LoanAttachmentFileView> getFileViews(String loanExtId, String branchId) {
        return loanAttachmentRepository.findFileViewsByLoanExtIdAndBranchId(loanExtId, branchId);
    }
}
