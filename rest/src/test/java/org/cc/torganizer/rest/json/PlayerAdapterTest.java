/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.cc.torganizer.rest.json;

import javax.json.JsonObject;
import org.cc.torganizer.core.entities.Person;
import org.cc.torganizer.core.entities.Player;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author svens
 */
public class PlayerAdapterTest {
  
  private PlayerAdapter adpater;
  
  @Before
  public void before(){
    adpater = new PlayerAdapter();
  }
  
  @Test
  public void testAdaptToJson_allValuesNullOrDefault() throws Exception {
    Player player = new Player(new Person());
    
    JsonObject jsonObject = adpater.adaptToJson(player);
    
  }
}
