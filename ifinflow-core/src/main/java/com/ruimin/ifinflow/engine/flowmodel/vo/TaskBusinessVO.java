package com.ruimin.ifinflow.engine.flowmodel.vo;

import org.apache.commons.lang.StringUtils;

public class TaskBusinessVO extends BaseStatisticVO {
	private static final long serialVersionUID = 1L;
	private String templateKey;
	private String templateName;

	public TaskBusinessVO(String templateKey, String total, String doneNum,
			String todoNum, String overtimeNum, String backNum,
			String exceptionNum) {
		super(total, doneNum, todoNum, overtimeNum, backNum, exceptionNum);
		this.templateKey = templateKey;
	}

	public String getTemplateName() {
		return this.templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getTemplateKey() {
		return this.templateKey;
	}

	public void setTemplateKey(String templateKey) {
		this.templateKey = templateKey;
	}

	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}

		if (((obj instanceof TaskBusinessVO))
				&& (StringUtils.equals(((TaskBusinessVO) obj).getTemplateKey(),
						this.templateKey))) {
			return true;
		}

		return false;
	}
}
