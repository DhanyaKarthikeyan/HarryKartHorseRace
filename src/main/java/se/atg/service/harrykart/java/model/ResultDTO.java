package se.atg.service.harrykart.java.model;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This is Data transfer Object for Winner Result
 * This DTO will be holding details of Winners Rank Details.
 */
@Data
@Component
public class ResultDTO {
  private List<PositionDTO> ranking;
}
