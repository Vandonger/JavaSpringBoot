package com.onemount.barcelonateam.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.onemount.barcelonateam.exception.TeamException;
import com.onemount.barcelonateam.model.Player;
import com.onemount.barcelonateam.model.Position;
import com.onemount.barcelonateam.model.Substitute;
import com.onemount.barcelonateam.model.TeamAndSubstitute;
import com.onemount.barcelonateam.repository.PlayerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CoachService {
  //Hãy tạo method chọn team
  //Chọn ngẫu nhiên cho từng vị trí
  @Autowired
  private PlayerRepository playerRepository;

  HashSet<Player> currentTeam;
  List<Substitute> substituteHistory;

  public CoachService() {
    currentTeam = new HashSet<>();
    substituteHistory = new ArrayList<>();
  }

  public Set<Player> chooseTeam(int dfNum, int mfNum, int fwNum) {
    if((dfNum +mfNum + fwNum) != 10) {
      throw new IllegalArgumentException("Sum of defenders " + dfNum + " and mid fielders " + mfNum + " and fowarder " + fwNum + " must be 10!");
    }
    
    currentTeam.clear();  //Xoá hết rồi chọn lại
    substituteHistory.clear();

    currentTeam.addAll(playerRepository.chooseUniquePlayersByPosition(Position.GK, 1));
    currentTeam.addAll(playerRepository.chooseUniquePlayersByPosition(Position.DF, dfNum));
    currentTeam.addAll(playerRepository.chooseUniquePlayersByPosition(Position.FW, fwNum));
    currentTeam.addAll(playerRepository.chooseUniquePlayersByPosition(Position.MF, mfNum));    
    
    return currentTeam;
  }

  public Set<Player> getCurrentTeam() {
    return currentTeam;
  }

  public List<Substitute> subtitude(int playerno, Position position) throws TeamException{
    if (currentTeam == null) {
      throw new TeamException("Team is not formed yet");
    }

    if (substituteHistory.size() == 5) {
      throw new TeamException("Number of substitution exceeds 5");
    }

    Player playerIn;
    Player playerOut;
    playerOut = currentTeam.stream().filter(a-> a.getNumber() == playerno).findAny().get();

    // check player out
    if( playerOut == null) {
      throw new TeamException("The player does not exist on the field");
    }
    // check position player in
    if(availablePlayers().stream().filter(a -> position == a.getPosition()).findAny() == null) {
      throw new TeamException("The position to be replaced has expired");
    }

    //remove player out in current team
    currentTeam.remove(playerOut);
    //add player out to substituteHistory
    substituteHistory.add(new Substitute(playerOut,null));
    playerIn = playerRepository.chooseUniquePlayersByPosition(position,1).stream().findAny().get();
    //add player in in current team
    currentTeam.add(playerIn);
    //remove player out to substituteHistory
    substituteHistory.add(new Substitute(null,playerIn));
    return substituteHistory;
  }

  public List<Player> availablePlayers() {
    List<Player> available = new ArrayList<Player>();
    available = playerRepository.getPlayers();
    boolean result =  available.removeAll(currentTeam);
    return result == true ? available : null;
  }

}
