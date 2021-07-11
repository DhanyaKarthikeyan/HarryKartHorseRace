package se.atg.service.harrykart.java.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.atg.service.harrykart.java.exception.HorseRaceSpeedException;
import se.atg.service.harrykart.java.model.HarryKartDTO;
import se.atg.service.harrykart.java.model.HorseInLoopDTO;
import se.atg.service.harrykart.java.model.ResultDTO;
import se.atg.service.harrykart.java.service.HarryKartService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.APPLICATION_XML_VALUE;

/**
 * This is a controller class for HarryKart Race.
 * This will be accepting data in xml type and producing the winner result in json type.
 * @author dhanya.karthykeyan@gmail.com
 * @version 1.0
 */

@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
@RequestMapping("/java/api")
public class HarryKartController {
  @Autowired
  private HarryKartService harryKartService;

  @Autowired
  private ResultDTO horseRaceResultData;

  /**
   *This REST Api call will accept data in xml type and produce winner result in json type.
   * @param harryKartData
   * @return ResultDTO
   * api url /java/api/play
   */

  @PostMapping(path = "/play", consumes = APPLICATION_XML_VALUE, produces = APPLICATION_JSON_VALUE)
  public ResultDTO playHarryKart(@RequestBody final HarryKartDTO harryKartData) {
    log.info("playHarryKart :: Starting Horse Race");
    try {
      final List<HorseInLoopDTO> horseInLoopList = harryKartService.calculateHorseSpeedAndTime(harryKartData);
      horseRaceResultData = harryKartService.getHorseRaceWinnerResult(horseInLoopList);
    } catch (final HorseRaceSpeedException e) {
      e.printStackTrace();
    }
    return horseRaceResultData;
  }
}
