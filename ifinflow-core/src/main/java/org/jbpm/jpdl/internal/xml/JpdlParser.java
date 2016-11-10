/*     */ package org.jbpm.jpdl.internal.xml;
/*     */ 
/*     */ import java.net.URL;
/*     */ import java.text.ParseException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.Enumeration;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import java.util.StringTokenizer;
/*     */ import org.jbpm.api.JbpmException;
/*     */ import org.jbpm.api.activity.ActivityBehaviour;
/*     */ import org.jbpm.api.listener.EventListener;
/*     */ import org.jbpm.internal.log.Log;
/*     */ import org.jbpm.jpdl.internal.activity.JpdlBinding;
/*     */ import org.jbpm.jpdl.internal.activity.MailListener;
/*     */ import org.jbpm.jpdl.internal.model.JpdlProcessDefinition;
/*     */ import org.jbpm.pvm.internal.el.Expression;
/*     */ import org.jbpm.pvm.internal.email.impl.MailProducerImpl;
/*     */ import org.jbpm.pvm.internal.email.impl.MailTemplate;
/*     */ import org.jbpm.pvm.internal.email.impl.MailTemplateRegistry;
/*     */ import org.jbpm.pvm.internal.env.EnvironmentImpl;
/*     */ import org.jbpm.pvm.internal.model.ActivityCoordinatesImpl;
/*     */ import org.jbpm.pvm.internal.model.ActivityImpl;
/*     */ import org.jbpm.pvm.internal.model.CompositeElementImpl;
/*     */ import org.jbpm.pvm.internal.model.Continuation;
/*     */ import org.jbpm.pvm.internal.model.EventImpl;
/*     */ import org.jbpm.pvm.internal.model.EventListenerReference;
/*     */ import org.jbpm.pvm.internal.model.ObservableElementImpl;
/*     */ import org.jbpm.pvm.internal.model.ProcessDefinitionImpl;
/*     */ import org.jbpm.pvm.internal.model.ScopeElementImpl;
/*     */ import org.jbpm.pvm.internal.model.TimerDefinitionImpl;
/*     */ import org.jbpm.pvm.internal.model.TransitionImpl;
/*     */ import org.jbpm.pvm.internal.model.VariableDefinitionImpl;
/*     */ import org.jbpm.pvm.internal.repository.DeploymentImpl;
/*     */ import org.jbpm.pvm.internal.task.AssignableDefinitionImpl;
/*     */ import org.jbpm.pvm.internal.task.SwimlaneDefinitionImpl;
/*     */ import org.jbpm.pvm.internal.task.TaskDefinitionImpl;
/*     */ import org.jbpm.pvm.internal.util.XmlUtil;
/*     */ import org.jbpm.pvm.internal.wire.Descriptor;
/*     */ import org.jbpm.pvm.internal.wire.binding.MailTemplateBinding;
/*     */ import org.jbpm.pvm.internal.wire.binding.ObjectBinding;
/*     */ import org.jbpm.pvm.internal.wire.descriptor.ObjectDescriptor;
/*     */ import org.jbpm.pvm.internal.wire.descriptor.ProvidedObjectDescriptor;
/*     */ import org.jbpm.pvm.internal.wire.usercode.UserCodeReference;
/*     */ import org.jbpm.pvm.internal.wire.xml.WireParser;
/*     */ import org.jbpm.pvm.internal.xml.Bindings;
/*     */ import org.jbpm.pvm.internal.xml.Parse;
/*     */ import org.jbpm.pvm.internal.xml.Parser;
/*     */ import org.w3c.dom.Element;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JpdlParser
/*     */   extends Parser
/*     */ {
/*  83 */   private static final Log log = Log.getLog(JpdlParser.class.getName());
/*     */   
/*     */   public static final String NAMESPACE_JPDL_40 = "http://jbpm.org/4.0/jpdl";
/*     */   
/*     */   public static final String NAMESPACE_JPDL_42 = "http://jbpm.org/4.2/jpdl";
/*     */   
/*     */   public static final String NAMESPACE_JPDL_43 = "http://jbpm.org/4.3/jpdl";
/*     */   
/*     */   public static final String NAMESPACE_JPDL_44 = "http://jbpm.org/4.4/jpdl";
/*     */   
/*     */   public static final String CURRENT_VERSION_JBPM = "4.4";
/*     */   public static final String CURRENT_VERSION_NAMESPACE = "http://jbpm.org/4.4/jpdl";
/*     */   public static final String CURRENT_VERSION_PROCESS_LANGUAGE_ID = "jpdl-4.4";
/*  96 */   private static final String[] SCHEMA_RESOURCES = { "jpdl-4.0.xsd", "jpdl-4.2.xsd", "jpdl-4.3.xsd", "jpdl-4.4.xsd" };
/*     */   
/*     */ 
/*     */ 
/*     */ 
/* 101 */   private static final String[] DEFAULT_BINDING_RESOURCES = { "jbpm.jpdl.bindings.xml", "jbpm.user.bindings.xml" };
/*     */   
/*     */ 
/* 104 */   private static JpdlBindingsParser jpdlBindingsParser = new JpdlBindingsParser();
/*     */   public static final String CATEGORY_ACTIVITY = "activity";
/*     */   public static final String CATEGORY_EVENT_LISTENER = "eventlistener";
/*     */   
/*     */   public JpdlParser()
/*     */   {
/* 110 */     parseBindings();
/* 111 */     setSchemaResources(SCHEMA_RESOURCES);
/*     */   }
/*     */   
/*     */   protected void parseBindings() {
/* 115 */     this.bindings = new Bindings();
/*     */     
/* 117 */     for (String activityResource : DEFAULT_BINDING_RESOURCES) {
/* 118 */       Enumeration<URL> resourceUrls = getResources(activityResource);
/* 119 */       if (resourceUrls.hasMoreElements()) {
/* 120 */         while (resourceUrls.hasMoreElements()) {
/* 121 */           URL resourceUrl = (URL)resourceUrls.nextElement();
/* 122 */           log.trace("loading jpdl bindings from resource: " + resourceUrl);
/* 123 */           jpdlBindingsParser.createParse().setUrl(resourceUrl).contextMapPut("bindings", this.bindings).execute().checkErrors("jpdl bindings from " + resourceUrl.toString());
/*     */         }
/*     */       }
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 131 */       log.trace("skipping unavailable jpdl activities resource: " + activityResource);
/*     */     }
/*     */   }
/*     */   
/*     */   protected Enumeration<URL> getResources(String resourceName)
/*     */   {
/* 137 */     ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
/*     */     Enumeration<URL> resourceUrls;
/*     */     try {
/* 140 */       resourceUrls = classLoader.getResources(resourceName);
/*     */       
/* 142 */       if (!resourceUrls.hasMoreElements()) {
/* 143 */         resourceUrls = JpdlParser.class.getClassLoader().getResources(resourceName);
/*     */       }
/*     */     }
/*     */     catch (Exception e) {
/* 147 */       throw new JbpmException("couldn't get resource urls for " + resourceName, e);
/*     */     }
/* 149 */     return resourceUrls;
/*     */   }
/*     */   
/*     */   public Object parseDocumentElement(Element documentElement, Parse parse) {
/* 153 */     JpdlProcessDefinition processDefinition = instantiateNewJpdlProcessDefinition();
/* 154 */     parse.contextStackPush(processDefinition);
/*     */     
/* 156 */     List<ProcessDefinitionImpl> processDefinitions = new ArrayList();
/* 157 */     processDefinitions.add(processDefinition);
/*     */     try
/*     */     {
/* 160 */       String name = XmlUtil.attribute(documentElement, "name", parse);
/* 161 */       processDefinition.setName(name);
/*     */       
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 167 */       DeploymentImpl deployment = (DeploymentImpl)parse.contextMapGet("deployment");
/*     */       
/* 169 */       if (deployment != null) {
/* 170 */         String processLanguageId = deployment.getProcessLanguageId(name);
/* 171 */         if (processLanguageId == null)
/*     */         {
/*     */ 
/*     */ 
/* 175 */           String jpdlParser = XmlUtil.attribute(documentElement, "jpdlparser");
/* 176 */           if (jpdlParser != null) {
/* 177 */             processLanguageId = "jpdl-" + jpdlParser;
/*     */ 
/*     */           }
/*     */           else
/*     */           {
/*     */ 
/* 183 */             jpdlParser = System.getProperty("jpdlparser");
/* 184 */             if (jpdlParser != null) {
/* 185 */               processLanguageId = "jpdl-" + jpdlParser;
/*     */ 
/*     */             }
/*     */             else
/*     */             {
/* 190 */               String namespaceUri = documentElement.getNamespaceURI();
/* 191 */               if (namespaceUri != null) {
/* 192 */                 processLanguageId = "jpdl-" + namespaceUri.substring(16, 19);
/*     */               }
/*     */               else
/*     */               {
/* 196 */                 processLanguageId = "jpdl-4.4";
/*     */               }
/*     */             }
/*     */           }
/*     */           
/*     */ 
/*     */ 
/* 203 */           deployment.setProcessLanguageId(name, processLanguageId);
/*     */         }
/* 205 */         parse.contextMapPut("proclangid", processLanguageId);
/*     */       }
/*     */       
/* 208 */       String packageName = XmlUtil.attribute(documentElement, "package");
/* 209 */       processDefinition.setPackageName(packageName);
/*     */       
/* 211 */       Integer version = XmlUtil.attributeInteger(documentElement, "version", parse);
/* 212 */       if (version != null) {
/* 213 */         processDefinition.setVersion(version.intValue());
/*     */       }
/*     */       
/* 216 */       String key = XmlUtil.attribute(documentElement, "key");
/* 217 */       if (key != null) {
/* 218 */         processDefinition.setKey(key);
/*     */       }
/*     */       
/* 221 */       Element descriptionElement = XmlUtil.element(documentElement, "description");
/* 222 */       if (descriptionElement != null) {
/* 223 */         String description = XmlUtil.getContentText(descriptionElement);
/* 224 */         processDefinition.setDescription(description);
/*     */       }
/*     */       
/* 227 */       UnresolvedTransitions unresolvedTransitions = new UnresolvedTransitions();
/* 228 */       parse.contextStackPush(unresolvedTransitions);
/*     */       
/*     */ 
/* 231 */       List<Element> swimlaneElements = XmlUtil.elements(documentElement, "swimlane");
/* 232 */       for (Element swimlaneElement : swimlaneElements) {
/* 233 */         String swimlaneName = XmlUtil.attribute(swimlaneElement, "name", parse);
/* 234 */         if (swimlaneName != null) {
/* 235 */           SwimlaneDefinitionImpl swimlaneDefinition = processDefinition.createSwimlaneDefinition(swimlaneName);
/*     */           
/* 237 */           parseAssignmentAttributes(swimlaneElement, swimlaneDefinition, parse);
/*     */         }
/*     */       }
/*     */       
/*     */ 
/* 242 */       parseVariableDefinitions(documentElement, parse, processDefinition);
/*     */       
/*     */ 
/* 245 */       parseOnEvents(documentElement, parse, processDefinition);
/*     */       
/*     */ 
/* 248 */       parseActivities(documentElement, parse, processDefinition);
/*     */       
/*     */ 
/* 251 */       resolveTransitionDestinations(parse, processDefinition, unresolvedTransitions);
/*     */       
/*     */ 
/* 254 */       Element migrationElement = XmlUtil.element(documentElement, "migrate-instances");
/* 255 */       if (migrationElement != null) {
/* 256 */         MigrationHelper.parseMigrationDescriptor(migrationElement, parse, processDefinition);
/*     */       }
/*     */     }
/*     */     finally {
/* 260 */       parse.contextStackPop();
/*     */     }
/*     */     
/* 263 */     if (processDefinition.getInitial() == null) {
/* 264 */       parse.addProblem("no start activity in process", documentElement);
/*     */     }
/*     */     
/* 267 */     return processDefinitions;
/*     */   }
/*     */   
/*     */   protected JpdlProcessDefinition instantiateNewJpdlProcessDefinition() {
/* 271 */     return new JpdlProcessDefinition();
/*     */   }
/*     */   
/*     */   protected void resolveTransitionDestinations(Parse parse, JpdlProcessDefinition processDefinition, UnresolvedTransitions unresolvedTransitions)
/*     */   {
/* 276 */     for (UnresolvedTransition unresolvedTransition : unresolvedTransitions.list) {
/* 277 */       unresolvedTransition.resolve(processDefinition, parse);
/*     */     }
/*     */   }
/*     */   
/*     */   public void parseActivities(Element documentElement, Parse parse, CompositeElementImpl compositeElement)
/*     */   {
/* 283 */     List<Element> elements = XmlUtil.elements(documentElement);
/* 284 */     for (Element nestedElement : elements) {
/* 285 */       String tagName = nestedElement.getLocalName();
/* 286 */       if ((!"on".equals(tagName)) && (!"timer".equals(tagName)) && (!"swimlane".equals(tagName)) && (!"migrate-instances".equals(tagName)) && (!"description".equals(tagName)))
/*     */       {
/*     */ 
/*     */ 
/* 290 */         JpdlBinding activityBinding = (JpdlBinding)getBinding(nestedElement, "activity");
/* 291 */         if (activityBinding == null) {
/* 292 */           log.debug("unrecognized activity: " + tagName);
/*     */         }
/*     */         else
/*     */         {
/* 296 */           ActivityImpl activity = compositeElement.createActivity();
/* 297 */           parse.contextStackPush(activity);
/*     */           try {
/* 299 */             activity.setType(activityBinding.getTagName());
/* 300 */             activityBinding.parseName(nestedElement, activity, parse);
/*     */             
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 306 */             String actName = activity.getName();
/* 307 */             if (actName != null) {
/* 308 */               if (actName.startsWith("Start")) {
/* 309 */                 activity.setType("1");
/*     */               }
/* 311 */               else if (actName.startsWith("End")) {
/* 312 */                 activity.setType("2");
/*     */               }
/* 314 */               else if (actName.startsWith("Task")) {
/* 315 */                 activity.setType("3");
/*     */               }
/* 317 */               else if (actName.startsWith("Auto")) {
/* 318 */                 activity.setType("4");
/*     */               }
/* 320 */               else if (actName.startsWith("Fork")) {
/* 321 */                 activity.setType("5");
/*     */               }
/* 323 */               else if (actName.startsWith("Join")) {
/* 324 */                 activity.setType("6");
/*     */               }
/* 326 */               else if (actName.startsWith("Decision")) {
/* 327 */                 activity.setType("7");
/*     */               }
/* 329 */               else if (actName.startsWith("SJoin")) {
/* 330 */                 activity.setType("8");
/*     */               }
/* 332 */               else if (actName.startsWith("Wait")) {
/* 333 */                 activity.setType("9");
/*     */               }
/* 335 */               else if (actName.startsWith("Timer")) {
/* 336 */                 activity.setType("10");
/*     */               }
/* 338 */               else if (actName.startsWith("Rule")) {
/* 339 */                 activity.setType("11");
/*     */               }
/* 341 */               else if (actName.startsWith("SubFlow")) {
/* 342 */                 activity.setType("12");
/* 343 */               } else if (actName.startsWith("Virtual")) {
/* 344 */                 activity.setType("13");
/*     */               }
/*     */             }
/*     */             
/* 348 */             parseTransitions(nestedElement, activity, parse);
/* 349 */             parseVariableDefinitions(nestedElement, parse, activity);
/*     */             
/* 351 */             Element descriptionElement = XmlUtil.element(nestedElement, "description");
/* 352 */             if (descriptionElement != null) {
/* 353 */               String description = XmlUtil.getContentText(descriptionElement);
/* 354 */               activity.setDescription(description);
/*     */             }
/*     */             
/* 357 */             String continuationText = XmlUtil.attribute(nestedElement, "continue");
/* 358 */             if (continuationText != null) {
/* 359 */               if ("async".equals(continuationText)) {
/* 360 */                 activity.setContinuation(Continuation.ASYNCHRONOUS);
/*     */               }
/* 362 */               else if ("exclusive".equals(continuationText)) {
/* 363 */                 activity.setContinuation(Continuation.EXCLUSIVE);
/*     */               }
/*     */             }
/*     */             
/* 367 */             Object parseResult = activityBinding.parse(nestedElement, parse, this);
/* 368 */             if ((parseResult instanceof ActivityBehaviour)) {
/* 369 */               ActivityBehaviour activityBehaviour = (ActivityBehaviour)parseResult;
/* 370 */               activity.setActivityBehaviour(activityBehaviour);
/*     */             }
/*     */             else {
/* 373 */               Descriptor activityBehaviourDescriptor = (Descriptor)parseResult;
/* 374 */               activity.setActivityBehaviourDescriptor(activityBehaviourDescriptor);
/*     */             }
/*     */             
/* 377 */             parseOnEvents(nestedElement, parse, activity);
/*     */             
/* 379 */             String g = XmlUtil.attribute(nestedElement, "g");
/* 380 */             if (g == null)
/*     */             {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/* 405 */               parse.contextStackPop();
/*     */             }
/*     */             else
/*     */             {
/* 383 */               StringTokenizer stringTokenizer = new StringTokenizer(g, ",");
/* 384 */               ActivityCoordinatesImpl coordinates = null;
/* 385 */               if (stringTokenizer.countTokens() == 4) {
/*     */                 try {
/* 387 */                   int x = Integer.parseInt(stringTokenizer.nextToken());
/* 388 */                   int y = Integer.parseInt(stringTokenizer.nextToken());
/* 389 */                   int width = Integer.parseInt(stringTokenizer.nextToken());
/* 390 */                   int height = Integer.parseInt(stringTokenizer.nextToken());
/* 391 */                   coordinates = new ActivityCoordinatesImpl(x, y, width, height);
/*     */                 }
/*     */                 catch (NumberFormatException e) {
/* 394 */                   coordinates = null;
/*     */                 }
/*     */               }
/* 397 */               if (coordinates != null) {
/* 398 */                 activity.setCoordinates(coordinates);
/*     */               }
/*     */               else {
/* 401 */                 parse.addProblem("invalid coordinates g=\"" + g + "\" in " + activity, nestedElement);
/*     */               }
/*     */             }
/*     */           } finally {
/* 405 */             parse.contextStackPop();
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/* 412 */   public TimerDefinitionImpl parseTimerDefinition(Element timerElement, Parse parse, ScopeElementImpl scopeElement) { TimerDefinitionImpl timerDefinition = scopeElement.createTimerDefinition();
/*     */     
/* 414 */     String duedate = XmlUtil.attribute(timerElement, "duedate");
/* 415 */     String duedatetime = XmlUtil.attribute(timerElement, "duedatetime");
/*     */     
/* 417 */     if (duedate != null) {
/* 418 */       timerDefinition.setDueDateDescription(duedate);
/*     */     }
/* 420 */     else if (duedatetime != null) {
/* 421 */       String dueDateTimeFormatText = (String)EnvironmentImpl.getFromCurrent("jbpm.duedatetime.format", false);
/*     */       
/* 423 */       if (dueDateTimeFormatText == null) {
/* 424 */         dueDateTimeFormatText = "HH:mm dd/MM/yyyy";
/*     */       }
/* 426 */       SimpleDateFormat dateFormat = new SimpleDateFormat(dueDateTimeFormatText);
/*     */       try {
/* 428 */         Date duedatetimeDate = dateFormat.parse(duedatetime);
/* 429 */         timerDefinition.setDueDate(duedatetimeDate);
/*     */       }
/*     */       catch (ParseException e) {
/* 432 */         parse.addProblem("couldn't parse duedatetime " + duedatetime, e);
/*     */       }
/*     */     }
/*     */     else {
/* 436 */       parse.addProblem("either duedate or duedatetime is required in timer", timerElement);
/*     */     }
/*     */     
/* 439 */     String repeat = XmlUtil.attribute(timerElement, "repeat");
/* 440 */     timerDefinition.setRepeat(repeat);
/*     */     
/* 442 */     return timerDefinition;
/*     */   }
/*     */   
/*     */   public void parseOnEvents(Element element, Parse parse, ScopeElementImpl scopeElement)
/*     */   {
/* 447 */     List<Element> onElements = XmlUtil.elements(element, "on");
/* 448 */     for (Element onElement : onElements) {
/* 449 */       String eventName = XmlUtil.attribute(onElement, "event", parse);
/* 450 */       parseOnEvent(onElement, parse, scopeElement, eventName);
/*     */       
/* 452 */       Element timerElement = XmlUtil.element(onElement, "timer");
/* 453 */       if (timerElement != null) {
/* 454 */         TimerDefinitionImpl timerDefinitionImpl = parseTimerDefinition(timerElement, parse, scopeElement);
/*     */         
/* 456 */         timerDefinitionImpl.setEventName(eventName);
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void parseOnEvent(Element element, Parse parse, ObservableElementImpl observableElement, String eventName) { EventImpl event;
/*     */     String continuationText;
/* 463 */     if (eventName != null) {
/* 464 */       event = observableElement.getEvent(eventName);
/* 465 */       if (event == null) {
/* 466 */         event = observableElement.createEvent(eventName);
/*     */       }
/*     */       
/* 469 */       continuationText = XmlUtil.attribute(element, "continue");
/* 470 */       if (continuationText != null) {
/* 471 */         if ("async".equals(continuationText)) {
/* 472 */           event.setContinuation(Continuation.ASYNCHRONOUS);
/*     */         }
/* 474 */         else if ("exclusive".equals(continuationText)) {
/* 475 */           event.setContinuation(Continuation.EXCLUSIVE);
/*     */         }
/*     */       }
/*     */       
/* 479 */       for (Element eventListenerElement : XmlUtil.elements(element)) {
/* 480 */         JpdlBinding eventBinding = (JpdlBinding)getBinding(eventListenerElement, "eventlistener");
/*     */         
/* 482 */         if (eventBinding != null) {
/* 483 */           EventListenerReference eventListenerReference = null;
/* 484 */           Object parseResult = eventBinding.parse(eventListenerElement, parse, this);
/* 485 */           if ((parseResult instanceof EventListener)) {
/* 486 */             EventListener eventListener = (EventListener)parseResult;
/* 487 */             eventListenerReference = event.createEventListenerReference(eventListener);
/*     */           }
/*     */           else {
/* 490 */             Descriptor eventListenerDescriptor = (Descriptor)parseResult;
/* 491 */             eventListenerReference = event.createEventListenerReference(eventListenerDescriptor);
/*     */           }
/*     */           
/*     */ 
/* 495 */           Boolean propagationEnabled = XmlUtil.attributeBoolean(eventListenerElement, "propagation", parse);
/*     */           
/* 497 */           if (propagationEnabled != null) {
/* 498 */             eventListenerReference.setPropagationEnabled(propagationEnabled.booleanValue());
/*     */           }
/*     */           
/* 501 */           continuationText = XmlUtil.attribute(eventListenerElement, "continue");
/* 502 */           if (continuationText != null) {
/* 503 */             if ((observableElement instanceof ActivityImpl)) {
/* 504 */               if (observableElement.getName() == null) {
/* 505 */                 parse.addProblem("async continuation on event listener requires activity name", eventListenerElement);
/*     */               }
/*     */             }
/* 508 */             else if ((observableElement instanceof TransitionImpl)) {
/* 509 */               TransitionImpl transition = (TransitionImpl)observableElement;
/* 510 */               if (transition.getSource().getName() == null) {
/* 511 */                 parse.addProblem("async continuation on event listener requires name in the transition source activity", eventListenerElement);
/*     */               }
/*     */             }
/* 514 */             if ("async".equals(continuationText)) {
/* 515 */               eventListenerReference.setContinuation(Continuation.ASYNCHRONOUS);
/*     */             }
/* 517 */             else if ("exclusive".equals(continuationText)) {
/* 518 */               eventListenerReference.setContinuation(Continuation.EXCLUSIVE);
/*     */             }
/*     */           }
/*     */         }
/*     */         else {
/* 523 */           String tagName = eventListenerElement.getLocalName();
/* 524 */           if ((!(observableElement instanceof TransitionImpl)) || ((!"condition".equals(tagName)) && (!"timer".equals(tagName)))) {
/* 525 */             parse.addProblem("unrecognized event listener: " + tagName, null, "warning", eventListenerElement);
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */   
/*     */   public void parseTransitions(Element element, ActivityImpl activity, Parse parse) {
/* 533 */     UnresolvedTransitions unresolvedTransitions = (UnresolvedTransitions)parse.contextStackFind(UnresolvedTransitions.class);
/*     */     
/*     */ 
/* 536 */     List<Element> transitionElements = XmlUtil.elements(element, "transition");
/* 537 */     for (Element transitionElement : transitionElements) {
/* 538 */       String transitionName = XmlUtil.attribute(transitionElement, "name");
/*     */       
/* 540 */       Element timerElement = XmlUtil.element(transitionElement, "timer");
/* 541 */       if (timerElement != null) {
/* 542 */         TimerDefinitionImpl timerDefinitionImpl = parseTimerDefinition(timerElement, parse, activity);
/*     */         
/* 544 */         timerDefinitionImpl.setSignalName(transitionName);
/*     */       }
/*     */       
/* 547 */       TransitionImpl transition = activity.createOutgoingTransition();
/* 548 */       transition.setName(transitionName);
/*     */       
/* 550 */       unresolvedTransitions.add(transition, transitionElement);
/* 551 */       parseOnEvent(transitionElement, parse, transition, "take");
/*     */     }
/*     */   }
/*     */   
/*     */   public void parseAssignmentAttributes(Element element, AssignableDefinitionImpl assignableDefinition, Parse parse)
/*     */   {
/* 557 */     Element descriptionElement = XmlUtil.element(element, "description");
/* 558 */     if (descriptionElement != null) {
/* 559 */       String descriptionText = XmlUtil.getContentText(descriptionElement);
/* 560 */       Expression descriptionExpression = Expression.create(descriptionText, "uel-value");
/*     */       
/* 562 */       assignableDefinition.setDescription(descriptionExpression);
/*     */     }
/*     */     
/* 565 */     Element assignmentHandlerElement = XmlUtil.element(element, "assignment-handler");
/* 566 */     if (assignmentHandlerElement != null) {
/* 567 */       UserCodeReference assignmentHandlerReference = parseUserCodeReference(assignmentHandlerElement, parse);
/*     */       
/* 569 */       assignableDefinition.setAssignmentHandlerReference(assignmentHandlerReference);
/*     */     }
/*     */     
/* 572 */     String assigneeExpressionText = XmlUtil.attribute(element, "assignee");
/* 573 */     if (assigneeExpressionText != null) {
/* 574 */       String assigneeExpressionLanguage = XmlUtil.attribute(element, "assignee-lang");
/* 575 */       Expression assigneeExpression = Expression.create(assigneeExpressionText, assigneeExpressionLanguage);
/*     */       
/* 577 */       assignableDefinition.setAssigneeExpression(assigneeExpression);
/*     */     }
/*     */     
/* 580 */     String candidateUsersExpressionText = XmlUtil.attribute(element, "candidate-users");
/* 581 */     if (candidateUsersExpressionText != null) {
/* 582 */       String candidateUsersExpressionLanguage = XmlUtil.attribute(element, "candidate-users-lang");
/*     */       
/* 584 */       Expression candidateUsersExpression = Expression.create(candidateUsersExpressionText, candidateUsersExpressionLanguage);
/*     */       
/* 586 */       assignableDefinition.setCandidateUsersExpression(candidateUsersExpression);
/*     */     }
/*     */     
/* 589 */     String candidateGroupsExpressionText = XmlUtil.attribute(element, "candidate-groups");
/* 590 */     if (candidateGroupsExpressionText != null) {
/* 591 */       String candidateGroupsExpressionLanguage = XmlUtil.attribute(element, "candidate-groups-lang");
/*     */       
/* 593 */       Expression candidateGroupsExpression = Expression.create(candidateGroupsExpressionText, candidateGroupsExpressionLanguage);
/*     */       
/* 595 */       assignableDefinition.setCandidateGroupsExpression(candidateGroupsExpression);
/*     */     }
/*     */   }
/*     */   
/*     */   public TaskDefinitionImpl parseTaskDefinition(Element element, Parse parse, ScopeElementImpl scopeElement)
/*     */   {
/* 601 */     TaskDefinitionImpl taskDefinition = new TaskDefinitionImpl();
/*     */     
/* 603 */     String taskName = XmlUtil.attribute(element, "name");
/* 604 */     taskDefinition.setName(taskName);
/*     */     
/* 606 */     String form = XmlUtil.attribute(element, "form");
/* 607 */     taskDefinition.setFormResourceName(form);
/*     */     
/* 609 */     String duedate = XmlUtil.attribute(element, "duedate");
/* 610 */     taskDefinition.setDueDateDescription(duedate);
/*     */     
/* 612 */     Integer priority = XmlUtil.attributeInteger(element, "priority", parse);
/* 613 */     if (priority != null) {
/* 614 */       taskDefinition.setPriority(priority.intValue());
/*     */     } else {
/* 616 */       taskDefinition.setPriority(1);
/*     */     }
/*     */     
/* 619 */     ProcessDefinitionImpl processDefinition = (ProcessDefinitionImpl)parse.contextStackFind(ProcessDefinitionImpl.class);
/*     */     
/* 621 */     if (processDefinition.getTaskDefinition(taskName) != null) {
/* 622 */       parse.addProblem("duplicate task name " + taskName, element);
/*     */     }
/*     */     else {
/* 625 */       processDefinition.addTaskDefinitionImpl(taskDefinition);
/*     */     }
/*     */     
/* 628 */     String swimlaneName = XmlUtil.attribute(element, "swimlane");
/* 629 */     if (swimlaneName != null) {
/* 630 */       JpdlProcessDefinition jpdlProcessDefinition = (JpdlProcessDefinition)parse.contextStackFind(JpdlProcessDefinition.class);
/*     */       
/* 632 */       SwimlaneDefinitionImpl swimlaneDefinition = jpdlProcessDefinition.getSwimlaneDefinition(swimlaneName);
/*     */       
/* 634 */       if (swimlaneDefinition != null) {
/* 635 */         taskDefinition.setSwimlaneDefinition(swimlaneDefinition);
/*     */       }
/*     */       else {
/* 638 */         parse.addProblem("swimlane " + swimlaneName + " not declared", element);
/*     */       }
/*     */     }
/*     */     
/* 642 */     parseAssignmentAttributes(element, taskDefinition, parse);
/*     */     
/*     */ 
/* 645 */     Element notificationElement = XmlUtil.element(element, "notification");
/* 646 */     if (notificationElement != null) {
/* 647 */       parseMailEvent(notificationElement, parse, scopeElement, "assign");
/*     */     }
/*     */     
/* 650 */     Element reminderElement = XmlUtil.element(element, "reminder");
/* 651 */     if (reminderElement != null) {
/* 652 */       parseMailEvent(reminderElement, parse, scopeElement, "remind");
/*     */       
/* 654 */       TimerDefinitionImpl timerDefinition = parseTimerDefinition(reminderElement, parse, scopeElement);
/*     */       
/* 656 */       timerDefinition.setEventName("remind");
/*     */     }
/*     */     
/* 659 */     return taskDefinition;
/*     */   }
/*     */   
/*     */   public void parseVariableDefinitions(Element element, Parse parse, ScopeElementImpl scopeElement)
/*     */   {
/* 664 */     List<VariableDefinitionImpl> variableDefinitions = new ArrayList();
/*     */     
/* 666 */     for (Element variableElement : XmlUtil.elements(element, "variable")) {
/* 667 */       VariableDefinitionImpl variableDefinition = scopeElement.createVariableDefinition();
/*     */       
/* 669 */       String name = XmlUtil.attribute(variableElement, "name", parse);
/* 670 */       variableDefinition.setName(name);
/*     */       
/* 672 */       String type = XmlUtil.attribute(variableElement, "type", parse);
/* 673 */       variableDefinition.setTypeName(type);
/*     */       
/* 675 */       Boolean isHistoryEnabled = XmlUtil.attributeBoolean(variableElement, "history", parse);
/* 676 */       if (isHistoryEnabled != null) {
/* 677 */         variableDefinition.setHistoryEnabled(isHistoryEnabled.booleanValue());
/*     */       }
/*     */       
/* 680 */       int sources = 0;
/*     */       
/* 682 */       String initExpr = XmlUtil.attribute(variableElement, "init-expr");
/* 683 */       String initExprType = XmlUtil.attribute(variableElement, "init-expr-type");
/* 684 */       if (initExpr != null) {
/* 685 */         Expression initExpression = Expression.create(initExpr, initExprType);
/* 686 */         variableDefinition.setInitExpression(initExpression);
/* 687 */         sources++;
/*     */       }
/*     */       
/* 690 */       Element initDescriptorElement = XmlUtil.element(variableElement);
/* 691 */       if (initDescriptorElement != null) {
/* 692 */         Descriptor initValueDescriptor = (Descriptor)WireParser.getInstance().parseElement(initDescriptorElement, parse);
/*     */         
/* 694 */         variableDefinition.setInitDescriptor(initValueDescriptor);
/* 695 */         sources++;
/*     */       }
/*     */       
/* 698 */       if (sources > 1) {
/* 699 */         parse.addProblem("init attribute and init element are mutually exclusive on element variable", variableElement);
/*     */       }
/*     */       
/* 702 */       variableDefinitions.add(variableDefinition);
/*     */     }
/*     */   }
/*     */   
/*     */ 
/*     */   public void parseMailEvent(Element element, Parse parse, ObservableElementImpl observableElement, String eventName)
/*     */   {
/* 709 */     EventImpl event = observableElement.getEvent(eventName);
/* 710 */     if (event == null) {
/* 711 */       event = observableElement.createEvent(eventName);
/*     */     }
/*     */     
/* 714 */     MailListener eventListener = new MailListener();
/* 715 */     EventListenerReference eventListenerRef = event.createEventListenerReference(eventListener);
/*     */     
/* 717 */     String continuationText = XmlUtil.attribute(element, "continue");
/* 718 */     if ("async".equals(continuationText)) {
/* 719 */       eventListenerRef.setContinuation(Continuation.ASYNCHRONOUS);
/*     */     }
/* 721 */     else if ("exclusive".equals(continuationText)) {
/* 722 */       eventListenerRef.setContinuation(Continuation.EXCLUSIVE);
/*     */     }
/*     */     
/*     */ 
/* 726 */     String mailTemplateName = eventName;
/* 727 */     if ("assign".equals(eventName)) {
/* 728 */       mailTemplateName = "task-notification";
/*     */     }
/* 730 */     else if ("remind".equals(eventName)) {
/* 731 */       mailTemplateName = "task-reminder";
/*     */     }
/*     */     
/*     */ 
/* 735 */     UserCodeReference mailProducer = parseMailProducer(element, parse, mailTemplateName);
/* 736 */     eventListener.setMailProducerReference(mailProducer);
/*     */   }
/*     */   
/*     */ 
/*     */   public UserCodeReference parseMailProducer(Element element, Parse parse, String defaultTemplateName)
/*     */   {
/* 742 */     if (ObjectBinding.isObjectDescriptor(element)) {
/* 743 */       return parseUserCodeReference(element, parse);
/*     */     }
/*     */     
/*     */ 
/* 747 */     MailTemplate mailTemplate = parseMailTemplate(element, parse, defaultTemplateName);
/* 748 */     ObjectDescriptor descriptor = new ObjectDescriptor(MailProducerImpl.class);
/* 749 */     descriptor.addPropertyInjection("template", new ProvidedObjectDescriptor(mailTemplate));
/*     */     
/* 751 */     UserCodeReference userCodeReference = new UserCodeReference();
/* 752 */     userCodeReference.setDescriptor(descriptor);
/* 753 */     return userCodeReference;
/*     */   }
/*     */   
/*     */   private MailTemplate parseMailTemplate(Element element, Parse parse, String defaultTemplateName)
/*     */   {
/* 758 */     if (element.hasAttribute("template"))
/*     */     {
/* 760 */       return findMailTemplate(element, parse, element.getAttribute("template"));
/*     */     }
/* 762 */     if (!XmlUtil.isTextOnly(element))
/*     */     {
/* 764 */       return MailTemplateBinding.parseMailTemplate(element, parse);
/*     */     }
/* 766 */     if (defaultTemplateName != null)
/*     */     {
/* 768 */       return findMailTemplate(element, parse, defaultTemplateName);
/*     */     }
/* 770 */     parse.addProblem("mail template must be referenced in the 'template' attribute or specified inline", element);
/*     */     
/* 772 */     return null;
/*     */   }
/*     */   
/*     */   private MailTemplate findMailTemplate(Element element, Parse parse, String templateName) {
/* 776 */     MailTemplateRegistry templateRegistry = (MailTemplateRegistry)EnvironmentImpl.getFromCurrent(MailTemplateRegistry.class);
/*     */     
/* 778 */     if (templateRegistry != null) {
/* 779 */       MailTemplate template = templateRegistry.getTemplate(templateName);
/* 780 */       if (template != null)
/* 781 */         return template;
/*     */     }
/* 783 */     parse.addProblem("mail template not found: " + templateName, element);
/* 784 */     return null;
/*     */   }
/*     */   
/*     */   public UserCodeReference parseUserCodeReference(Element element, Parse parse) {
/* 788 */     UserCodeReference userCodeReference = new UserCodeReference();
/*     */     
/* 790 */     ObjectDescriptor objectDescriptor = parseObjectDescriptor(element, parse);
/* 791 */     userCodeReference.setDescriptor(objectDescriptor);
/*     */     
/* 793 */     if (objectDescriptor.getExpression() != null)
/*     */     {
/* 795 */       userCodeReference.setCached(false);
/*     */     }
/*     */     
/* 798 */     Boolean isCached = XmlUtil.attributeBoolean(element, "cache", parse);
/* 799 */     if (isCached != null) {
/* 800 */       userCodeReference.setCached(isCached.booleanValue());
/*     */     }
/*     */     
/* 803 */     return userCodeReference;
/*     */   }
/*     */   
/*     */   public ObjectDescriptor parseObjectDescriptor(Element element, Parse parse) {
/* 807 */     return ObjectBinding.parseObjectDescriptor(element, parse, WireParser.getInstance());
/*     */   }
/*     */   
/*     */   public Descriptor parseDescriptor(Element element, Parse parse) {
/* 811 */     return (Descriptor)WireParser.getInstance().parseElement(element, parse);
/*     */   }
/*     */   
/*     */   public Set<String> getActivityTagNames() {
/* 815 */     return getBindings().getTagNames("activity");
/*     */   }
/*     */ }


/* Location:              /Users/Jason/Desktop/ifinflow-core.jar!/org/jbpm/jpdl/internal/xml/JpdlParser.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       0.7.1
 */