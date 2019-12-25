package de.jannek.price.tracking.sql.entities;

import io.ebean.Model;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import java.time.Instant;

/**
 * Base domain object with Id, version, whenCreated and whenModified.
 */
@MappedSuperclass
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public abstract class BaseModel extends Model {

    @Id
    protected long id;

    @Version
    protected Long version;

    @WhenCreated
    protected Instant whenCreated;

    @WhenModified
    protected Instant whenModified;

}
