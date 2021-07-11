package se.atg.service.harrykart.java.model;

import lombok.Data;

/**
 * This is Data transfer Object for Participant
 * This DTO will be holding details of participant name, lane and baseSpeed.
 */
@Data
public class ParticipantDTO {
  private String name;
  private Integer lane;
  private Integer baseSpeed;
}
