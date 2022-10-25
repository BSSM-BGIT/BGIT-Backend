package bssm.db.bssmgit.domain.user.repository;

import bssm.db.bssmgit.domain.user.domain.ImaginaryNumber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImaginaryNumberRepository extends JpaRepository<ImaginaryNumber, Long> {

//    List<ImaginaryNumber> findTop3ByOrderByVotingNumberDesc();

}
