package com.criminals.plusExponential.domain.entity;

import com.criminals.plusExponential.domain.embeddable.UnmatchedPath;
import jakarta.persistence.*;

@Entity
@Table(name = "passengers")
public class Passenger extends User {

    @Embedded
    private UnmatchedPath unmatchedPath;



}
