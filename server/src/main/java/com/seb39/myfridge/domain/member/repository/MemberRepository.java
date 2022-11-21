package com.seb39.myfridge.domain.member.repository;


import com.seb39.myfridge.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m from Member m where m.provider is null and m.email = :email")
    Optional<Member> findGeneralByEmail(@Param("email") String email);
    boolean existsById(Long id);
    boolean existsByName(String name);
    boolean existsByProviderAndProviderId(String provider, String providerId);
    Optional<Member> findByProviderAndProviderId(String provider, String providerId);
}
