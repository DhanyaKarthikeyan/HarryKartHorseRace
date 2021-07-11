package se.atg.service.harrykart.java.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.List;

/**
 * This is Data transfer Object for Loop
 * This DTO will be holding details of Loop number and lane.
 */
@Data
public class LoopDTO {
  @JacksonXmlProperty(isAttribute = true)
  private Integer number;

  @JacksonXmlElementWrapper(useWrapping = false)
  private List<LaneDTO> lane;
}
