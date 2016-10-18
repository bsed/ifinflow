package org.jbpm.jpdl.internal.activity;

import org.jbpm.pvm.internal.builder.ActivityBehaviourBuilder;
import org.jbpm.pvm.internal.builder.ActivityBuilder;
import org.jbpm.pvm.internal.wire.Descriptor;

public class DecisionBuilder extends ActivityBehaviourBuilder {
	public DecisionBuilder(ActivityBuilder activityBuilder) {
		super(activityBuilder);
	}

	public DecisionBuilder handler(Descriptor descriptor) {
		return null;
	}
}
