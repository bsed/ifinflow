package com.ruimin.ifinflow.model.flowmodel.xml;

import com.ruimin.ifinflow.framework.parser.ITreeNode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;

public class Template implements ITreeNode, Serializable {
    private static final long serialVersionUID = 1L;
    private String handle;
    private String packageHandle;
    private String templateId;
    private String name;
    private String remark;
    private int version;
    private String creatorId;
    private String creatorName;
    private Date createdTime;
    private String lastModifierId;
    private String lastModifierName;
    private Date lastModifiedTime;
    private Date deployedTime;
    private String effectType;
    private Date appointTime;
    private String deploymentId;
    private String isDeployed;
    private String cmd;
    private Set<DesignTemplate> designTemplateSet = null;


    private int priority;


    private String isLock;


    private String firstTaskCommit;


    private String packageId;


    public String getHandle() {
        return this.handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getPackageHandle() {
        return this.packageHandle;
    }

    public void setPackageHandle(String packageHandle) {
        this.packageHandle = packageHandle;
    }

    public String getTemplateId() {
        return this.templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return this.remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getVersion() {
        return this.version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getCreatorId() {
        return this.creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorName() {
        return this.creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public Date getCreatedTime() {
        return this.createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getLastModifierId() {
        return this.lastModifierId;
    }

    public void setLastModifierId(String lastModifierId) {
        this.lastModifierId = lastModifierId;
    }

    public String getLastModifierName() {
        return this.lastModifierName;
    }

    public void setLastModifierName(String lastModifierName) {
        this.lastModifierName = lastModifierName;
    }

    public Date getLastModifiedTime() {
        return this.lastModifiedTime;
    }

    public void setLastModifiedTime(Date lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public Date getDeployedTime() {
        return this.deployedTime;
    }

    public void setDeployedTime(Date deployedTime) {
        this.deployedTime = deployedTime;
    }

    public String getEffectType() {
        return this.effectType;
    }

    public void setEffectType(String effectType) {
        this.effectType = effectType;
    }

    public Date getAppointTime() {
        return this.appointTime;
    }

    public void setAppointTime(Date appointTime) {
        this.appointTime = appointTime;
    }

    public String getDeploymentId() {
        return this.deploymentId;
    }

    public void setDeploymentId(String deploymentId) {
        this.deploymentId = deploymentId;
    }

    public String getIsDeployed() {
        return this.isDeployed;
    }

    public void setIsDeployed(String isDeployed) {
        this.isDeployed = isDeployed;
    }

    public Set<DesignTemplate> getDesignTemplateSet() {
        return this.designTemplateSet;
    }

    public void setDesignTemplateSet(Set<DesignTemplate> designTemplateSet) {
        this.designTemplateSet = designTemplateSet;
    }

    public int getPriority() {
        return this.priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getIsLock() {
        return this.isLock;
    }

    public void setIsLock(String isLock) {
        this.isLock = isLock;
    }

    public Collection<ITreeNode> children() {
        return new ArrayList();
    }

    public Element element() {
        Element e = DocumentHelper.createElement("template");
        e.addAttribute("ID", getHandle());
        e.addAttribute("handle", getHandle());
        e.addAttribute("packageHandle", getPackageHandle());
        e.addAttribute("id", getTemplateId());
        e.addAttribute("name", getName());
        e.addAttribute("version", String.valueOf(getVersion()));
        e.addAttribute("nameAndVersion", getName() + "_v" + getVersion());
        e.addAttribute("deploymentId", String.valueOf(getDeploymentId()));
        return e;
    }

    public String getFirstTaskCommit() {
        return this.firstTaskCommit;
    }

    public void setFirstTaskCommit(String firstTaskCommit) {
        this.firstTaskCommit = firstTaskCommit;
    }

    public String getPackageId() {
        return this.packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }

    public String getCmd() {
        return this.cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }
}