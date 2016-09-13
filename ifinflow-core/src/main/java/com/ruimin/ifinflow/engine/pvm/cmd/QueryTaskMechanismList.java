package com.ruimin.ifinflow.engine.pvm.cmd;

/*     */

import com.ruimin.ifinflow.engine.external.model.IWfUnit;
import com.ruimin.ifinflow.engine.flowmodel.vo.TaskMechanismVO;
import com.ruimin.ifinflow.engine.internal.config.UserExtendsReference;
import com.ruimin.ifinflow.util.StringHelper;
import java.util.*;
/*     */
/*     */
/*     */
/*     */
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jbpm.api.cmd.Command;
import org.jbpm.api.cmd.Environment;
import org.jbpm.pvm.internal.history.model.HistoryTaskImpl;









public class QueryTaskMechanismList
  implements Command<List<TaskMechanismVO>>
{
  private static final long serialVersionUID = 1L;
  private Date startTime1;
  private Date startTime2;
  
  public QueryTaskMechanismList(Date startTime1, Date startTime2)
  {
    this.startTime1 = startTime1;
    this.startTime2 = startTime2;
  }
  
  public List<TaskMechanismVO> execute(Environment environment) throws Exception
  {
    Session session = environment.get(Session.class);
    
    List<IWfUnit> units = (List<IWfUnit>) UserExtendsReference.getIdentityAdapter().getIdentityInfoPage(null, "3", 0, 0);
    
    Map<String, String> unitMap = new HashMap();
    for (IWfUnit u : units) {
      unitMap.put(u.getUnitId(), u.getUnitName());
    }
    
    Criteria criteria = session.createCriteria(HistoryTaskImpl.class);
    
    if (null != this.startTime1) {
      criteria.add(Restrictions.ge("createTime", this.startTime1));
    }
    
    if (null != this.startTime2) {
      criteria.add(Restrictions.le("createTime", this.startTime2));
    }
    

    ProjectionList projectionList = Projections.projectionList();
    projectionList.add(Projections.groupProperty("ownerUnitId"));
    projectionList.add(Projections.groupProperty("status"));
    projectionList.add(Projections.rowCount());
    
    criteria.setProjection(projectionList);
    
    List<Object[]> histasks = criteria.list();
    
    List<TaskMechanismVO> results = new ArrayList();
    TaskMechanismVO tmvo = null;
    
    for (Object[] tmps : histasks) {
      if (tmps[0] == null) {
        tmvo = new TaskMechanismVO("未指定机构", "未指定机构", "0", "0", "0", "0", "0", "0");
      }
      else {
        tmvo = new TaskMechanismVO((String)tmps[0], unitMap.get(tmps[0]), "0", "0", "0", "0", "0", "0");
      }
      
      if (results.contains(tmvo)) {
        tmvo = results.get(results.indexOf(tmvo));
      } else {
        results.add(tmvo);
      }
      
      int ti = 0;
      if (tmps[1] != null) {
        ti = ((Integer)tmps[1]).intValue();
        if ((1 == ti) || (2 == ti))
        {
          tmvo.setTodoNum(StringHelper.stringPlusN(tmvo.getTodoNum(), (Integer)tmps[2]));
        }
        else if ((16 == ti) || (6 == ti))
        {
          tmvo.setDoneNum(StringHelper.stringPlusN(tmvo.getDoneNum(), (Integer)tmps[2]));
        }
        else if (4 == ti) {
          tmvo.setBackNum(StringHelper.stringPlusN(tmvo.getBackNum(), (Integer)tmps[2]));
        }
        else if (512 == ti) {
          tmvo.setExceptionNum(StringHelper.stringPlusN(tmvo.getExceptionNum(), (Integer)tmps[2]));
        }
        else if (256 == ti) {
          tmvo.setOvertimeNum(StringHelper.stringPlusN(tmvo.getOvertimeNum(), (Integer)tmps[2]));
        }
      }
      

      tmvo.setTotal(StringHelper.stringPlusN(tmvo.getTotal(), (Integer)tmps[2]));
    }
    


    return results;
  }
}