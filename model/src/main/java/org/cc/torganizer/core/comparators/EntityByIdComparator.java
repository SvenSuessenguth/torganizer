package org.cc.torganizer.core.comparators;

import java.util.Comparator;
import java.util.Objects;
import org.cc.torganizer.core.entities.Entity;

/**
 * @author svens
 */
public class EntityByIdComparator implements Comparator<Entity>{
  
  @Override
  public int compare(Entity o1, Entity o2) {
    Long id1 = o1.getId();
    Long id2 = o2.getId();
        
    if(Objects.equals(o1, o2) || Objects.equals(id1, id2)){
      return 0;
    }
        
    if(id1!=null && id2==null){
      return 1;
    }else if(id1==null && id2!=null){
      return -1;
    }else{
      return Long.compare(id1, id2);
    }
  }
}
