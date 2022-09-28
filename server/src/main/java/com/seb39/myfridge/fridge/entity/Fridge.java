package com.seb39.myfridge.fridge.entity;

import com.seb39.myfridge.member.entity.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Fridge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fridge_id")
    private Long id;


    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

}
