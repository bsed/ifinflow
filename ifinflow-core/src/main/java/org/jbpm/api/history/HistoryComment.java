package org.jbpm.api.history;

import java.util.List;

public abstract interface HistoryComment extends HistoryDetail {
	public abstract String getMessage();

	public abstract List<? extends HistoryComment> getReplies();
}
