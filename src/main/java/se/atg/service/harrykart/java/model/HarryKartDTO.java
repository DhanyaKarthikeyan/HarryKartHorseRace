package se.atg.service.harrykart.java.model;

import lombok.Data;

import java.util.List;

/**
 * This is Data transfer Object for HarryKart
 * This DTO will be holding details of number of loops, participant details and power variance details.
 */
@Data
public class HarryKartDTO {
  private Integer numberOfLoops;
  private List<ParticipantDTO> startList;
  private List<LoopDTO> powerUps;
}
