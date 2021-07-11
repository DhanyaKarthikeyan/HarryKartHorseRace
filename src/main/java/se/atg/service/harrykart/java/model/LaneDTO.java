package se.atg.service.harrykart.java.model;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;
import lombok.Data;
/**
 * This is Data transfer Object for Lane
 * This DTO will be holding details of lane number and value.
 */
@Data
public class LaneDTO {
  @JacksonXmlProperty(isAttribute = true)
  private Integer number;

  @JacksonXmlText
  private Integer value;
}
