package com.maqs.moneytracker.common.transferobjects;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.maqs.moneytracker.common.util.CollectionsUtil;

@XmlAccessorType(XmlAccessType.PROPERTY)
@XmlType(name = "action")
public class Action {

	private int actionIndex;
	
	public static final int DO_NOTHING = 0;
	
	public static final int CREATE_NEW = 1;
	
	public static final int UPDATE = 2;
	
	public static final int DELETE = 3;
	
	public static final int CHILDLIST_CHANGED = 4;
	
	private static final Set<Integer> cacheSet = new HashSet<Integer>();
	
	static {
		cacheSet.add(DO_NOTHING);
		cacheSet.add(CREATE_NEW);
		cacheSet.add(UPDATE);
		cacheSet.add(DELETE);
		cacheSet.add(CHILDLIST_CHANGED);
	}
	
	public Action() {
		this(CREATE_NEW);
	}
	
	public Action(int actionIndex) {
		setActionIndex(actionIndex);
	}
	
	public void setActionIndex(int actionIndex) {
		if (!isValid(actionIndex)) {
			throw new IllegalArgumentException("action with index [" + actionIndex + "] is not a valid action");
		}
		this.actionIndex = actionIndex;
	}
	
	public int getActionIndex() {
		return actionIndex;
	}
	
	private boolean isValid(int index) {
		return cacheSet.contains(index);
	}
	
	@Override
	public String toString() {
		return actionIndex + "";
	}

	/**
	 * Sets doNothing action to the entity.
	 * @param e
	 */
	public static void setDoNothingAction(Entity e) {
		if (e == null)
			return;
		Action action = e.getAction();
		action.setActionIndex(Action.DO_NOTHING);
	}
	
	/**
	 * Sets doNothing action to the entity.
	 * @param e
	 */
	public static void setDoNothingAction(List<? extends Entity> list) {
		if (CollectionsUtil.isNonEmpty(list)) {
			for (Entity entity : list) {
				setDoNothingAction(entity);
			}
		}
	}

	public static void setUpdateAction(Entity e) {
		if (e != null) {
			e.getAction().setActionIndex(UPDATE);
		}
	}
	
	public static void setDeleteAction(Entity e) {
		if (e != null) {
			e.getAction().setActionIndex(DELETE);
		}
	}

	public static void setNewAction(Entity e) {
		if (e != null) {
			e.getAction().setActionIndex(CREATE_NEW);
		}
	}
}
