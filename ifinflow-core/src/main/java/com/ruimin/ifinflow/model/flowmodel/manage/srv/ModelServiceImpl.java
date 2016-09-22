package com.ruimin.ifinflow.model.flowmodel.manage.srv;

import com.ruimin.ifinflow.model.flowmodel.cache.TemplateCache;
import com.ruimin.ifinflow.model.flowmodel.external.IModelService;
import com.ruimin.ifinflow.model.flowmodel.manage.command.pkg.GetAllPackageCmd;
import com.ruimin.ifinflow.model.flowmodel.manage.command.pkg.JudgePkgExistCmd;
import com.ruimin.ifinflow.model.flowmodel.manage.command.pkg.JudgeTemplateIdExistCmd;
import com.ruimin.ifinflow.model.flowmodel.manage.command.template.GetTemplateCmd;
import com.ruimin.ifinflow.model.flowmodel.manage.command.template.GetTemplatesCmd;
import com.ruimin.ifinflow.model.flowmodel.manage.command.template.GetXmlCmd;
import com.ruimin.ifinflow.model.flowmodel.manage.command.test.TestCmd;
import com.ruimin.ifinflow.model.flowmodel.manage.express.ExpressionValidation;
import com.ruimin.ifinflow.model.flowmodel.manage.xml.Parser;
import com.ruimin.ifinflow.model.flowmodel.manage.xml.Validation;
import com.ruimin.ifinflow.model.flowmodel.manage.xml.XmlHandle;
import com.ruimin.ifinflow.model.flowmodel.manage.xml.parse.vo.ParseReturnVo;
import com.ruimin.ifinflow.model.flowmodel.xml.*;
import com.ruimin.ifinflow.model.util.MD5;
import com.ruimin.ifinflow.model.util.UUIDGenerator;
import com.ruimin.ifinflow.util.exception.IFinFlowException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbpm.pvm.internal.cmd.*;
import org.jbpm.pvm.internal.repository.DeploymentProperty;
import org.jbpm.pvm.internal.svc.AbstractServiceImpl;
import org.jbpm.pvm.internal.util.XmlUtil;
import org.w3c.dom.Element;

import java.io.Serializable;
import java.util.*;

public class ModelServiceImpl extends AbstractServiceImpl implements IModelService<Template> {
    private final String ID_TAG = "<id>";
    private final String ID_END_TAG = "</id>";
    private final String PROPERTIES_END_TAG = "</properties>";
    private static Log log = LogFactory.getLog(ModelServiceImpl.class);
    protected Class<Template> entityClass;

    public List<TemplatePackage> getAllPackage() {
        return (List) this.commandService.execute(new GetAllPackageCmd());
    }

    public Template save(Template entity) {
        return (Template) this.commandService.execute(new SaveEntityCmd<Template>(entity));
    }

    public boolean isExist(String packageId) {
        return this.commandService.execute(new JudgePkgExistCmd(packageId)).booleanValue();
    }

    public Template get(Class<Template> entityClass, Serializable id) {
        return this.commandService.execute(new GetEntityCmd<Template>(entityClass, id));
    }

    public List<Template> getTemplates(String packageHandle, boolean cascade) {
        return (List) this.commandService.execute(new GetTemplatesCmd(packageHandle, cascade));
    }


    public void test() {
        this.commandService.execute(new TestCmd());
    }

    public void update(Template entity) {
        this.commandService.execute(new UpdateEntityCmd(entity));
    }

    public void delete(Template entity) {
        this.commandService.execute(new DeleteEntityCmd(entity));
    }

    public List<?> queryByHql(String hql) throws IFinFlowException {
        return (List) this.commandService.execute(new QueryByHqlCmd(hql));
    }

    public ParseReturnVo parseJpdl(String jpdlStr) throws Exception {
        Parser parser = new Parser(this, true);
        return parser.parseJpdlXml(jpdlStr, 0);
    }

    public ParseReturnVo parseJpdl(String jpdlStr, boolean check)
            throws Exception {
        Parser parser = new Parser(this, true, check);
        return parser.parseJpdlXml(jpdlStr, 0);
    }

    public String getXml(String templateHandle) {
        return (String) this.commandService.execute(new GetXmlCmd(templateHandle));
    }

    public String getXml(String packageId, String templateId, int version) {
        return (String) this.commandService.execute(new GetXmlCmd(packageId, templateId, version));
    }

    public Integer updateByHql(String hql) {
        return this.commandService.execute(new ExecuteUpdateCmd(hql));
    }

    public boolean checkExpression(String expression, List<String> varList) throws IFinFlowException {
        if (StringUtils.isNotBlank(expression)) {
            if (ExpressionValidation.match(expression)) {
                return ExpressionValidation.checkExpress(expression, varList);
            }
        } else {
            throw new IFinFlowException(110006);
        }
        return false;
    }

    public boolean saveDesigningTemplate(String handle, String packageHandle, String id, String name, String content)
            throws IFinFlowException {
        Template template = get(Template.class, handle);
        DesignTemplate designTemplate = null;
        if (null != template) {
            Set<DesignTemplate> designTemplateSet = template.getDesignTemplateSet();

            if ((null != designTemplateSet) && (designTemplateSet.size() > 0)) {
                designTemplate = designTemplateSet.iterator().next();
            } else {
                designTemplate = new DesignTemplate();
                designTemplate.setHandle(UUIDGenerator.generate(designTemplate));

                Set<DesignTemplate> aDesignTemplateSet = new HashSet();
                aDesignTemplateSet.add(designTemplate);
                template.setDesignTemplateSet(aDesignTemplateSet);
            }
            designTemplate.setContent(content);
            designTemplate.setTemplate(template);
            designTemplate.setPackageHandle(template.getPackageHandle());
            designTemplate.setDesignTemplateId(template.getTemplateId());
            designTemplate.setName(template.getName());
            designTemplate.setRemark(template.getRemark());
            designTemplate.setVersion(template.getVersion());
            designTemplate.setIsDeployed(template.getIsDeployed());
            update(template);
        } else {
            throw new IFinFlowException(107002, handle);
        }

        return true;
    }


    public boolean saveDesigningTemplate(String handle, String packageHandle, String id, String name, String content, byte[] pictures)
            throws IFinFlowException {
        Template template = get(Template.class, handle);
        DesignTemplate designTemplate = null;
        if (null != template) {
            Set<DesignTemplate> designTemplateSet = template.getDesignTemplateSet();

            if ((null != designTemplateSet) && (designTemplateSet.size() > 0)) {
                designTemplate = designTemplateSet.iterator().next();
            } else {
                designTemplate = new DesignTemplate();
                designTemplate.setHandle(UUIDGenerator.generate(designTemplate));

                Set<DesignTemplate> aDesignTemplateSet = new HashSet();
                aDesignTemplateSet.add(designTemplate);
                template.setDesignTemplateSet(aDesignTemplateSet);
            }
            designTemplate.setContent(content);
            designTemplate.setTemplate(template);
            designTemplate.setPackageHandle(template.getPackageHandle());
            designTemplate.setDesignTemplateId(template.getTemplateId());
            designTemplate.setName(template.getName());
            designTemplate.setRemark(template.getRemark());
            designTemplate.setVersion(template.getVersion());
            designTemplate.setIsDeployed(template.getIsDeployed());
            designTemplate.setPictureByte(pictures);
            update(template);
        } else {
            throw new IFinFlowException(107002, handle);
        }

        return true;
    }

    public boolean checkXml(String jpdlStr) throws IFinFlowException {
        return new Validation().checkXml(jpdlStr);
    }

    public boolean isExistOfTemplateId(String packageHandle, String templateId) throws IFinFlowException {
        return this.commandService.execute(new JudgeTemplateIdExistCmd(packageHandle, templateId)).booleanValue();
    }

    public String findDeploymentId(String packageId, String templateId, int version)
            throws IFinFlowException {
        StringBuilder hql = new StringBuilder("select t.deploymentId from ");
        hql.append(Template.class.getName());
        hql.append(" t, ");
        hql.append(TemplatePackage.class.getName());
        hql.append(" tp where t.packageHandle = tp.handle");
        hql.append(" and tp.templatePackageId = '");
        hql.append(packageId);
        hql.append("' and t.templateId = '");
        hql.append(templateId);
        hql.append("' and t.version = ");
        hql.append(version);

        List<String> list = (List) this.commandService.execute(new QueryByHqlCmd(hql.toString()));

        if ((null != list) && (!list.isEmpty())) {
            return list.get(0);
        }
        return null;
    }

    public boolean uninstallTemplate(String packageId, String templateId, int version) throws IFinFlowException {
        StringBuilder hql = new StringBuilder("select t.handle from ");
        hql.append(Template.class.getName());
        hql.append(" t, ");
        hql.append(TemplatePackage.class.getName());
        hql.append(" tp where t.packageHandle = tp.handle");
        hql.append(" and tp.templatePackageId = '");
        hql.append(packageId);
        hql.append("' and t.templateId = '");
        hql.append(templateId);
        hql.append("' and t.version = ");
        hql.append(version);

        List<Object[]> list = (List) this.commandService.execute(new QueryByHqlCmd(hql.toString()));

        if ((null != list) && (!list.isEmpty())) {
            Object[] template = list.get(0);

            deleteTemplateData((String) template[0], packageId);
        }
        return false;
    }

    public TemplateCache getTemplateCacheInfo(String deploymentId) {
        return null;
    }


    public void deleteTemplateData(String templateHandle, String packageId) {
        deleteTempalteInPackage(packageId);
        deleteTemplateData(templateHandle);
    }


    public void deleteTemplateData(String templateHandle) {
        deleteObject(TemplateVariable.class.getName(), templateHandle);
        deleteObject(TemplateEvent.class.getName(), templateHandle);
        deleteObject(TemplateTimeLimit.class.getName(), templateHandle);
        deleteObject(Route.class.getName(), templateHandle);


        try {
            //TODO:类型转换不确定
            List<String> list = (List<String>) queryByHql("select o.handle from " + Node.class.getName() + " o where o.templateHandle = '" + templateHandle + "'");


            for (String nodeHandle : list) {
                deleteOthers(NodeEvent.class.getName(), nodeHandle);
                deleteOthers(NodeTimeLimit.class.getName(), nodeHandle);
                deleteOthers(VarTaskMapping.class.getName(), nodeHandle);
                deleteOthers(NodeTime.class.getName(), nodeHandle);
                deleteOthers(AssignPolicy.class.getName(), nodeHandle);
            }
        } catch (Exception e) {
            log.error("调用deleteTemplateData异常", e);
            e.printStackTrace();
        }

        deleteObject(Node.class.getName(), templateHandle);
    }

    private int deleteTempalteInPackage(String packageId) {
        StringBuilder tempHql = new StringBuilder("delete from ");
        tempHql.append(TemplatePackage.class.getName());
        tempHql.append(" where id = '");
        tempHql.append(packageId);
        tempHql.append("'");
        return updateByHql(tempHql.toString()).intValue();
    }

    private int deleteOthers(String entityName, String nodeHandle) {
        StringBuilder tempHql = new StringBuilder("delete from ");
        tempHql.append(entityName);
        if ((NodeTime.class.getName().equals(entityName)) || (AssignPolicy.class.getName().equals(entityName))) {
            tempHql.append(" where handle = '");
        } else {
            tempHql.append(" where nodeHandle = '");
        }
        tempHql.append(nodeHandle);
        tempHql.append("'");
        return updateByHql(tempHql.toString()).intValue();
    }

    private int deleteObject(String entityName, String templateHandle) {
        StringBuilder tempHql = new StringBuilder("delete from ");
        tempHql.append(entityName);
        tempHql.append(" where templateHandle = '");
        tempHql.append(templateHandle);
        tempHql.append("'");
        return updateByHql(tempHql.toString()).intValue();
    }

    public boolean importCheck(String jpdlStr) {
        String tempStr = jpdlStr.substring(0, jpdlStr.indexOf("<check>"));
        String md5Str = jpdlStr.substring(jpdlStr.indexOf("<check>") + "<check>".length(), jpdlStr.indexOf("</check>"));

        return md5Str.equals(MD5.MD5Encode(tempStr));
    }

    public ParseReturnVo importXml(String jpdlXml)
            throws IFinFlowException {
        Parser parser = new Parser(this, true);
        return parser.parseJpdlXml(jpdlXml, 1);
    }

    public TemplatePackage findTemplate(String pkgId) throws IFinFlowException {
        StringBuilder hql = new StringBuilder();
        hql.append("from ");
        hql.append(TemplatePackage.class.getName());
        hql.append(" o where o.templatePackageId = '");
        hql.append(pkgId);
        hql.append("'");

        //TODO:类型不确定
        List<TemplatePackage> list = (List<TemplatePackage>) queryByHql(hql.toString());

        if (null != list) {
            if (list.size() == 1) {
                return list.get(0);
            }
            throw new IFinFlowException(101003, pkgId);
        }

        return null;
    }

    public List<TemplatePackage> findPackagesCascade(String packageHandle)
            throws IFinFlowException {
        return null;
    }


    public List<TemplateVariable> findTemplateVariables(String templateHandle)
            throws IFinFlowException {
        StringBuilder hql = new StringBuilder();
        hql.append("from ");
        hql.append(TemplateVariable.class.getName());
        hql.append(" o where o.templateHandle = '");
        hql.append(templateHandle);
        hql.append("'");
        return (List<TemplateVariable>) queryByHql(hql.toString());
    }

    public boolean hasChangeOfContent(String jpdlXml, String templateHandle) throws IFinFlowException {
        boolean hasChangeFlag = false;

        String G_SIGN = " {1}g{1}={1}\"{1}([\\d]|\\.|;|,|\r\n|\r|\n)+\"{1} {1}";


        String content = getTemplateContent(templateHandle);
        Map<String, String> oldNodeBlockMap = getNodeBlocks(content);


        Map<String, String> nodeBlockMap = getNodeBlocks(jpdlXml);

        for (Map.Entry<String, String> entry : nodeBlockMap.entrySet()) {
            String key = entry.getKey();
            String oldValue = oldNodeBlockMap.get(key);
            if (StringUtils.isNotBlank(oldValue)) {
                if (!entry.getValue().replaceAll(" {1}g{1}={1}\"{1}([\\d]|\\.|;|,|\r\n|\r|\n)+\"{1} {1}", " ").equals(oldValue.replaceAll(" {1}g{1}={1}\"{1}([\\d]|\\.|;|,|\r\n|\r|\n)+\"{1} {1}", " "))) {
                    hasChangeFlag = true;
                    break;
                }
            } else {
                hasChangeFlag = true;
                break;
            }
        }
        return hasChangeFlag;
    }

    private String getTemplateContent(String templateHandle) {
        StringBuilder hql = new StringBuilder();
        hql.append("select o.content from ");
        hql.append(DesignTemplate.class.getName());
        hql.append(" o where o.template = '");
        hql.append(templateHandle);
        hql.append("'");

        List<String> contents = (List<String>) queryByHql(hql.toString());
        String content = null;
        if ((contents != null) && (!contents.isEmpty())) {
            content = contents.get(0);
        } else {
            throw new IFinFlowException(107002, templateHandle);
        }
        return content;
    }

    private Map<String, String> getNodeBlocks(String jpdlXml) {
        Element root = XmlHandle.getRootElement(jpdlXml);
        if (null == root) {
            throw new IFinFlowException(110008);
        }
        Map<String, String> nodeBlockMap = new HashMap();

        for (Element subElement : XmlUtil.elements(root)) {
            String nodeName = subElement.getNodeName();
            String nodeText = null;
            if ("properties".equals(nodeName)) {
                nodeText = jpdlXml.substring(0, jpdlXml.indexOf("</properties>"));


                String propertiesText = null;
                propertiesText = nodeText.substring(0, nodeText.indexOf("<lastmodifierid"));

                if (nodeText.indexOf("<firsttaskcommit") >= 0) {
                    propertiesText = propertiesText + nodeText.substring(nodeText.indexOf("<firsttaskcommit"));
                }


                nodeBlockMap.put("properties", propertiesText);
                jpdlXml = jpdlXml.substring(jpdlXml.indexOf("</properties>"));
            } else {
                jpdlXml = splitElement(jpdlXml, nodeBlockMap, nodeName);
            }
        }
        return nodeBlockMap;
    }


    private String splitElement(String jpdlXml, Map<String, String> nodeBlockMap, String nodeName) {
        String idValue = jpdlXml.substring(jpdlXml.indexOf("<id>") + "<id>".length(), jpdlXml.indexOf("</id>"));


        StringBuilder sb = new StringBuilder();
        sb.append("<id>").append(idValue);
        String sbString = sb.toString();
        int start = jpdlXml.indexOf(sbString);
        sb = new StringBuilder();
        sb.append("</").append(nodeName).append(">");
        sbString = sb.toString();
        int end = jpdlXml.indexOf(sbString);
        sb = null;
        String nodeText = jpdlXml.substring(start, end).replaceAll("\r\n|\r|\n", "");

        jpdlXml = jpdlXml.substring(end + sbString.length());
        nodeBlockMap.put(idValue, nodeText);
        return jpdlXml;
    }

    public List<Node> findNodeList(String packageId, String templateId, int version, String nodeType)
            throws IFinFlowException {
        StringBuilder hql = new StringBuilder();
        hql.append("select n from ").append(Node.class.getName()).append(" n, ");

        hql.append(Template.class.getName()).append(" t, ");
        hql.append(TemplatePackage.class.getName()).append(" tp");
        hql.append(" where n.templateHandle = t.handle");
        hql.append(" and tp.templatePackageId = '").append(packageId).append("'");

        hql.append(" and tp.handle = t.packageHandle");
        if (version <= 0) {
            hql.append(" and t.version = ").append(getLastVersion(packageId, templateId));
        } else {
            hql.append(" and t.version = " + version);
        }
        hql.append(" and t.templateId = '").append(templateId).append("'");
        if (StringUtils.isNotBlank(nodeType)) {
            hql.append(" and n.kind = '").append(nodeType).append("'");
        }
        return (List<Node>) queryByHql(hql.toString());
    }

    public List<Node> findNodeList(String packageId, String templateId) throws IFinFlowException {
        return findNodeList(packageId, templateId, getLastVersion(packageId, templateId), null);
    }

    public int getLastVersion(String packageId, String templateId)
            throws IFinFlowException {
        StringBuilder hql = new StringBuilder();
        hql.append("select max(longValue) ");
        hql.append("from ");
        hql.append(DeploymentProperty.class.getName());
        hql.append(" where objectName = '" + packageId + "_" + templateId + "'");
        List<?> list = queryByHql(hql.toString());
        if (list.isEmpty()) {
            return -1;
        }
        Number num = (Number) list.get(0);
        return num == null ? -1 : num.intValue();
    }


    public ParseReturnVo getJpdl(String designXml)
            throws IFinFlowException {
        Parser parser = new Parser(this, false);
        return parser.parseJpdlXml(designXml, 0);
    }


    public byte[] getTemplatePicture(String packageId, String templateId, int version) {
        StringBuilder hql = new StringBuilder();
        hql.append("select t from ");
        hql.append(DesignTemplate.class.getName()).append(" t, ");
        hql.append(TemplatePackage.class.getName()).append(" tp");
        hql.append(" where ");
        hql.append(" t.templateId = '").append(templateId).append("'");
        hql.append(" and tp.templatePackageId = '").append(packageId).append("'");

        hql.append(" and tp.handle = t.packageHandle");
        if (version <= 0) {
            hql.append(" and t.version = ").append(getLastVersion(packageId, templateId));
        } else {
            hql.append(" and t.version = " + version);
        }


        List<DesignTemplate> list = (List<DesignTemplate>) queryByHql(hql.toString());

        DesignTemplate designTemplate = null;
        if (list.size() > 0) {
            designTemplate = list.get(0);
            return designTemplate.getPictureByte();
        }
        return null;
    }


    public void updateDefaultRejectValue(String taskId, String deploymentId, String defaultRejectValue)
            throws IFinFlowException {
        StringBuilder hql = new StringBuilder();

        hql.append("select t.handle from ").append(Template.class.getName()).append(" t").append(" where t.deploymentId = '").append(deploymentId).append("'");


        List<String> list = (List<String>) queryByHql(hql.toString());
        if ((list != null) && (list.size() > 0)) {
            hql = new StringBuilder();
            hql.append("update ").append(Node.class.getName()).append(" n").append(" set n.rejectDefault = '").append(defaultRejectValue).append("'").append(" where n.templateHandle = '").append(list.get(0)).append("'").append(" and n.nodeId = '").append(taskId).append("'");


            updateByHql(hql.toString());
        }
    }

    public Template getTemplate(String packageId, String templateId, int version) {
        if (version <= 0) {
            version = getLastVersion(packageId, templateId);
        }
        return this.commandService.execute(new GetTemplateCmd(packageId, templateId, version));
    }
}