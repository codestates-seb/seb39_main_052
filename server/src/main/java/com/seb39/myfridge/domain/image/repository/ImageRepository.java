package com.seb39.myfridge.domain.image.repository;

import com.seb39.myfridge.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
