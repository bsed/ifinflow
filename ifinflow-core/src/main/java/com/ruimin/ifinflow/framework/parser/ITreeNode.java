package com.ruimin.ifinflow.framework.parser;

import java.util.Collection;
import org.dom4j.Element;

public abstract interface ITreeNode
{
  public static final String ID = "ID";
  public static final String ROOT = "root";
  
  public abstract Collection<ITreeNode> children();
  
  public abstract Element element();
}
