package com.fpt.ojt.models.postgres.card;

import org.checkerframework.checker.units.qual.A;

import com.fpt.ojt.models.postgres.AbstractBaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "countries")
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Country extends AbstractBaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "iso_code", nullable = false)
    private String isoCode;

    @Column(name = "phone_code", nullable = false)
    private String phoneCode;
}
