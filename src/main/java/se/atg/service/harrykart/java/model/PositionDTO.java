package se.atg.service.harrykart.java.model;

import lombok.Data;
import org.springframework.stereotype.Component;

/**
 * This is Data transfer Object for Position
 * This DTO will be holding details of position and horse name.
 */
@Data
@Component
public class PositionDTO {
  private Integer position;
  private String horse;
}
