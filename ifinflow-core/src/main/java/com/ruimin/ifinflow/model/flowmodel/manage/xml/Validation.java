package com.ruimin.ifinflow.model.flowmodel.manage.xml;

import com.ruimin.ifinflow.model.flowmodel.manage.xml.check.vo.GraphVo;
import com.ruimin.ifinflow.model.flowmodel.manage.xml.check.vo.TransitionVo;
import com.ruimin.ifinflow.util.exception.IFinFlowException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jbpm.pvm.internal.util.XmlUtil;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

public class Validation {
	private static Log log = LogFactory.getLog(Validation.class);
	private Map<String, GraphVo> graphMap = new HashMap();

	private final String CYCLE_TYPE_COUNT = "2";

	private String endId = null;

	private final String CYCLE_TYPE_TIME = "3";

	private final String FORK_NODE = "FORK";
	private final String DECISION_NODE = "DECISION";

	private void init(String jpdlStr) {
		Element root = XmlHandle.getRootElement(jpdlStr);
		if (null == root) {
			throw new IFinFlowException(110008, new Object[0]);
		}

		List<Element> eleList = XmlUtil.elements(root);

		for (Element childElement : eleList) {
			String nodeName = childElement.getNodeName();
			if (!"properties".equals(nodeName)) {
				parseGraph(childElement);
			}
		}
		setPriorAfterName();
	}

	public boolean checkXml(String jpdlStr) throws IFinFlowException {
		log.trace("校验xml开始。。。 ");
		init(jpdlStr);
		Set<String> forkSet = new HashSet();
		Set<String> joinSet = new HashSet();
		Set<String> decisionSet = new HashSet();
		Set<String> sjoinSet = new HashSet();
		Set<String> endSet = new HashSet();

		for (Map.Entry<String, GraphVo> entry : this.graphMap.entrySet()) {
			GraphVo graph = (GraphVo) entry.getValue();
			String key = (String) entry.getKey();

			if (null != graph) {
				List<TransitionVo> transitions = graph.getTransitions();
				String to;
				if ((null != transitions) && (key.startsWith("ForkNode_"))) {
					if (transitions.size() < 2) {
						throw new IFinFlowException(110009,
								new Object[] { graph.getId() });
					}
					to = null;
					for (TransitionVo tvo : transitions) {
						if (null != tvo) {
							if (null == to) {
								to = tvo.getTo();

							} else if ((to.startsWith("ForkNode_"))
									&& (to.equals(tvo.getTo()))) {
								throw new IFinFlowException(110010,
										new Object[] { graph.getName(),
												graph.getId() });
							}
						}
					}
				}

				if (key.startsWith("ForkNode_")) {
					forkSet.add(key);
					if (!hasJoinNode()) {
						throw new IFinFlowException(110012, new Object[0]);
					}
				}
				if (key.startsWith("JoinNode_")) {
					joinSet.add(key);
				}

				if (key.startsWith("SJoinNode_")) {
					sjoinSet.add(key);
				}
				if (key.startsWith("EndNode_")) {
					endSet.add(key);
				}
			}
		}
		int joinSize = joinSet.size();
		int forkSize = forkSet.size();

		if (joinSize > 0) {
			if (joinSize != forkSize) {
				throw new IFinFlowException(110014, new Object[0]);
			}
			while ((forkSet.size() != 0) && (joinSet.size() != 0)) {
				validSpecialNode(forkSet, joinSet, endSet, "FORK");
			}
		}

		if (sjoinSet.size() != 0) {
			while ((decisionSet.size() != 0) && (sjoinSet.size() != 0)) {
				validSpecialNode(decisionSet, sjoinSet, endSet, "DECISION");
			}
		}
		this.graphMap = null;
		log.trace("校验xml结束。。。 ");
		return true;
	}

	private void validSpecialNode(Set<String> forkOrDecisionSet,
			Set<String> joinOrSJoinSet, Set<String> endSet, String nodeType) {
		for (String nodeId : endSet) {
			this.endId = nodeId;
			if ("FORK".equals(nodeType)) {
				validForkJoinMatchs((GraphVo) this.graphMap.get(nodeId),
						forkOrDecisionSet, joinOrSJoinSet);
			} else {
				validDecisionSjoinMatchs((GraphVo) this.graphMap.get(nodeId),
						forkOrDecisionSet, joinOrSJoinSet);
			}
		}
	}

	private boolean hasJoinNode() {
		for (Map.Entry<String, GraphVo> entry : this.graphMap.entrySet()) {
			if (((String) entry.getKey()).startsWith("JoinNode_")) {
				return true;
			}
		}

		return false;
	}

	private void validDecisionSjoinMatchs(GraphVo graph,
			Set<String> decisionSet, Set<String> sjoinSet)
			throws IFinFlowException {
		if (decisionSet.size() >= sjoinSet.size()) {
			if (sjoinSet.size() == 0) {
				return;
			}
			String graphId = graph.getId();
			validSimpleJoin(graphId, decisionSet, sjoinSet);

		} else {

			throw new IFinFlowException(110013, new Object[0]);
		}
	}

	private void validDecisionSimpleJoinMatch(GraphVo graph,
			Set<String> decisionSet, Set<String> sjoinSet)
			throws IFinFlowException {
		List<String> priorNames = graph.getPriorNameList();
		int priorNamesSize = priorNames.size();
		if (priorNamesSize < 2) {
			throw new IFinFlowException(110024, new Object[0]);
		}
		String tempDecisionName = null;
		String currentJoinName = graph.getId();
		for (int i = 0; i < priorNamesSize; i++) {

			String priorName = (String) priorNames.get(i);
			if (priorName.startsWith("SJoinNode_")) {
				validDecisionSimpleJoinMatch(
						(GraphVo) this.graphMap.get(priorName), decisionSet,
						sjoinSet);
			} else {
				List<String> result = new ArrayList();
				getForkId(priorName, result, decisionSet, sjoinSet, 1);

				for (String tempForkId : result) {
					if (tempForkId.startsWith("DecisionNode_")) {
						priorName = tempForkId;
						break;
					}
				}
				result = null;

				if (i == 0) {
					tempDecisionName = priorName;
				}

				if (sjoinSet.size() == 0) {
					return;
				}
				if ((tempDecisionName != priorName)
						&& (StringUtils.isNotBlank(tempDecisionName))) {
					throw new IFinFlowException(110013, new Object[0]);
				}
				if ((i == priorNamesSize - 1)
						&& (tempDecisionName == priorName)) {
					if (((GraphVo) this.graphMap.get(tempDecisionName))
							.getAfterNameList().size() != ((GraphVo) this.graphMap
							.get(currentJoinName)).getPriorNameList().size()) {
						throw new IFinFlowException(110013, new Object[0]);
					}
					deleteForkJoin(tempDecisionName, currentJoinName);
					decisionSet.remove(tempDecisionName);
					sjoinSet.remove(currentJoinName);

					validDecisionSjoinMatchs(
							(GraphVo) this.graphMap.get(this.endId),
							decisionSet, sjoinSet);
				}
			}
		}
	}

	private void validForkJoinMatchs(GraphVo graph, Set<String> forkSet,
			Set<String> joinSet) throws IFinFlowException {
		if (forkSet.size() == joinSet.size()) {
			if (joinSet.size() == 0) {
				return;
			}
			validJoin(graph.getId(), forkSet, joinSet);
		} else {
			throw new IFinFlowException(110014, new Object[0]);
		}
	}

	private void validJoin(String graphId, Set<String> forkSet,
			Set<String> joinSet) {
		List<String> priorIdList = findPriorGraph(graphId);

		List<String> tempPriorIdList = new ArrayList();
		tempPriorIdList.addAll(priorIdList);
		for (String nodeId : tempPriorIdList) {
			if (nodeId.startsWith("StartNode_")) {
				break;
			}
			if (!nodeId.startsWith("JoinNode_")) {
				validJoin(nodeId, forkSet, joinSet);
			} else {
				validForkJoinMatch((GraphVo) this.graphMap.get(nodeId),
						forkSet, joinSet);
			}
		}
		tempPriorIdList = null;
	}

	private void validSimpleJoin(String graphId, Set<String> forkSet,
			Set<String> joinSet) {
		List<String> priorIdList = findPriorGraph(graphId);
		List<String> tempPriorIdList = new ArrayList();
		tempPriorIdList.addAll(priorIdList);
		for (String nodeId : tempPriorIdList) {
			if (nodeId.startsWith("StartNode_")) {
				break;
			}
			if (!nodeId.startsWith("SJoinNode_")) {
				graphId = nodeId;
				validSimpleJoin(graphId, forkSet, joinSet);
			} else {
				validDecisionSimpleJoinMatch(
						(GraphVo) this.graphMap.get(nodeId), forkSet, joinSet);
			}
		}
	}

	private void validForkJoinMatch(GraphVo graph, Set<String> forkSet,
			Set<String> joinSet) throws IFinFlowException {
		List<String> priorNames = graph.getPriorNameList();
		int priorNamesSize = priorNames.size();
		if (priorNamesSize < 2) {
			throw new IFinFlowException(110015, new Object[] { graph.getId() });
		}

		String tempForkName = null;
		String currentJoinName = graph.getId();
		for (int i = 0; i < priorNamesSize; i++) {
			String priorName = (String) priorNames.get(i);

			if (priorName.startsWith("JoinNode_")) {
				validForkJoinMatch((GraphVo) this.graphMap.get(priorName),
						forkSet, joinSet);
			} else {
				if (priorName.startsWith("ForkNode_")) {
					throw new IFinFlowException(110016, new Object[0]);
				}
				List<String> result = new ArrayList();
				getForkId(priorName, result, forkSet, joinSet, 0);

				for (String tempForkId : result) {
					if (tempForkId.startsWith("ForkNode_")) {
						priorName = tempForkId;
						break;
					}
				}
				result = null;

				if (i == 0) {
					tempForkName = priorName;
				}

				if (joinSet.size() == 0) {
					return;
				}
				if ((tempForkName != priorName)
						&& (StringUtils.isNotBlank(tempForkName))) {
					throw new IFinFlowException(110014, new Object[0]);
				}
				if ((i == priorNamesSize - 1) && (tempForkName == priorName)) {
					if (((GraphVo) this.graphMap.get(tempForkName))
							.getAfterNameList().size() != ((GraphVo) this.graphMap
							.get(currentJoinName)).getPriorNameList().size()) {
						throw new IFinFlowException(110014, new Object[0]);
					}
					if (!tempForkName.equals(((GraphVo) this.graphMap
							.get(currentJoinName)).getMatchId())) {
						throw new IFinFlowException(110017,
								new Object[] { currentJoinName });
					}
					deleteForkJoin(tempForkName, currentJoinName);
					forkSet.remove(tempForkName);
					joinSet.remove(currentJoinName);

					validForkJoinMatchs(
							(GraphVo) this.graphMap.get(this.endId), forkSet,
							joinSet);
				}
			}
		}
	}

	public void getForkId(String nodeId, List<String> result,
			Set<String> forkOrDeciSet, Set<String> joinOrSimpSet, int flag) {
		String joinType = null;
		String forkType = null;
		int exceptionCode = 0;
		if (flag == 0) {
			joinType = "JoinNode_";
			forkType = "ForkNode_";
			exceptionCode = 110014;
		} else {
			joinType = "SJoinNode_";
			forkType = "DecisionNode_";
			exceptionCode = 110013;
		}
		if (!result.contains(nodeId)) {
			if (nodeId.startsWith(joinType)) {
				validForkJoinMatch((GraphVo) this.graphMap.get(nodeId),
						forkOrDeciSet, joinOrSimpSet);
			}

			if (nodeId.startsWith("StartNode_")) {
				throw new IFinFlowException(exceptionCode, new Object[0]);
			}

			result.add(nodeId);
			if (!nodeId.startsWith(forkType)) {
			}

		} else {
			return;
		}

		for (String pn : ((GraphVo) this.graphMap.get(nodeId))
				.getPriorNameList()) {
			if (StringUtils.isNotBlank(pn)) {
				getForkId(pn, result, forkOrDeciSet, joinOrSimpSet, flag);
			}
		}
	}

	private void deleteForkJoin(String tempForkName, String currentJoinName) {
		String afterName = (String) ((GraphVo) this.graphMap
				.get(currentJoinName)).getAfterNameList().get(0);

		if (StringUtils.isNotBlank(afterName)) {
			((GraphVo) this.graphMap.get(currentJoinName))
					.setAfterNameList(null);

			((GraphVo) this.graphMap.get(afterName)).getPriorNameList().remove(
					0);

			String priorNameBeforFork = (String) ((GraphVo) this.graphMap
					.get(tempForkName)).getPriorNameList().get(0);

			((GraphVo) this.graphMap.get(afterName)).getPriorNameList().add(
					priorNameBeforFork);

			((GraphVo) this.graphMap.get(priorNameBeforFork))
					.getAfterNameList().remove(0);

			((GraphVo) this.graphMap.get(priorNameBeforFork))
					.getAfterNameList().add(afterName);

			((GraphVo) this.graphMap.get(tempForkName)).setPriorNameList(null);
		}
	}

	private List<String> findPriorGraph(String priorName) {
		return ((GraphVo) this.graphMap.get(priorName)).getPriorNameList();
	}

	private void parseGraph(Element childElement) throws IFinFlowException {
		GraphVo graph = new GraphVo();
		List<TransitionVo> transitions = null;
		for (Element taskSubElement : XmlUtil.elements(childElement)) {
			String pthistory;
			if ("properties".equals(taskSubElement.getNodeName())) {
				for (Element taskSubEle : XmlUtil.elements(taskSubElement)) {
					if ("base".equals(taskSubEle.getNodeName())) {
						String cycleType = null;
						String cycleCount = null;

						int delayTime = 0;

						int intervalTime = 0;

						int continueTime = 0;
						for (Element baseEle : XmlUtil.elements(taskSubEle)) {
							String subNodeName = baseEle.getNodeName();
							String subNodeValue = XmlHandle.dealString(baseEle
									.getTextContent());
							if (XmlHandle.isNotNull(subNodeValue)) {
								if ("id".equals(subNodeName)) {
									graph.setId(subNodeValue);
								} else if ("name".equals(subNodeName)) {
									graph.setName(subNodeValue);
								} else if ("matchid".equals(subNodeName)) {
									graph.setMatchId(subNodeValue);
								} else if ("cycletype".equals(subNodeName)) {
									cycleType = subNodeValue;
								} else if ("cyclecount".equals(subNodeName)) {
									cycleCount = subNodeValue;
								} else if ("delayday".equals(subNodeName)) {
									delayTime += Integer.parseInt(subNodeValue) * 24 * 60 * 60;
								} else if ("delayhour".equals(subNodeName)) {
									delayTime += Integer.parseInt(subNodeValue) * 60 * 60;
								} else if ("delayminute".equals(subNodeName)) {
									delayTime += Integer.parseInt(subNodeValue) * 60;
								} else if ("delaysecond".equals(subNodeName)) {
									delayTime += Integer.parseInt(subNodeValue);
								} else if ("intervalday".equals(subNodeName)) {
									intervalTime += Integer
											.parseInt(subNodeValue) * 24 * 60 * 60;
								} else if ("intervalhour".equals(subNodeName)) {
									intervalTime += Integer
											.parseInt(subNodeValue) * 60 * 60;
								} else if ("intervalminute".equals(subNodeName)) {
									intervalTime += Integer
											.parseInt(subNodeValue) * 60;
								} else if ("intervalsecond".equals(subNodeName)) {
									intervalTime += Integer
											.parseInt(subNodeValue);
								} else if ("continueday".equals(subNodeName)) {
									continueTime += Integer
											.parseInt(subNodeValue) * 24 * 60 * 60;
								} else if ("continuehour".equals(subNodeName)) {
									continueTime += Integer
											.parseInt(subNodeValue) * 60 * 60;
								} else if ("continueminute".equals(subNodeName)) {
									continueTime += Integer
											.parseInt(subNodeValue) * 60;
								} else if ("continuesecond".equals(subNodeName)) {
									continueTime += Integer
											.parseInt(subNodeValue);
								}
							} else if ("adaptername".equals(subNodeName)) {
								throw new IFinFlowException(110018,
										new Object[] { graph.getName(),
												graph.getId() });
							}
						}

						if (("timer".equals(childElement.getNodeName()))
								&& (delayTime <= 0)) {
							throw new IFinFlowException(110019, new Object[] {
									graph.getName(), graph.getId() });
						}
						if ("2".equals(cycleType)) {
							if ((Integer.parseInt(cycleCount) > 0)
									&& (intervalTime <= 0)) {
								throw new IFinFlowException(110020,
										new Object[] { graph.getName(),
												graph.getId() });
							}
						} else if (("3".equals(cycleType))
								&& (intervalTime != 0) && (continueTime != 0)) {
							if (intervalTime > continueTime) {
								throw new IFinFlowException(110021,
										new Object[] { graph.getName(),
												graph.getId() });
							}
						}
					} else if ("assigners".equals(taskSubEle.getNodeName())) {
						pthistory = null;
						for (Element baseEle : XmlUtil.elements(taskSubEle)) {
							String nodeValue = XmlHandle.dealString(baseEle
									.getTextContent());
							if ("pthistory".equals(baseEle.getNodeName())) {
								pthistory = nodeValue;
							} else if ("result".equals(baseEle.getNodeName())) {
								int history = Integer.parseInt(pthistory);
								if ((1 != history) && (3 != history)
										&& (4 != history)
										&& (!XmlHandle.isNotNull(nodeValue))) {

									throw new IFinFlowException(110022,
											new Object[] { graph.getName(),
													graph.getId() });
								}

							}
						}
					}
				}
			} else if ("transition".equals(taskSubElement.getNodeName())) {
				TransitionVo tvo = new TransitionVo();
				for (Attr transSubEleAttr : XmlUtil.attributes(taskSubElement)) {
					String subNodeName = transSubEleAttr.getName();
					String subNodeValue = XmlHandle.dealString(transSubEleAttr
							.getValue());
					if ("from".equals(subNodeName)) {
						tvo.setFrom(subNodeValue);
					} else if ("to".equals(subNodeName)) {
						tvo.setTo(subNodeValue);
					}
				}
				if (null == transitions) {
					transitions = new ArrayList();
				}
				transitions.add(tvo);
			}
		}
		graph.setTransitions(transitions);
		this.graphMap.put(graph.getId(), graph);
	}

	private void setPriorAfterName() {
		for (Map.Entry<String, GraphVo> entry : this.graphMap.entrySet()) {
			String key = (String) entry.getKey();
			GraphVo graph = (GraphVo) entry.getValue();

			List<TransitionVo> transitions = graph.getTransitions();
			if (null != transitions) {
				int size = transitions.size();
				for (int i = 0; i < size; i++) {
					String from = ((TransitionVo) transitions.get(i)).getFrom();

					String to = ((TransitionVo) transitions.get(i)).getTo();
					((GraphVo) this.graphMap.get(to)).getPriorNameList().add(
							key);
					((GraphVo) this.graphMap.get(from)).getAfterNameList().add(
							to);
				}
			}
		}
	}
}
