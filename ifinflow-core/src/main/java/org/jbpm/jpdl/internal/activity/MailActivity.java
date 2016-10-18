package org.jbpm.jpdl.internal.activity;

import java.util.Collection;
import javax.mail.Message;
import org.jbpm.api.model.OpenExecution;
import org.jbpm.pvm.internal.email.spi.MailProducer;
import org.jbpm.pvm.internal.email.spi.MailSession;
import org.jbpm.pvm.internal.env.EnvironmentImpl;
import org.jbpm.pvm.internal.wire.usercode.UserCodeReference;

public class MailActivity extends JpdlAutomaticActivity {
	private UserCodeReference mailProducerReference;
	private static final long serialVersionUID = 1L;

	void perform(OpenExecution execution) throws Exception {
		MailProducer mailProducer = (MailProducer) this.mailProducerReference
				.getObject(execution);
		Collection<Message> messages = mailProducer.produce(execution);
		((MailSession) EnvironmentImpl.getFromCurrent(MailSession.class))
				.send(messages);
	}

	public UserCodeReference getMailProducerReference() {
		return this.mailProducerReference;
	}

	public void setMailProducerReference(UserCodeReference mailProducer) {
		this.mailProducerReference = mailProducer;
	}
}
