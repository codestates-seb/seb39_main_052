package com.seb39.myfridge.domain.auth.repository;

import com.seb39.myfridge.domain.auth.domain.AuthenticationToken;
import org.springframework.data.repository.CrudRepository;

public interface AuthenticationTokenRepository extends CrudRepository<AuthenticationToken, String> {
}
