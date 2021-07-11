package se.atg.service.harrykart.java.model;

import lombok.Data;

/**
 * This is Data transfer Object for HorseInLoop
 * This DTO will be holding details of lane, finishTime, horseName and loopSpeed for each loop.
 */
@Data
public class HorseInLoopDTO {
  private Integer lane;
  private Double finishTime;
  private String horseName;
  private Double loopSpeed;
}
