package com.ruimin.ifinflow.engine.flowmodel.vo;

import java.io.Serializable;

public class BaseStatisticVO implements Serializable {
	private static final long serialVersionUID = 1L;
	protected String total;
	protected String doneNum;
	protected String todoNum;
	protected String overtimeNum;
	protected String backNum;
	protected String exceptionNum;

	public BaseStatisticVO(String total, String doneNum, String todoNum,
			String overtimeNum, String backNum, String exceptionNum) {
		this.total = total;
		this.doneNum = doneNum;
		this.todoNum = todoNum;
		this.overtimeNum = overtimeNum;
		this.backNum = backNum;
		this.exceptionNum = exceptionNum;
	}

	public String getTotal() {
		return this.total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getDoneNum() {
		return this.doneNum;
	}

	public void setDoneNum(String doneNum) {
		this.doneNum = doneNum;
	}

	public String getTodoNum() {
		return this.todoNum;
	}

	public void setTodoNum(String todoNum) {
		this.todoNum = todoNum;
	}

	public String getOvertimeNum() {
		return this.overtimeNum;
	}

	public void setOvertimeNum(String overtimeNum) {
		this.overtimeNum = overtimeNum;
	}

	public String getBackNum() {
		return this.backNum;
	}

	public void setBackNum(String backNum) {
		this.backNum = backNum;
	}

	public String getExceptionNum() {
		return this.exceptionNum;
	}

	public void setExceptionNum(String exceptionNum) {
		this.exceptionNum = exceptionNum;
	}
}
