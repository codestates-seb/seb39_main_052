package com.seb39.myfridge.member.repository;


import com.seb39.myfridge.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsById(Long id);

    boolean existsByProviderAndProviderId(String provider, String providerId);
}
