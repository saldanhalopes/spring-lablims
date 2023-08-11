package br.com.lablims.domain;

import jakarta.persistence.*;


import br.com.lablims.config.CustomRevisionListener;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionEntity;

import java.util.Date;

@Entity
@RevisionEntity(value = CustomRevisionListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Table(name = "ta_revinfo_audit", schema = "audit")
public class CustomRevisionEntity extends DefaultRevisionEntity {

    private String userName;
    private String ip;
    @Transient
    private String revType;
    private String motivo;

    public CustomRevisionEntity(Integer revisaoId, Date revisaoData, String userName, String ip, String motivo, String revType) {
        this.setId(revisaoId);
        this.setTimestamp(revisaoData.getTime());
        this.userName = userName;
        this.ip = ip;
        this.revType = revType;
        this.motivo = motivo;
    }

}


