package it.gov.pagopa.bpd.common.model.entity;

import lombok.Data;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.io.Serializable;
import java.time.OffsetDateTime;

@MappedSuperclass
@Data
@Where(clause = "ENABLED_B = 'TRUE'")
public abstract class BaseEntity implements Serializable {

    @Column(name = "INSERT_DATE_T")
    private OffsetDateTime insertDate;

    @Column(name = "INSERT_USER_S")
    private String insertUser;

    @Column(name = "UPDATE_DATE_T")
    private OffsetDateTime updateDate;

    @Column(name = "UPDATE_USER_S")
    private String updateUser;

    @Column(name = "ENABLED_B")
    private boolean enabled = true;

    @PrePersist
    protected void onCreate() {
        insertDate = OffsetDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateDate = OffsetDateTime.now();
    }

}
