package com.ruimin.ifinflow.model.flowmodel.manage.xml;

import com.ruimin.ifinflow.engine.pvm.activity.AutoActivity;
import com.ruimin.ifinflow.engine.pvm.activity.DecisionJoinActivity;
import com.ruimin.ifinflow.engine.pvm.activity.RuleActivity;
import com.ruimin.ifinflow.engine.pvm.activity.TimerActivity;
import com.ruimin.ifinflow.engine.pvm.activity.TimerExitListener;
import com.ruimin.ifinflow.engine.pvm.activity.VirtualActivity;
import com.ruimin.ifinflow.model.flowmodel.external.IModelService;
import com.ruimin.ifinflow.model.flowmodel.manage.xml.parse.vo.ParseReturnVo;
import com.ruimin.ifinflow.model.flowmodel.xml.AssignPolicy;
import com.ruimin.ifinflow.model.flowmodel.xml.DesignTemplate;
import com.ruimin.ifinflow.model.flowmodel.xml.Node;
import com.ruimin.ifinflow.model.flowmodel.xml.NodeEvent;
import com.ruimin.ifinflow.model.flowmodel.xml.NodeTime;
import com.ruimin.ifinflow.model.flowmodel.xml.NodeTimeLimit;
import com.ruimin.ifinflow.model.flowmodel.xml.Route;
import com.ruimin.ifinflow.model.flowmodel.xml.Template;
import com.ruimin.ifinflow.model.flowmodel.xml.TemplateEvent;
import com.ruimin.ifinflow.model.flowmodel.xml.TemplatePackage;
import com.ruimin.ifinflow.model.flowmodel.xml.TemplateTimeLimit;
import com.ruimin.ifinflow.model.flowmodel.xml.TemplateVariable;
import com.ruimin.ifinflow.model.flowmodel.xml.VarTaskMapping;
import com.ruimin.ifinflow.model.util.DateUtil;
import com.ruimin.ifinflow.model.util.UUIDGenerator;
import com.ruimin.ifinflow.util.exception.IFinFlowException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbpm.pvm.internal.util.XmlUtil;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class Parser {
    private static Log log = LogFactory.getLog(Parser.class);

    private final String EVENT_TYPE_START = "1";
    private final String EVENT_TYPE_END = "2";
    private final String EVENT_TYPE_CANCEL = "3";


    private final String EVENT_START_STRING = "start";
    private final String EVENT_END_STRING = "end";
    private final String EVENT_CANCEL_STRING = "cancelprocess";


    private final String NODE_EVENT_START_STRING = "start";
    private final String NODE_EVENT_END_STRING = "end";

    private final String DECISION_NODE_PREFIX = "DecisionNode_";
    private final String FORK_NODE_PREFIX = "ForkNode_";
    private final String SUB_PROCESS_NODE_PREFIX = "SubFlowNode_";
    private final String JOIN_NODE_PREFIX = "JoinNode_";


    private IModelService modelService = null;


    private String nodeHandle = null;


    private List<Route> routeList = new ArrayList();


    private List<Node> nodeList = new ArrayList();


    private Template template = null;


    private boolean persistent = false;


    private Element jpdlRoot = null;


    private Document xmlDoc = null;


    Element root = null;


    private ParseReturnVo returnVo;

    private boolean check = true;


    public Parser(IModelService modelService) {
        this.modelService = modelService;
        this.returnVo = new ParseReturnVo();
    }


    public Parser(IModelService modelService, boolean persistent) {
        this.modelService = modelService;
        this.persistent = persistent;
        this.returnVo = new ParseReturnVo();
        init();
    }

    public Parser(IModelService modelService, boolean persistent, boolean check) {
        this.modelService = modelService;
        this.persistent = persistent;
        this.check = check;
        this.returnVo = new ParseReturnVo();
        init();
    }


    public ParseReturnVo parseJpdlXml(String jpdlStr, int flag)
            throws IFinFlowException {
        log.trace("解析xml开始。。。 ");
        this.root = XmlHandle.getRootElement(jpdlStr);
        if (null == this.root) {
            throw new IFinFlowException(110008);
        }

        for (Element childElement : XmlUtil.elements(this.root)) {
            String nodeName = childElement.getNodeName();
            if ("properties".equals(nodeName)) {
                parseProperties(childElement, flag);
            } else if ("start".equals(nodeName)) {
                parseActivity(childElement, "start", null);
            } else if ("end".equals(nodeName)) {
                parseActivity(childElement, "end", null);
            } else if ("task".equals(nodeName)) {
                parseActivity(childElement, "task", null);
            } else if ("auto".equals(nodeName)) {
                parseActivity(childElement, "custom", "auto");
            } else if ("wait".equals(nodeName)) {
                parseActivity(childElement, "state", "wait");
            } else if ("timer".equals(nodeName)) {
                parseActivity(childElement, "custom", "timer");
            } else if ("fork".equals(nodeName)) {
                parseActivity(childElement, "fork", null);
            } else if ("join".equals(nodeName)) {
                parseActivity(childElement, "join", null);
            } else if ("simplejoin".equals(nodeName)) {
                parseActivity(childElement, "custom", "simplejoin");
            } else if ("decision".equals(nodeName)) {
                parseActivity(childElement, "decision", null);
            } else if ("sub-process".equals(nodeName)) {
                int quorum = getQuorumValue(childElement);
                if (quorum < 2) {
                    parseActivity(childElement, "sub-process", null);
                }
            } else if ("rule".equals(nodeName)) {
                parseActivity(childElement, "custom", "rule");
            } else if ("virtual".equals(nodeName)) {
                parseActivity(childElement, "custom", "virtual");
            }
        }

        if (this.persistent) {
            saveTemplate(this.template, jpdlStr);
            saveNodeAndTran(this.routeList, this.nodeList);
        }

        XmlHandle.output(this.xmlDoc);
        String jpdlResult = null;
        try {
            jpdlResult = XmlHandle.toFormatedXML(this.xmlDoc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.returnVo.setJpdl(jpdlResult.replaceAll("&amp;", "&"));
        log.trace("解析xml结束。。。 ");
        return this.returnVo;
    }


    private int getQuorumValue(Element childElement) {
        int quorum = 1;
        List<Element> properties = XmlHandle.elements(childElement, "properties");

        if ((properties != null) && (properties.size() > 0)) {
            List<Element> bases = XmlHandle.elements((Element) properties.get(0), "base");
            if ((bases != null) && (bases.size() > 0)) {
                List<Element> quorums = XmlHandle.elements((Element) bases.get(0), "quorum");

                if ((quorums != null) && (quorums.size() > 0)) {
                    quorum = Integer.parseInt(((Element) quorums.get(0)).getTextContent());
                }
            }
        }
        return quorum;
    }


    private void parseProperties(Element element, int flag)
            throws IFinFlowException {
        for (Element subEle : XmlUtil.elements(element)) {
            if ("base".equals(subEle.getNodeName())) {
                parseBase(subEle, flag);
            } else if ("variables".equals(subEle.getNodeName())) {
                try {
                    if (this.persistent) {
                        parseVariables(subEle);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    log.error("调用operateVariable方法错误:" + e);
                }
            } else if ("events".equals(subEle.getNodeName())) {
                parseEvent(subEle, 0, this.jpdlRoot);
            } else if ("timelimits".equals(subEle.getNodeName())) {
                parseTimelimits(subEle, 0, this.jpdlRoot);
            }
        }
    }


    private void parseTimelimits(Element element, int flag, Element jpdlElement) {
        TemplateTimeLimit templateTimeLimit = null;
        NodeTimeLimit nodeTimeLimit = null;
        String businessTime = null;
        for (Element eventsEle : XmlUtil.elements(element)) {
            int consumeTime = 0;
            StringBuilder consumeBusinessTime = new StringBuilder();
            String adapterType = null;
            String adapterName = null;
            for (Element eventEle : XmlUtil.elements(eventsEle)) {
                String value = XmlHandle.dealString(eventEle.getTextContent());
                if ("refcalendar".equals(eventEle.getNodeName())) {
                    businessTime = value;
                } else if ("consumeday".equals(eventEle.getNodeName())) {
                    if (StringUtils.equals(businessTime, "1")) {
                        consumeBusinessTime.append(value).append(" business days");
                    } else {
                        consumeTime += Integer.parseInt(value) * 24 * 60 * 60;
                        if (flag == 0) {
                            templateTimeLimit = new TemplateTimeLimit();
                            templateTimeLimit.setConsumeDay(Integer.parseInt(value));
                        } else {
                            nodeTimeLimit = new NodeTimeLimit();
                            nodeTimeLimit.setConsumeDay(Integer.parseInt(value));
                        }
                    }
                } else if ("consumehour".equals(eventEle.getNodeName())) {
                    if (StringUtils.equals(businessTime, "1")) {
                        consumeBusinessTime.append("+").append(value).append(" business hours");
                    } else {
                        consumeTime += Integer.parseInt(value) * 60 * 60;
                        if (flag == 0) {
                            templateTimeLimit.setConsumeHour(Integer.parseInt(value));
                        } else {
                            nodeTimeLimit.setConsumeHour(Integer.parseInt(value));
                        }
                    }
                } else if ("consumeminute".equals(eventEle.getNodeName())) {
                    if (StringUtils.equals(businessTime, "1")) {
                        consumeBusinessTime.append("+").append(value).append(" business minutes");
                    } else {
                        consumeTime += Integer.parseInt(value) * 60;
                        if (flag == 0) {
                            templateTimeLimit.setConsumeMinute(Integer.parseInt(value));
                        } else {
                            nodeTimeLimit.setConsumeMinute(Integer.parseInt(value));
                        }
                    }
                } else if ("consumesecond".equals(eventEle.getNodeName())) {
                    if (StringUtils.equals(businessTime, "1")) {
                        consumeBusinessTime.append("+").append(value).append(" business seconds");
                    } else {
                        consumeTime += Integer.parseInt(value);
                        if (flag == 0) {
                            templateTimeLimit.setConsumeSecond(Integer.parseInt(value));
                        } else {
                            nodeTimeLimit.setConsumeSecond(Integer.parseInt(value));
                        }
                    }
                } else if ("adaptertype".equals(eventEle.getNodeName())) {
                    adapterType = value;
                    if (flag == 0) {
                        templateTimeLimit.setAdapterType(value);
                    } else {
                        nodeTimeLimit.setAdapterType(value);
                    }
                } else if ("adaptername".equals(eventEle.getNodeName())) {
                    adapterName = value;
                    if (flag == 0) {
                        templateTimeLimit.setAdapterName(value);
                    } else {
                        nodeTimeLimit.setAdapterName(value);
                    }
                }
            }
            if (consumeTime > 0) {
                parseOnElement(consumeTime, adapterType, adapterName, jpdlElement, consumeBusinessTime.toString());
            }
            if (this.persistent) {
                if (flag == 0) {
                    templateTimeLimit.setId(UUIDGenerator.generate(templateTimeLimit));

                    templateTimeLimit.setTemplateHandle(this.template.getHandle());
                    this.modelService.save(templateTimeLimit);
                } else {
                    nodeTimeLimit.setId(UUIDGenerator.generate(nodeTimeLimit));
                    nodeTimeLimit.setNodeHandle(this.nodeHandle);
                    nodeTimeLimit.setNodeKind(String.valueOf(flag));
                    this.modelService.save(nodeTimeLimit);
                }
            }
        }
    }


    private void parseBase(Element element, int flag)
            throws IFinFlowException {
        String packageId = null;
        String packageName = null;
        String packageHandle = null;
        String processName = null;
        String remark = null;
        for (Element templateEle : XmlUtil.elements(element)) {
            String nodeName = templateEle.getNodeName();
            String nodeValue = XmlHandle.dealString(templateEle.getTextContent());

            if (XmlHandle.isNotNull(nodeValue)) {
                if ("handle".equals(nodeName)) {
                    this.template = ((Template) this.modelService.get(Template.class, nodeValue));

                    if (null == this.template) {
                        this.template = new Template();
                        this.template.setHandle(nodeValue);
                    }
                    if (this.persistent) {
                        this.modelService.deleteTemplateData(nodeValue);
                    }
                } else if ("id".equals(nodeName)) {
                    processName = nodeValue;
                    this.template.setTemplateId(nodeValue);
                } else if ("name".equals(nodeName)) {
                    this.template.setName(nodeValue);
                } else if ("packageid".equals(nodeName)) {
                    packageId = nodeValue;
                } else if ("packagename".equals(nodeName)) {
                    packageName = nodeValue;
                } else if ("priority".equals(nodeName)) {
                    this.template.setPriority(Integer.parseInt(nodeValue));
                } else if ("remark".equals(nodeName)) {
                    remark = nodeValue;
                    this.template.setRemark(nodeValue);
                } else if ("version".equals(nodeName)) {
                    StringBuilder tempProcessName = new StringBuilder();
                    tempProcessName.append(packageId).append("_").append(processName);

                    this.template.setVersion(Integer.parseInt(nodeValue));

                    XmlHandle.setAttribute(this.jpdlRoot, "name", tempProcessName.toString());

                    XmlHandle.setAttribute(this.jpdlRoot, "version", nodeValue);
                    XmlHandle.setAttribute(this.jpdlRoot, "xmlns", "http://jbpm.org/4.4/jpdl");
                } else if ("creatorid".equals(nodeName)) {
                    this.template.setCreatorId(nodeValue);
                } else if ("creatorname".equals(nodeName)) {
                    this.template.setCreatorName(nodeValue);
                } else if ("createdtime".equals(nodeName)) {
                    this.template.setCreatedTime(DateUtil.convertStringToDate(nodeValue));
                } else if ("lastmodifierid".equals(nodeName)) {
                    this.template.setLastModifierId(nodeValue);
                } else if ("lastmodifiername".equals(nodeName)) {
                    this.template.setLastModifierName(nodeValue);
                } else if ("lastmodifiedtime".equals(nodeName)) {
                    this.template.setLastModifiedTime(DateUtil.convertStringToDate(nodeValue));
                } else if ("deployedtime".equals(nodeName)) {
                    this.template.setDeployedTime(DateUtil.convertStringToDate(nodeValue));
                } else if ("effecttype".equals(nodeName)) {
                    this.template.setEffectType(nodeValue);
                } else if ("appointtime".equals(nodeName)) {
                    this.template.setAppointTime(DateUtil.convertStringToDate(nodeValue));


                } else if (("packagehandle".equals(nodeName)) && (this.persistent)) {
                    packageHandle = nodeValue;
                    this.template.setPackageHandle(nodeValue);
                } else if ("firsttaskcommit".equals(nodeName)) {
                    this.template.setFirstTaskCommit(nodeValue);
                }
            }
        }
        if (flag != 0) {
            parsePackage(packageId, packageName, packageHandle);
        }

        if (StringUtils.isNotBlank(remark)) {
            createElement("description", remark, this.jpdlRoot);
        }
    }

    private void parsePackage(String packageId, String packageName, String packageHandle) throws IFinFlowException {
        if (StringUtils.isEmpty(packageId)) {
            throw new IFinFlowException(107003);
        }
        if (StringUtils.isEmpty(packageName)) {
            throw new IFinFlowException(107004);
        }
        if (StringUtils.isEmpty(packageHandle)) {
            throw new IFinFlowException(107005);
        }
        String[] pkgIds = packageId.split("\\.");
        String[] packageNames = packageName.split("\\.");
        int idLength = pkgIds.length;
        int nameLength = packageNames.length;
        if (idLength == nameLength) {
            StringBuilder pkgId = new StringBuilder();
            String pkgName = null;
            TemplatePackage superPackage = null;

            for (int i = 0; i < idLength; i++) {
                if (i != 0) {
                    pkgId.append(".");
                }
                pkgId.append(pkgIds[i]);
                pkgName = packageNames[i];
                TemplatePackage templatePackage = null;

                if (!this.modelService.isExist(pkgId.toString())) {
                    templatePackage = new TemplatePackage();
                    String handle = null;
                    if (idLength == i + 1) {
                        handle = packageHandle;
                    } else {
                        handle = UUIDGenerator.generate(templatePackage);
                    }
                    templatePackage.setHandle(handle);
                    templatePackage.setTemplatePackageId(pkgId.toString());
                    templatePackage.setName(pkgName);
                    templatePackage.setCurrentId(pkgIds[i]);
                    templatePackage.setParent(superPackage);
                    this.modelService.save(templatePackage);

                    superPackage = templatePackage;
                } else {
                    superPackage = this.modelService.findTemplate(pkgId.toString());
                    if (i == idLength - 1) {
                        this.template.setPackageHandle(superPackage.getHandle());
                    }
                }
                if (idLength == i + 1) {
                    this.returnVo.setPackageId(superPackage.getTemplatePackageId());
                }
            }
        } else {
            throw new IFinFlowException(107006);
        }
    }


    private void parseVariables(Element element)
            throws ParseException {
        for (Element variablesEle : XmlUtil.elements(element)) {
            TemplateVariable variable = new TemplateVariable();
            for (Element variablesSubEle : XmlUtil.elements(variablesEle)) {
                String nodeName = variablesSubEle.getNodeName();
                String nodeValue = XmlHandle.dealString(variablesSubEle.getTextContent());

                if (XmlHandle.isNotNull(nodeValue)) {
                    if ("name".equals(nodeName)) {
                        variable.setName(nodeValue);
                    } else if ("remark".equals(nodeName)) {
                        variable.setRemark(nodeValue);
                    } else if ("displayname".equals(nodeName)) {
                        variable.setDisplayName(nodeValue);
                    } else if ("type".equals(nodeName)) {
                        variable.setType(Integer.valueOf(Integer.parseInt(nodeValue)));
                    }
                }
            }

            variable.setHandle(UUIDGenerator.generate(variable));
            variable.setTemplateHandle(this.template.getHandle());
            this.modelService.save(variable);
        }
    }


    private void parseTransition(Element transSubElement, String elementName, Element activityElement) {
        Route route = new Route();
        String tempExrp = null;

        String to = null;


        Element transitionElement = createElement("transition", null, activityElement);

        for (Attr transSubEleAttr : XmlUtil.attributes(transSubElement)) {
            String nodeName = transSubEleAttr.getName();
            String nodeValue = XmlHandle.dealString(transSubEleAttr.getValue());
            if ("name".equals(nodeName)) {
                route.setName(nodeValue);
                XmlHandle.setAttribute(transitionElement, "name", nodeValue);
            } else if ("to".equals(nodeName)) {
                to = nodeValue;
                route.setToName(nodeValue);
                XmlHandle.setAttribute(transitionElement, "to", nodeValue);
            } else if (("content".equals(nodeName)) && ("decision".equals(elementName))) {

                tempExrp = nodeValue.replaceAll("\"", "&quot;").replaceAll("&&", "&amp;&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
            }
        }


        if ("decision".equals(elementName)) {
            tempExrp = tempExrp.replaceAll("\\#", "").replaceAll("\\{", "").replaceAll("\\}", "");


            createElement("condition", null, transitionElement, "expr", "#{" + tempExrp + "}");
        }

        route.setFromHandle(this.nodeHandle);
        route.setTemplateHandle(this.template.getHandle());
        this.routeList.add(route);

        Element forkEle = null;
        Element joinEle = null;

        if (to.startsWith("SubFlowNode_")) {
            Element subProcessEle = getSubProcessByRoot(to);
            int quorum = getQuorumValue(subProcessEle);

            if (quorum > 1) {
                List<Element> subProcessTranEles = XmlHandle.elements(subProcessEle, "transition");

                String joinTo = null;


                String forkId = "ForkNode_" + XmlHandle.randomString(5);
                forkEle = createElement("fork", null, this.jpdlRoot, "name", forkId);
                String forkHandle = this.nodeHandle;
                createNode(forkId, "5", 0);


                String joinId = "JoinNode_" + XmlHandle.randomString(5);
                joinEle = createElement("join", null, this.jpdlRoot, "name", joinId);
                String joinHandle = this.nodeHandle;
                createNode(joinId, "6", 0);

                if (subProcessTranEles != null) {
                    if (subProcessTranEles.size() > 1) {
                        parseMultiTransitions(joinId, subProcessTranEles);
                    } else {
                        Element tranEle = (Element) subProcessTranEles.get(0);
                        joinTo = XmlUtil.attribute(tranEle, "to");
                    }
                }


                if (StringUtils.isNotBlank(joinTo)) {
                    createElement("transition", null, joinEle, new String[]{"name", "to"}, new String[]{"", joinTo});


                    createRoute(joinTo, joinHandle);
                }


                XmlHandle.setAttribute(transitionElement, "to", forkId);
                route.setToName(forkId);

                for (int i = 0; i < quorum; i++) {
                    Element prop = (Element) XmlHandle.elements(subProcessEle, "properties").get(0);
                    Element subflow = (Element) XmlHandle.elements(prop, "subflow").get(0);

                    String subProcessHandle = null;
                    String toId = null;
                    Node subNode = null;

                    if (i == 0) {
                        toId = to;

                        subNode = createNode(toId, "12", 1);
                    } else {
                        toId = to + "_" + i;
                        subNode = createNode(toId, "12", 0);
                    }
                    subProcessHandle = this.nodeHandle;


                    createElement("transition", null, forkEle, new String[]{"name", "to"}, new String[]{"", toId});


                    createRoute(toId, forkHandle);


                    String subProcessId = ((Element) XmlHandle.elements(subflow, "template").get(0)).getTextContent();

                    String subProTag = "sub-process-id";

                    String persistnew = ((Element) XmlHandle.elements(prop, "persistnew").get(0)).getTextContent();
                    if (StringUtils.equals(persistnew, "1")) {
                        subProcessId = subProcessId.substring(0, subProcessId.lastIndexOf("-"));
                        subProTag = "sub-process-key";
                    }
                    subProcessId = subProcessId.replaceAll("\\.", "_");


                    Element subProEle = createElement("sub-process", null, this.jpdlRoot, new String[]{"name", subProTag}, new String[]{toId, subProcessId});


                    Element base = (Element) XmlHandle.elements(prop, "base").get(0);
                    String skipauth = ((Element) XmlHandle.elements(base, "skipauth").get(0)).getTextContent();
                    String rejectAuth = ((Element) XmlHandle.elements(base, "rejectauth").get(0)).getTextContent();
                    if (StringUtils.isNotBlank(skipauth)) {
                        subNode.setSkipAuth(skipauth);
                    }
                    if (StringUtils.isNotBlank(rejectAuth)) {
                        subNode.setRejectAuth(rejectAuth);
                    }

                    parseSubflow(subflow, subProEle, persistnew);

                    List<Element> eventList = XmlHandle.elements(prop, "events");
                    if ((eventList != null) && (!eventList.isEmpty())) {
                        Element eventElement = (Element) XmlHandle.elements(prop, "events").get(0);
                        parseEvent(eventElement, 1, subProEle);
                    }

                    createElement("transition", null, subProEle, new String[]{"name", "to"}, new String[]{"", joinId});


                    createRoute(toId, subProcessHandle);
                }
            }
        }
    }


    private Element getSubProcessByRoot(String nodeId) {
        Element subProcessEle = null;
        List<Element> subList = XmlHandle.elements(this.root, "sub-process");
        Iterator i$ = subList.iterator();
        if (i$.hasNext()) {
            Element e = (Element) i$.next();

            Element prop = (Element) XmlHandle.elements(e, "properties").get(0);
            Element base = (Element) XmlHandle.elements(prop, "base").get(0);

            if (nodeId.equals(((Element) XmlHandle.elements(base, "id").get(0)).getTextContent())) {
                subProcessEle = e;
            }
        }


        return subProcessEle;
    }


    public void parseActivity(Element element, String elementName, String realName)
            throws IFinFlowException {
        Node node = new Node();
        node.setTemplateHandle(this.template.getHandle());
        this.nodeHandle = UUIDGenerator.generate(node);
        node.setHandle(this.nodeHandle);
        this.nodeList.add(node);
        int nodeType = getNodeType(element.getNodeName());
        node.setKind(nodeType);


        Element activityElement = null;


        List<Element> transitionList = new ArrayList();

        int routeCount = 0;

        List<Element> routeList = XmlHandle.elements(element, "transition");
        if (routeList != null) {
            routeCount = routeList.size();
        }

        String persistNew = null;

        for (Element activitySubElement : XmlUtil.elements(element)) {
            if ("properties".equals(activitySubElement.getNodeName())) {
                activityElement = createElement(elementName, null, this.jpdlRoot);

                for (Element activitySubEle : XmlUtil.elements(activitySubElement)) {
                    if ("base".equals(activitySubEle.getNodeName())) {
                        parseBase(elementName, node, activitySubEle, realName, activityElement);
                    } else if ("assigners".equals(activitySubEle.getNodeName())) {
                        parseAssigners(activitySubEle, activityElement);
                    } else if ("reject".equals(activitySubEle.getNodeName())) {
                        parseReject(activitySubEle, node);
                    } else if ("events".equals(activitySubEle.getNodeName())) {
                        parseEvent(activitySubEle, nodeType, activityElement);
                    } else if ("timelimits".equals(activitySubEle.getNodeName())) {
                        parseTimelimits(activitySubEle, nodeType, activityElement);
                    } else if ("userexpansions".equals(activitySubEle.getNodeName())) {
                        if (this.persistent) {
                            parseUserExpansions(activitySubEle);
                        }
                    } else if ("persistnew".equals(activitySubEle.getNodeName())) {
                        persistNew = XmlHandle.dealString(activitySubEle.getTextContent());
                    } else if ("subflow".equals(activitySubEle.getNodeName())) {
                        parseSubflow(activitySubEle, activityElement, persistNew);
                    }
                }
            } else if ("transition".equals(activitySubElement.getNodeName())) {
                transitionList.add(activitySubElement);
                if ((routeCount == 1) || ("decision".equals(elementName)) || ("fork".equals(elementName))) {
                    parseTransition(activitySubElement, elementName, activityElement);
                }
            }
        }

        parseMultiTransitions(node.getNodeId(), transitionList);
    }


    private void parseMultiTransitions(String nodeId, List<Element> transitionList) {
        if ((transitionList.size() > 1) && (!nodeId.startsWith("DecisionNode_")) && (!nodeId.startsWith("ForkNode_"))) {
            String decisionId = "DecisionNode_" + XmlHandle.randomString(5);


            Element decisonEle = createElement("decision", null, this.jpdlRoot, "name", decisionId);

            String decisionHandle = null;
            if (this.persistent) {
                decisionHandle = this.nodeHandle;
                createNode(decisionId, "7", 0);
                createRoute(decisionId, this.nodeHandle);
            }

            for (Element transition : transitionList) {
                String content = XmlUtil.attribute(transition, "content");

                content = content.replaceAll("\"", "&quot;").replaceAll("&&", "&amp;&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;").replaceAll("\\#", "").replaceAll("\\{", "").replaceAll("\\}", "");


                String to = XmlUtil.attribute(transition, "to");

                Element transitionElement = createElement("transition", null, decisonEle, new String[]{"name", "to"}, new String[]{"to " + to, to});


                createElement("condition", null, transitionElement, "expr", "#{" + content + "}");


                if (this.persistent) {
                    createRoute(to, decisionHandle);
                }
            }


            Element taskEle = XmlHandle.element(this.jpdlRoot, "name", nodeId);

            createElement("transition", null, taskEle, new String[]{"name", "to"}, new String[]{"", decisionId});
        }
    }


    private Node createNode(String nodeId, String nodeType, int display) {
        Node node = new Node();
        node.setHandle(UUIDGenerator.generate(node));
        node.setTemplateHandle(this.template.getHandle());
        node.setNodeId(nodeId);
        node.setName(nodeId);
        node.setKind(Integer.parseInt(nodeType));

        node.setDisplay(display);
        this.nodeList.add(node);
        return node;
    }


    private void createRoute(String nodeId, String fromHandle) {
        Route route = new Route();
        route.setName("");
        route.setToName(nodeId);
        route.setFromHandle(fromHandle);
        route.setTemplateHandle(this.template.getHandle());
        this.routeList.add(route);
    }


    private void parseSubflow(Element element, Element jpdlElement, String persistNew)
            throws IFinFlowException {
        boolean hasIn = false;
        boolean hasOut = false;
        Element tempElement = null;
        for (Element subflowEle : XmlUtil.elements(element)) {
            String nodeName = subflowEle.getNodeName();
            String nodeValue = XmlHandle.dealString(subflowEle.getTextContent());

            if (("template".equals(nodeName)) && (this.check)) {
                if (StringUtils.isEmpty(nodeValue)) {
                    throw new IFinFlowException(102008);
                }

                String subProTag = "sub-process-id";

                if (StringUtils.equals(persistNew, "1")) {
                    nodeValue = nodeValue.substring(0, nodeValue.lastIndexOf("-"));
                    subProTag = "sub-process-key";
                }

                XmlHandle.setAttribute(jpdlElement, subProTag, nodeValue.replaceAll("\\.", "_"));
            } else if ("parent-subs".equals(nodeName)) {
                if (StringUtils.isNotBlank(nodeValue)) {
                    hasOut = true;
                }
                parseSubProcessParam(jpdlElement, subflowEle, "parameter-in");
            } else if ("sub-parents".equals(nodeName)) {
                if (StringUtils.isNotBlank(nodeValue)) {
                    hasIn = true;
                    tempElement = subflowEle;
                }
                parseSubProcessParam(jpdlElement, subflowEle, "parameter-out");
            }
        }
        if ((hasIn) && (!hasOut)) {
            parseSubProcessParam(jpdlElement, tempElement, "parameter-in");
        }
    }


    private void parseSubProcessParam(Element jpdlElement, Element subflowEle, String inOrOut) {
        for (Element parentSubsEle : XmlUtil.elements(subflowEle)) {
            String var = null;
            String subVar = null;
            for (Element parentSubEle : XmlUtil.elements(parentSubsEle)) {
                String parentSubName = parentSubEle.getNodeName();
                String parentSubValue = XmlHandle.dealString(parentSubEle.getTextContent());

                if (XmlHandle.isNotNull(parentSubValue)) {
                    if ("parentvar".equals(parentSubName)) {
                        var = parentSubValue;
                    } else if ("subvar".equals(parentSubName)) {
                        subVar = parentSubValue;
                    }
                }
            }
            createElement(inOrOut, null, jpdlElement, new String[]{"var", "subvar"}, new String[]{var, subVar});
        }
    }


    private void parseBase(String elementName, Node node, Element element, String realName, Element jpdlElement) {
        NodeTime nodeTime = null;
        String className = null;
        if ("timer".equals(realName)) {
            nodeTime = new NodeTime();
            className = TimerActivity.class.getName();
        } else if ("auto".equals(realName)) {
            className = AutoActivity.class.getName();
        } else if ("rule".equals(realName)) {
            className = RuleActivity.class.getName();
        } else if ("simplejoin".equals(realName)) {
            className = DecisionJoinActivity.class.getName();
        } else if ("virtual".equals(realName)) {
            className = VirtualActivity.class.getName();
        }


        long delayTime = 0L;

        long intervalTime = 0L;

        long continueTime = 0L;

        int cycleCount = 0;
        String remark = null;
        String classType = null;
        String extendsclass = null;

        String delayBusinessTimeFlag = null;
        StringBuilder delayBusinessTime = null;

        String intervalBusinessTimeFlag = null;
        StringBuilder intervalBusinessTime = null;

        String continueBusinessTimeFlag = null;
        StringBuilder continueBusinessTime = null;
        for (Element baseEle : XmlUtil.elements(element)) {
            String nodeName = baseEle.getNodeName();
            String nodeValue = XmlHandle.dealString(baseEle.getTextContent());
            if (XmlHandle.isNotNull(nodeValue)) {
                if ("id".equals(nodeName)) {
                    XmlHandle.setAttribute(jpdlElement, "name", nodeValue);
                    if ("custom".equals(elementName)) {
                        XmlHandle.setAttribute(jpdlElement, "class", className);
                    }
                    node.setNodeId(nodeValue);
                } else if ("name".equals(nodeName)) {
                    node.setName(nodeValue);
                } else if ("remark".equals(nodeName)) {
                    remark = nodeValue;
                    node.setRemark(nodeValue);
                } else if ("priority".equals(nodeName)) {
                    if ("task".equals(elementName)) {
                        XmlHandle.setAttribute(jpdlElement, "priority", nodeValue);
                    }

                    node.setPriority(Integer.parseInt(nodeValue));
                } else if ("url".equals(nodeName)) {
                    XmlHandle.setAttribute(jpdlElement, "form", nodeValue);
                    node.setUrl(nodeValue);
                } else if ("oprateauth".equals(nodeName)) {
                    node.setOprateAuth(baseEle.getTextContent());
                } else if ("skipauth".equals(nodeName)) {
                    node.setSkipAuth(nodeValue);
                } else if ("rejectauth".equals(nodeName)) {
                    node.setRejectAuth(nodeValue);
                } else if ("matchid".equals(nodeName)) {
                    node.setMatchId(nodeValue);
                } else if ("adaptertype".equals(nodeName)) {
                    if (!StringUtils.equals(realName, "timer")) {
                        parsEeventListenerField(jpdlElement, nodeValue, "classType", "string");
                    } else {
                        classType = nodeValue;
                    }
                    node.setAdapterType(nodeValue);
                } else if ("adaptername".equals(nodeName)) {
                    if (!StringUtils.equals(realName, "timer")) {
                        parsEeventListenerField(jpdlElement, nodeValue, "extendsclass", "string");
                    } else {
                        extendsclass = nodeValue;
                    }
                    node.setAdapterName(nodeValue);
                } else if ("delayrefcalendar".equals(nodeName)) {
                    delayBusinessTimeFlag = nodeValue;
                    if (StringUtils.equals(delayBusinessTimeFlag, "1")) {
                        delayBusinessTime = new StringBuilder();
                    }
                } else if ("delayday".equals(nodeName)) {
                    if ((StringUtils.equals(delayBusinessTimeFlag, "1")) && (!StringUtils.equals(nodeValue, "0"))) {
                        delayBusinessTime.append(nodeValue).append(" business days");
                    } else {
                        delayTime += Integer.parseInt(nodeValue) * 24 * 60 * 60;
                    }
                    nodeTime.setDelayDay(Integer.parseInt(nodeValue));
                } else if ("delayhour".equals(nodeName)) {
                    if ((StringUtils.equals(delayBusinessTimeFlag, "1")) && (!StringUtils.equals(nodeValue, "0"))) {
                        if (delayBusinessTime.length() != 0) {
                            delayBusinessTime.append("+");
                        }
                        delayBusinessTime.append(nodeValue).append(" business hours");
                    } else {
                        delayTime += Integer.parseInt(nodeValue) * 60 * 60;
                    }
                    nodeTime.setDelayHour(Integer.parseInt(nodeValue));
                } else if ("delayminute".equals(nodeName)) {
                    if ((StringUtils.equals(delayBusinessTimeFlag, "1")) && (!StringUtils.equals(nodeValue, "0"))) {
                        if (delayBusinessTime.length() != 0) {
                            delayBusinessTime.append("+");
                        }
                        delayBusinessTime.append(nodeValue).append(" business minutes");
                    } else {
                        delayTime += Integer.parseInt(nodeValue) * 60;
                    }
                    nodeTime.setDelayMinute(Integer.parseInt(nodeValue));
                } else if ("delaysecond".equals(nodeName)) {
                    if ((StringUtils.equals(delayBusinessTimeFlag, "1")) && (!StringUtils.equals(nodeValue, "0"))) {
                        if (delayBusinessTime.length() != 0) {
                            delayBusinessTime.append("+");
                        }
                        delayBusinessTime.append(nodeValue).append(" business seconds");
                    } else {
                        delayTime += Integer.parseInt(nodeValue);
                    }
                    nodeTime.setDelaySecond(Integer.parseInt(nodeValue));
                } else if ("cycletype".equals(nodeName)) {
                    nodeTime.setCycleType(nodeValue);
                } else if ("cyclecount".equals(nodeName)) {
                    cycleCount = Integer.parseInt(nodeValue);
                    nodeTime.setCycleCount(Integer.parseInt(nodeValue));
                } else if ("intervalrefcalendar".equals(nodeName)) {
                    intervalBusinessTimeFlag = nodeValue;
                    if (StringUtils.equals(intervalBusinessTimeFlag, "1")) {
                        intervalBusinessTime = new StringBuilder();
                    }
                } else if ("intervalday".equals(nodeName)) {
                    if ((StringUtils.equals(intervalBusinessTimeFlag, "1")) && (!StringUtils.equals(nodeValue, "0"))) {
                        intervalBusinessTime.append(nodeValue).append(" business days");
                    } else {
                        intervalTime += Integer.parseInt(nodeValue) * 24 * 60 * 60;
                    }
                    nodeTime.setIntervalDay(Integer.parseInt(nodeValue));
                } else if ("intervalhour".equals(nodeName)) {
                    if ((StringUtils.equals(intervalBusinessTimeFlag, "1")) && (!StringUtils.equals(nodeValue, "0"))) {
                        if (intervalBusinessTime.length() != 0) {
                            intervalBusinessTime.append("+");
                        }
                        intervalBusinessTime.append(nodeValue).append(" business hours");
                    } else {
                        intervalTime += Integer.parseInt(nodeValue) * 60 * 60;
                    }
                    nodeTime.setIntervalHour(Integer.parseInt(nodeValue));
                } else if ("intervalminute".equals(nodeName)) {
                    if ((StringUtils.equals(intervalBusinessTimeFlag, "1")) && (!StringUtils.equals(nodeValue, "0"))) {
                        if (intervalBusinessTime.length() != 0) {
                            intervalBusinessTime.append("+");
                        }
                        intervalBusinessTime.append(nodeValue).append(" business minutes");
                    } else {
                        intervalTime += Integer.parseInt(nodeValue) * 60;
                    }
                    nodeTime.setIntervalMinute(Integer.parseInt(nodeValue));
                } else if ("intervalsecond".equals(nodeName)) {
                    if ((StringUtils.equals(intervalBusinessTimeFlag, "1")) && (!StringUtils.equals(nodeValue, "0"))) {
                        if (intervalBusinessTime.length() != 0) {
                            intervalBusinessTime.append("+");
                        }
                        intervalBusinessTime.append(nodeValue).append(" business seconds");
                    } else {
                        intervalTime += Integer.parseInt(nodeValue);
                    }
                    nodeTime.setIntervalSecond(Integer.parseInt(nodeValue));
                } else if ("continuerefcalendar".equals(nodeName)) {
                    continueBusinessTimeFlag = nodeValue;
                    if (StringUtils.equals(continueBusinessTimeFlag, "1")) {
                        continueBusinessTime = new StringBuilder();
                    }
                } else if ("continueday".equals(nodeName)) {
                    if ((StringUtils.equals(continueBusinessTimeFlag, "1")) && (!StringUtils.equals(nodeValue, "0"))) {
                        continueBusinessTime.append(nodeValue).append(" business days");
                    } else {
                        continueTime += Integer.parseInt(nodeValue) * 24 * 60 * 60;
                    }
                    nodeTime.setContinueDay(Integer.parseInt(nodeValue));
                } else if ("continuehour".equals(nodeName)) {
                    if ((StringUtils.equals(continueBusinessTimeFlag, "1")) && (!StringUtils.equals(nodeValue, "0"))) {
                        if (continueBusinessTime.length() != 0) {
                            continueBusinessTime.append("+");
                        }
                        continueBusinessTime.append(nodeValue).append(" business hours");
                    } else {
                        continueTime += Integer.parseInt(nodeValue) * 60 * 60;
                    }
                    nodeTime.setContinueHour(Integer.parseInt(nodeValue));
                } else if ("continueminute".equals(nodeName)) {
                    if ((StringUtils.equals(continueBusinessTimeFlag, "1")) && (!StringUtils.equals(nodeValue, "0"))) {
                        if (continueBusinessTime.length() != 0) {
                            continueBusinessTime.append("+");
                        }
                        continueBusinessTime.append(nodeValue).append(" business minutes");
                    } else {
                        continueTime += Integer.parseInt(nodeValue) * 60;
                    }
                    nodeTime.setContinueMinute(Integer.parseInt(nodeValue));
                } else if ("continuesecond".equals(nodeName)) {
                    if ((StringUtils.equals(continueBusinessTimeFlag, "1")) && (!StringUtils.equals(nodeValue, "0"))) {
                        if (continueBusinessTime.length() != 0) {
                            continueBusinessTime.append("+");
                        }
                        continueBusinessTime.append(nodeValue).append(" business seconds");
                    } else {
                        continueTime += Integer.parseInt(nodeValue);
                    }
                    nodeTime.setContinueSecond(Integer.parseInt(nodeValue));
                }
            }
        }
        if (StringUtils.isNotBlank(remark)) {
            createElement("description", remark, jpdlElement);
        }

        if ((delayTime > 0L) || (StringUtils.equals(delayBusinessTimeFlag, "1"))) {
            String commonTimeUnit = " seconds";
            String tmpDelay = null;
            if (StringUtils.equals(delayBusinessTimeFlag, "1")) {
                tmpDelay = delayBusinessTime.toString();
            } else {
                tmpDelay = String.valueOf(delayTime) + commonTimeUnit;
            }
            String tmpInterval = null;
            if (StringUtils.equals(intervalBusinessTimeFlag, "1")) {
                tmpInterval = intervalBusinessTime.toString();
            } else {
                tmpInterval = String.valueOf(intervalTime) + commonTimeUnit;
            }
            String tmpContinue = null;
            if (StringUtils.equals(continueBusinessTimeFlag, "1")) {
                tmpContinue = continueBusinessTime.toString();
            } else {
                tmpContinue = String.valueOf(continueTime) + commonTimeUnit;
            }

            Element onElement = createElement("on", null, jpdlElement);
            XmlHandle.setAttribute(onElement, "event", "timeout");

            Element timerElement = createElement("timer", null, onElement, "duedate", tmpDelay);


            if ((intervalTime > 0L) || (StringUtils.equals(intervalBusinessTimeFlag, "1"))) {
                XmlHandle.setAttribute(timerElement, "repeat", tmpInterval);
            }

            Element eventListenerElement = createElement("event-listener", null, onElement);

            XmlHandle.setAttribute(eventListenerElement, "class", TimerExitListener.class.getName());


            parsEeventListenerField(eventListenerElement, classType, "classType", "string");

            parsEeventListenerField(eventListenerElement, extendsclass, "extendsclass", "string");

            parsEeventListenerField(eventListenerElement, tmpDelay, "duedate", "string");
            parsEeventListenerField(eventListenerElement, tmpContinue, "duration", "string");
            parsEeventListenerField(eventListenerElement, tmpInterval, "repeat", "string");
            parsEeventListenerField(eventListenerElement, String.valueOf(cycleCount), "cycleCount", "int");
        }

        if ((null != nodeTime) && (this.persistent)) {
            nodeTime.setHandle(this.nodeHandle);
            this.modelService.save(nodeTime);
        }
    }


    private void parsEeventListenerField(Element jpdlElement, String fieldSubEleValue, String namePropValue, String fieldSubEleName) {
        Element fieldElement = createElement("field", null, jpdlElement, "name", namePropValue);


        createElement(fieldSubEleName, null, fieldElement, "value", fieldSubEleValue);
    }


    private void parseAssigners(Element element, Element activityElement) {
        StringBuilder sysAssignParam = new StringBuilder();
        parseAssignersParam(element, sysAssignParam);


        Element assignmentHandlerElement = createElement("assignment-handler", null, activityElement, "class", "com.ruimin.ifinflow.engine.assign.AssignmentHandlerImpl");


        parsEeventListenerField(assignmentHandlerElement, sysAssignParam.toString(), "top_sys_assign_param", "string");
    }


    private void parseUserExpansions(Element element) {
        for (Element extEle : XmlUtil.elements(element)) {
            VarTaskMapping varTask = new VarTaskMapping();
            varTask.setId(UUIDGenerator.generate(varTask));
            varTask.setNodeHandle(this.nodeHandle);

            for (Element varSubEle : XmlUtil.elements(extEle)) {
                String value = varSubEle.getTextContent().trim();
                if ("userexpcolumn".equals(varSubEle.getNodeName())) {
                    varTask.setUserExtColume(value);
                } else if ("variablename".equals(varSubEle.getNodeName())) {
                    varTask.setVariableName(value);
                }
            }
            this.modelService.save(varTask);
        }
    }


    private void parseReject(Element element, Node node) {
        for (Element assignerEle : XmlUtil.elements(element)) {
            String nodeName = assignerEle.getNodeName();
            String value = XmlHandle.dealString(assignerEle.getTextContent());
            if ("rejectauth".equals(nodeName)) {
                node.setRejectAuth(value);
            } else if ("rejectnodeid".equals(nodeName)) {
                node.setRejectDefaultNodeId(value);
            } else if ("rejectassigntype".equals(nodeName)) {
                node.setRejectAssignType(value);
            } else if ("rejectdefault".equals(nodeName)) {
                node.setRejectDefault(value);
            } else if ("rejectdcontinue".equals(nodeName)) {
                node.setRejectdContinue(value);
            }
        }
    }


    private int getNodeType(String elementName) {
        int result = -1;
        if ("start".equals(elementName)) {
            result = 1;
        } else if ("end".equals(elementName)) {
            result = 2;
        } else if ("task".equals(elementName)) {
            result = 3;
        } else if ("auto".equals(elementName)) {
            result = 4;
        } else if ("fork".equals(elementName)) {
            result = 5;
        } else if ("join".equals(elementName)) {
            result = 6;
        } else if ("decision".equals(elementName)) {
            result = 7;
        } else if ("simplejoin".equals(elementName)) {
            result = 8;
        } else if ("wait".equals(elementName)) {
            result = 9;
        } else if ("timer".equals(elementName)) {
            result = 10;
        } else if ("rule".equals(elementName)) {
            result = 11;
        } else if ("sub-process".equals(elementName)) {
            result = 12;
        } else if ("virtual".equals(elementName)) {
            result = 13;
        }
        return result;
    }


    private void parseAssignersParam(Element element, StringBuilder sb) {
        AssignPolicy policy = new AssignPolicy();
        policy.setHandle(this.nodeHandle);

        for (Element assignerEle : XmlUtil.elements(element)) {
            String nodeName = assignerEle.getNodeName();
            String nodeValue = assignerEle.getTextContent().trim();
            if (("assignmode".equals(nodeName)) || ("ptkind".equals(nodeName)) || ("ptassign".equals(nodeName)) || ("pthistory".equals(nodeName)) || ("result".equals(nodeName))) {


                sb.append(assignerEle.getTextContent().trim());
                sb.append(",");
            }
            if (XmlHandle.isNotNull(nodeValue)) {
                if ("assignmode".equals(nodeName)) {
                    policy.setAssignMode(Integer.parseInt(nodeValue));
                } else if ("exittype".equals(nodeName)) {
                    policy.setExitType(nodeValue);
                } else if ("exitcount".equals(nodeName)) {
                    policy.setExitCount(Integer.parseInt(nodeValue));
                } else if ("ptkind".equals(nodeName)) {
                    policy.setParticipantType(Integer.parseInt(nodeValue));
                } else if ("ptassign".equals(nodeName)) {
                    policy.setParticipantAssign(Integer.parseInt(nodeValue));
                } else if ("pthistory".equals(nodeName)) {
                    policy.setParticipantHistory(Integer.parseInt(nodeValue));
                } else if ("result".equals(nodeName)) {
                    policy.setResult(nodeValue);
                }
            }
        }
        if (this.persistent) {
            this.modelService.save(policy);
        }
        sb.deleteCharAt(sb.length() - 1);
    }


    private void parseEvent(Element eventElement, int flag, Element jpdlElement) {
        TemplateEvent templateEvent = null;
        NodeEvent nodeEvent = null;
        String adapterName = null;
        String adapterType = null;
        String typeStr = null;

        Element onElement = null;
        for (Element eventsEle : XmlUtil.elements(eventElement)) {
            for (Element eventEle : XmlUtil.elements(eventsEle)) {
                if ("type".equals(eventEle.getNodeName())) {
                    typeStr = getTypeStr(eventEle.getTextContent().trim(), flag);
                    if (flag == 0) {
                        templateEvent = new TemplateEvent();
                        templateEvent.setType(Integer.parseInt(eventEle.getTextContent().trim()));
                    } else {
                        nodeEvent = new NodeEvent();
                        nodeEvent.setType(Integer.parseInt(eventEle.getTextContent().trim()));
                    }


                    onElement = createElement("on", null, jpdlElement);
                    XmlHandle.setAttribute(onElement, "event", typeStr);
                } else if ("adaptername".equals(eventEle.getNodeName())) {
                    adapterName = eventEle.getTextContent().trim();

                    if (flag == 0) {
                        templateEvent.setAdapterName(eventEle.getTextContent().trim());
                    } else {
                        nodeEvent.setAdapterName(eventEle.getTextContent().trim());
                    }
                } else if ("adaptertype".equals(eventEle.getNodeName())) {
                    adapterType = eventEle.getTextContent().trim();
                    if (flag == 0) {
                        templateEvent.setAdapterType(eventEle.getTextContent().trim());
                    } else {
                        nodeEvent.setAdapterType(eventEle.getTextContent().trim());
                    }
                }
            }


            if ((StringUtils.isNotEmpty(adapterName)) && (StringUtils.isNotEmpty(adapterType))) {

                Element eventListenerElement = createElement("event-listener", null, onElement);

                XmlHandle.setAttribute(eventListenerElement, "class", "com.ruimin.ifinflow.engine.internal.adapter.CallBusinessAdapter");


                parsEeventListenerField(eventListenerElement, adapterName, "extendsclass", "string");

                parsEeventListenerField(eventListenerElement, adapterType, "classType", "string");
            }

            if (this.persistent) {
                if (flag == 0) {
                    templateEvent.setId(UUIDGenerator.generate(templateEvent));
                    templateEvent.setTemplateHandle(this.template.getHandle());
                    this.modelService.save(templateEvent);
                } else {
                    nodeEvent.setId(UUIDGenerator.generate(nodeEvent));
                    nodeEvent.setNodeHandle(this.nodeHandle);

                    nodeEvent.setNodeKind("2");
                    this.modelService.save(nodeEvent);
                }
            }
        }
    }


    private String getTypeStr(String eventType, int flag) {
        String result = null;
        if ("1".equals(eventType)) {
            if (flag == 0) {
                result = "start";
            } else {
                result = "start";
            }
        } else if ("2".equals(eventType)) {
            if (flag == 0) {
                result = "end";
            } else {
                result = "end";
            }
        } else if ("3".equals(eventType)) {
            result = "cancelprocess";
        }
        return result;
    }


    private void saveNodeAndTran(List<Route> routeList, List<Node> nodeList) {
        for (Node node : nodeList) {
            this.modelService.save(node);
        }
        for (Route route : routeList) {
            String toName = route.getToName();
            for (Node node : nodeList) {
                if ((XmlHandle.isNotNull(toName)) && (toName.equals(node.getNodeId()))) {
                    route.setToHandle(node.getHandle());
                    break;
                }
            }
            
        }
    }

    private void saveTemplate(Template template, String jpdlStr) {
        DesignTemplate designTemplate = null;

        Set<DesignTemplate> designTemplateSet = template.getDesignTemplateSet();
        if ((null != designTemplateSet) && (designTemplateSet.size() > 0)) {
            designTemplate = (DesignTemplate) designTemplateSet.iterator().next();
        } else {
            designTemplate = new DesignTemplate();
            designTemplate.setHandle(UUIDGenerator.generate(designTemplate));
            Set<DesignTemplate> aDesignTemplateSet = new HashSet();
            aDesignTemplateSet.add(designTemplate);
            template.setDesignTemplateSet(aDesignTemplateSet);
        }
        designTemplate.setContent(jpdlStr);
        designTemplate.setTemplate(template);
        designTemplate.setPackageHandle(template.getPackageHandle());
        designTemplate.setDesignTemplateId(template.getTemplateId());
        designTemplate.setName(template.getName());
        designTemplate.setRemark(template.getRemark());
        designTemplate.setVersion(template.getVersion());
        designTemplate.setIsDeployed(template.getIsDeployed());

        template.setIsDeployed("0");

        this.modelService.update(template);
    }

    public void init() {
        buildDocument();
        buildRoot();
    }

    private void buildDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            log.debug("创建DocumentBuilder成功。");
            this.xmlDoc = builder.newDocument();
            log.debug("创建xml Document成功。");
        } catch (ParserConfigurationException e) {
            log.error("创建DocumentBuilder错误:" + e);
        }
    }

    private void buildRoot() {
        this.jpdlRoot = this.xmlDoc.createElement("process");
        this.xmlDoc.appendChild(this.jpdlRoot);
    }

    private void parseOnElement(long consumeTime, String adapterType, String adapterName, Element jpdlElement, String businessTime) {
        Element onElement = createElement("on", null, jpdlElement);
        XmlHandle.setAttribute(onElement, "event", "timeout");

        String dueDate = null;
        if (businessTime.contains("business")) {
            dueDate = businessTime;
        } else {
            dueDate = String.valueOf(consumeTime) + " seconds";
        }

        Element timerElement = createElement("timer", null, onElement);
        XmlHandle.setAttribute(timerElement, "duedate", dueDate);


        Element eventListenerElement = createElement("event-listener", null, onElement);

        XmlHandle.setAttribute(eventListenerElement, "class", "com.ruimin.ifinflow.engine.internal.adapter.CallOvertimeAdapter");


        parsEeventListenerField(eventListenerElement, adapterType, "classType", "string");

        parsEeventListenerField(eventListenerElement, adapterName, "extendsclass", "string");

        parsEeventListenerField(eventListenerElement, adapterName, "extendsclass", "string");
    }

    private Element createElement(String name, String content, Element parent) {
        Element element = this.xmlDoc.createElement(name);
        return XmlHandle.createElement(element, name, content, parent);
    }

    private Element createElement(String name, String content, Element parent, String[] propNames, String[] propValues) {
        Element element = this.xmlDoc.createElement(name);
        return XmlHandle.createElement(element, name, content, parent, propNames, propValues);
    }

    private Element createElement(String name, String content, Element parent, String propName, String propValue) {
        Element element = this.xmlDoc.createElement(name);
        return XmlHandle.createElement(element, name, content, parent, propName, propValue);
    }
}