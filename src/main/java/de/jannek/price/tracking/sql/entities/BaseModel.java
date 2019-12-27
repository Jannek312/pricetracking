package de.jannek.price.tracking.sql.entities;

import io.ebean.Model;
import io.ebean.annotation.WhenCreated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
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

    @WhenCreated
    protected Instant whenCreated;

}
