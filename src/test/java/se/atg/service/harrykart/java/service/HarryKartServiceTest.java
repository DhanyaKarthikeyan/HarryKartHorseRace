package se.atg.service.harrykart.java.service;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import se.atg.service.harrykart.java.exception.HorseRaceSpeedException;
import se.atg.service.harrykart.java.model.HarryKartDTO;
import se.atg.service.harrykart.java.model.HorseInLoopDTO;
import se.atg.service.harrykart.java.model.PositionDTO;
import se.atg.service.harrykart.java.model.ResultDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("UnitTest")
final class HarryKartServiceTest {
  private final XmlMapper xmlMapper = new XmlMapper();
  private HarryKartService harryKartService;

  @BeforeEach
  void setUp() {
    harryKartService = new HarryKartService();
  }

  private ResultDTO getResultDTO() {
    final ResultDTO resultDTO = new ResultDTO();
    final List<PositionDTO> positionDTOList = new ArrayList<>();
    positionDTOList.add(getFirstPosition());
    positionDTOList.add(getSecondPosition());
    positionDTOList.add(getThirdPosition());
    resultDTO.setRanking(positionDTOList);

    return resultDTO;
  }

  private PositionDTO getFirstPosition() {
    final PositionDTO positionDTO = new PositionDTO();
    positionDTO.setPosition(1);
    positionDTO.setHorse("TIMETOBELUCKY");
    return positionDTO;
  }

  private PositionDTO getSecondPosition() {
    final PositionDTO positionDTO = new PositionDTO();
    positionDTO.setPosition(2);
    positionDTO.setHorse("HERCULES BOKO");
    return positionDTO;
  }

  private PositionDTO getThirdPosition() {
    final PositionDTO positionDTO = new PositionDTO();
    positionDTO.setPosition(3);
    positionDTO.setHorse("CARGO DOOR");
    return positionDTO;
  }

  @Nested
  class calculateHorseSpeedAndTimeInLoop {

    @Test
    void defaultTest() throws HorseRaceSpeedException, IOException {
      String resourceName = "input_firstRace.xml";
      ClassLoader classLoader = this.getClass().getClassLoader();
      HarryKartDTO harryKartDTO = xmlMapper.readValue(classLoader.getResource(resourceName), HarryKartDTO.class);
      final List<HorseInLoopDTO> horseInLoopDTOList = harryKartService.calculateHorseSpeedAndTime(harryKartDTO);
      assertFalse(horseInLoopDTOList.isEmpty());
      assertEquals(12, horseInLoopDTOList.size());
    }

    @Test
    void speedValueIsNegative() throws IOException, HorseRaceSpeedException {
      String resourceName = "input_speedNegative.xml";
      ClassLoader classLoader = this.getClass().getClassLoader();
      HarryKartDTO harryKartDTO = xmlMapper.readValue(classLoader.getResource(resourceName), HarryKartDTO.class);
      assertThrows(HorseRaceSpeedException.class, () -> {
        harryKartService.calculateHorseSpeedAndTime(harryKartDTO);
      });
    }

    @Test
    void speedIsZero() throws IOException, HorseRaceSpeedException {
      String resourceName = "input_speedIsZero.xml";
      ClassLoader classLoader = this.getClass().getClassLoader();
      HarryKartDTO harryKartDTO = xmlMapper.readValue(classLoader.getResource(resourceName), HarryKartDTO.class);
      final List<HorseInLoopDTO> horseInLoopDTOList = harryKartService.calculateHorseSpeedAndTime(harryKartDTO);
      assertFalse(horseInLoopDTOList.isEmpty());
      assertEquals(1000.0, horseInLoopDTOList.get(4).getFinishTime());
      assertEquals(8, horseInLoopDTOList.size());
    }

    @Test
    void zeroPowerUps() throws IOException, HorseRaceSpeedException {
      String resourceName = "input_zeroPowerUps.xml";
      ClassLoader classLoader = this.getClass().getClassLoader();
      HarryKartDTO harryKartDTO = xmlMapper.readValue(classLoader.getResource(resourceName), HarryKartDTO.class);
      final List<HorseInLoopDTO> horseInLoopDTOList = harryKartService.calculateHorseSpeedAndTime(harryKartDTO);
      assertNotNull(horseInLoopDTOList);
    }

    @Test
    void zeroStartList() throws IOException, HorseRaceSpeedException {
      String resourceName = "input_emptyStartList.xml";
      ClassLoader classLoader = this.getClass().getClassLoader();
      HarryKartDTO harryKartDTO = xmlMapper.readValue(classLoader.getResource(resourceName), HarryKartDTO.class);
      final List<HorseInLoopDTO> horseInLoopDTOList = harryKartService.calculateHorseSpeedAndTime(harryKartDTO);
      assertTrue(horseInLoopDTOList.isEmpty());
    }

    @Test
    void nullTest() throws HorseRaceSpeedException {
      final List<HorseInLoopDTO> horseInLoopDTOList = harryKartService.calculateHorseSpeedAndTime(null);
      assertTrue(horseInLoopDTOList.isEmpty());
    }
  }

  @Nested
  class getHorseRaceWinnerResult {
    @Test
    void defaultTest() throws HorseRaceSpeedException, IOException {
      String resourceName = "input_firstRace.xml";
      ClassLoader classLoader = this.getClass().getClassLoader();
      HarryKartDTO harryKartDTO = xmlMapper.readValue(classLoader.getResource(resourceName), HarryKartDTO.class);
      final List<HorseInLoopDTO> horseInLoopDTOList = harryKartService.calculateHorseSpeedAndTime(harryKartDTO);
      final ResultDTO actualResult = harryKartService.getHorseRaceWinnerResult(horseInLoopDTOList);
      assertNotNull(actualResult);
      assertEquals(getResultDTO(), actualResult);
    }

    @Test
    void nullTest() {
      final ResultDTO actualResult = harryKartService.getHorseRaceWinnerResult(null);
      assertNull(actualResult.getRanking());
    }

  }

}