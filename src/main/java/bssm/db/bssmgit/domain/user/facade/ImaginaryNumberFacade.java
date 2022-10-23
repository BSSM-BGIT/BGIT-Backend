package bssm.db.bssmgit.domain.user.facade;

import bssm.db.bssmgit.domain.user.domain.ImaginaryNumber;
import bssm.db.bssmgit.domain.user.repository.ImaginaryNumberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ImaginaryNumberFacade {

    private final ImaginaryNumberRepository imaginaryNumberRepository;

    public ImaginaryNumber save(ImaginaryNumber imaginaryNumber) {
        return imaginaryNumberRepository.save(imaginaryNumber);
    }

    public List<ImaginaryNumber> findUsersByTop3() {
        return imaginaryNumberRepository.findTop3ByOrderByVotingNumberDesc();
    }
    public List<ImaginaryNumber> findAll() {
        return imaginaryNumberRepository.findAll();
    }

    public void removeAll() {
        imaginaryNumberRepository.deleteAll();
    }
}
