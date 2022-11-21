package com.seb39.myfridge.domain.image.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int idx;

    private String imagePath;


    private String isUpdated; //업데이트 여부

}
