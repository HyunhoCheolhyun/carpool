package com.criminals.plusExponential.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

    @Column(name = "created_data_time", nullable = false)
    @CreatedDate
    private LocalDateTime createdDateTime;

    @Column(name = "modified_date_time", nullable = false)
    @LastModifiedDate
    private LocalDateTime modifiedDateTime;

    /* 해당 엔티티를 저장하기 이전에 실행 */
    @PrePersist
    public void onPrePersist() {
        this.createdDateTime = LocalDateTime.now();
        this.modifiedDateTime = this.createdDateTime;
    }

    /* 해당 엔티티를 업데이트 하기 이전에 실행*/
    @PreUpdate
    public void onPreUpdate() {
        this.modifiedDateTime = LocalDateTime.now();
    }
}
