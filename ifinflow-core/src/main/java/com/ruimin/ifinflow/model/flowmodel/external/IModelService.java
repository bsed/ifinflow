package com.ruimin.ifinflow.model.flowmodel.external;

import com.ruimin.ifinflow.model.flowmodel.manage.xml.parse.vo.ParseReturnVo;
import com.ruimin.ifinflow.model.flowmodel.xml.Node;
import com.ruimin.ifinflow.model.flowmodel.xml.Template;
import com.ruimin.ifinflow.model.flowmodel.xml.TemplatePackage;
import com.ruimin.ifinflow.model.flowmodel.xml.TemplateVariable;
import com.ruimin.ifinflow.util.exception.IFinFlowException;
import java.io.Serializable;
import java.util.List;

public interface IModelService<T>
{
  List<TemplatePackage> getAllPackage();
  
  T save(T paramT);
  
  boolean isExist(String paramString);
  
  T get(Class<T> paramT, Serializable paramSerializable);
  
  List<Template> getTemplates(String paramString, boolean paramBoolean);
  
  void update(T paramT);
  
  Integer updateByHql(String paramString);
  
  void delete(T paramT);
  
  List<?> queryByHql(String paramString)
    throws Exception;
  
  void test();
  
  ParseReturnVo parseJpdl(String paramString)
    throws Exception;
  
  ParseReturnVo parseJpdl(String paramString, boolean paramBoolean)
    throws Exception;
  
  String getXml(String paramString);
  
  String getXml(String paramString1, String paramString2, int paramInt);
  
  boolean checkExpression(String paramString, List<String> paramList)
    throws IFinFlowException;
  
  boolean saveDesigningTemplate(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5)
    throws IFinFlowException;
  
  boolean saveDesigningTemplate(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, byte[] paramArrayOfByte)
    throws IFinFlowException;
  
  boolean checkXml(String paramString)
    throws IFinFlowException;
  
  boolean isExistOfTemplateId(String paramString1, String paramString2)
    throws IFinFlowException;
  
  String findDeploymentId(String paramString1, String paramString2, int paramInt)
    throws IFinFlowException;
  
  boolean uninstallTemplate(String paramString1, String paramString2, int paramInt)
    throws IFinFlowException;
  
  boolean importCheck(String paramString);
  
  void deleteTemplateData(String paramString);
  
  ParseReturnVo importXml(String paramString)
    throws IFinFlowException;
  
  TemplatePackage findTemplate(String paramString)
    throws IFinFlowException;
  
  List<TemplatePackage> findPackagesCascade(String paramString)
    throws IFinFlowException;
  
  List<TemplateVariable> findTemplateVariables(String paramString)
    throws IFinFlowException;
  
  boolean hasChangeOfContent(String paramString1, String paramString2)
    throws IFinFlowException;
  
  List<Node> findNodeList(String paramString1, String paramString2, int paramInt, String paramString3)
    throws IFinFlowException;
  
  List<Node> findNodeList(String paramString1, String paramString2)
    throws IFinFlowException;
  
  int getLastVersion(String paramString1, String paramString2);
  
  ParseReturnVo getJpdl(String paramString)
    throws IFinFlowException;
  
  byte[] getTemplatePicture(String paramString1, String paramString2, int paramInt);
  
  void updateDefaultRejectValue(String paramString1, String paramString2, String paramString3)
    throws IFinFlowException;
  
  Template getTemplate(String paramString1, String paramString2, int paramInt);
}
