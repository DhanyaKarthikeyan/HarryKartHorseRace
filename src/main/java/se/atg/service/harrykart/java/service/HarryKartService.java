package se.atg.service.harrykart.java.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import se.atg.service.harrykart.java.exception.HorseRaceSpeedException;
import se.atg.service.harrykart.java.model.HarryKartDTO;
import se.atg.service.harrykart.java.model.HorseInLoopDTO;
import se.atg.service.harrykart.java.model.LoopDTO;
import se.atg.service.harrykart.java.model.ParticipantDTO;
import se.atg.service.harrykart.java.model.PositionDTO;
import se.atg.service.harrykart.java.model.ResultDTO;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toMap;

/**
 * Service class for HarryKart race. This will be defining the business logic to identify the race winner.
 * @author dhanya.karthykeyan@gmail.com
 * @version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
public class HarryKartService {
  private static final double TRACK_LENGTH = 1000.0;
  private static final double LOOP_LAG_TIME = 1000.0;

  /**
   *This method will be calculating the horse speed in each loop and adding the details into HorseInLoopDTO object
   * @param harryKartData
   * @return List of HorseInLoopDTO
   * @throws HorseRaceSpeedException
   *
   */
  public List<HorseInLoopDTO> calculateHorseSpeedAndTime(final HarryKartDTO harryKartData) throws HorseRaceSpeedException {
    final List<HorseInLoopDTO> horseInLoopDTOList = new ArrayList<>();

    if (isNull(harryKartData) || isNull(harryKartData.getStartList())) {
      log.info("calculateHorseSpeedAndTime :: Returning -> Given horse details is null or empty!");
      return horseInLoopDTOList;
    }

    double horseSpeed;
    for (int loop = 0; loop < harryKartData.getNumberOfLoops(); loop++) {
      for (int lane = 0; lane < harryKartData.getStartList().size(); lane++) {
        final HorseInLoopDTO horseInLoopData = new HorseInLoopDTO();
        final ParticipantDTO participantData = harryKartData.getStartList().get(lane);
        horseInLoopData.setLane(participantData.getLane());
        horseInLoopData.setHorseName(participantData.getName());
        int powerVariance = 0;
        for (int powerInLoop = 0; powerInLoop < loop; powerInLoop++) {
          final List<LoopDTO> powerUps = harryKartData.getPowerUps();
          if (isNull(powerUps) || powerUps.isEmpty()) {
            log.info("calculateHorseSpeedAndTime :: No power change given for horses in race.");
            break;
          }
          powerVariance = powerVariance + powerUps.get(powerInLoop).getLane().get(lane).getValue();
        }
        horseInLoopData.setLoopSpeed((double) (participantData.getBaseSpeed() + powerVariance));
        horseSpeed = participantData.getBaseSpeed() + powerVariance;
        if (horseSpeed == 0) {
          horseInLoopData.setFinishTime(LOOP_LAG_TIME);
        } else if (horseSpeed < 0) {
          throw new HorseRaceSpeedException("Invalid speed given for the horse!");
        } else {
          horseInLoopData.setFinishTime(TRACK_LENGTH / horseSpeed);
        }
        horseInLoopDTOList.add(horseInLoopData);
      }
    }
    return horseInLoopDTOList;
  }

  /**
   *This method will give the winners list (first 3 positions and horse name)
   * @param horseInLoopList
   * @return ResultDTO object
   *
   */
  public ResultDTO getHorseRaceWinnerResult(final List<HorseInLoopDTO> horseInLoopList) {
    final ResultDTO raceResult = new ResultDTO();

    if (isNull(horseInLoopList) || horseInLoopList.isEmpty()) {
      log.info("getHorseRaceWinnerResult :: Winner list couldn't calculate, as the horse details is null or empty!");
      return raceResult;
    }

    final Map<String, Double> result = horseInLoopList.stream().collect(Collectors.groupingBy(HorseInLoopDTO::getHorseName, Collectors.summingDouble(HorseInLoopDTO::getFinishTime)));
    final Map<String, Double> sortedResult = result.entrySet().stream().sorted(comparingByValue()).collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
            LinkedHashMap::new));
    final List<PositionDTO> positionDTOList = new ArrayList<>();
    int position = 1;
    for (Map.Entry<String, Double> entry : sortedResult.entrySet()) {
      if (position > 3) {
        log.info("getHorseRaceWinnerResult :: Good Race '{}', but the fourth place is not considering in winner list", entry.getKey());
        break;
      }
      final PositionDTO positionDTO = new PositionDTO();
      positionDTO.setPosition(position);
      positionDTO.setHorse(entry.getKey());
      positionDTOList.add(positionDTO);
      position++;
    }
    raceResult.setRanking(positionDTOList);
    return raceResult;
  }

}




