package com.daangn.clone.common;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;


/**
 * (ERD에는 표시하지 않았지만) 모든 Entity에서 공통적으로 가질 createdAt과 updatedAt 필드를 이 BasicEntity에 집어넣어두고
 * 이 BasicEntity를 다른 모든 Entity들이 상속하게 하므로써 -> 다른 Entity들이 createdAt과 updatedAt 컬럼을 갖도록 만듦.
 * => 이렇게 특정 필드를 (DB 상에선 컬럼을) 상속하게 만드는 어노테이션이 바로 @MappedSuperclass 이다.
 *
 * 또한 이 BasicEntity 클래스는 오직 필드 상속을 위한 클래스 이지 - DB상의 테이블과 대응되는 Entity가 아니므로
 * @Entity 어노테이션을 붙이지 않는다.
 *
 * */

@MappedSuperclass
@Getter @Setter
public class BasicEntity {

    @Column(updatable = false, name = "created_at") //createdAt은 최초에 생성될 떄 초기화 된 이후 업데이트 되면 안되는 값 이므로 -> 업데이트를 막아둠
    private LocalDateTime createdAt;
    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    /** em.persist() 가 수행되기 전 */
    @PrePersist
    public void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;
    }

    /** dirty checking 으로 update 쿼리가 나가기 전 */
    @PreUpdate
    public void postUpdate(){
        updatedAt = LocalDateTime.now();
    }

}
