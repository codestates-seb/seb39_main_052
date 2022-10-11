package com.seb39.myfridge.image.repository;

import com.seb39.myfridge.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
