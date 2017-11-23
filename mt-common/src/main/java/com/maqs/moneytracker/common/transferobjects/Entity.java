package com.maqs.moneytracker.common.transferobjects;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * EntityDTO is the base class for all the DTOs & it is {@link Serializable}.
 * 
 * @author maqbool.ahmed
 *
 */
public class Entity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2071376340849451365L;
	
	/**
	 * Action to be performed on this entity.
	 */
	private Action action = new Action();
	
	/**
	 * Identifier of the entity.
	 */
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * hashCode() is implemented to produce the hashCode for the id.
	 * If it requires to change the implementation,
	 * the subclasses can do so.
	 * @return hashCode
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	/**
	 * equals() is implemented to check the equality of the id.
	 * If it requires to change the implementation,
	 * the subclasses can do so.
	 * @return true if it's equal otherwise false;
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Entity other = (Entity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Action getAction() {
		return action;
	}
	
	/**
	 * Sets the action on the entity
	 * @param action
	 * @see Action
	 */
	public void setAction(Action action) {
		this.action = action;
	}
	
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE).toString();
	}
	
	public String printDetails() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE).toString();
	}
}
