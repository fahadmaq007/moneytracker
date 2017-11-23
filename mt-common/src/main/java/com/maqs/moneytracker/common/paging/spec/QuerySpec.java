package com.maqs.moneytracker.common.paging.spec;

import static com.maqs.moneytracker.common.Constansts.CLOSE_BRACKET;
import static com.maqs.moneytracker.common.Constansts.OPEN_BRACKET;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.log4j.Logger;

import com.maqs.moneytracker.common.transferobjects.Entity;
import com.maqs.moneytracker.common.util.CollectionsUtil;

/**
 * QuerySpec defines the whole query clause.
 * It defines the className to be operated on & 
 * the list of propertySpecs, orderBySpecs to be applied.
 * 
 * @author maqbool.ahmed
 *
 */
@XmlRootElement(name = "querySpec")
@XmlAccessorType(XmlAccessType.FIELD)
public class QuerySpec implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5631109974811514424L;

	private String className;
	
	@XmlElementWrapper(name = "propertySpecs")
	@XmlElement(name = "propertySpec")
	private List<PropertySpec> propertySpecs = null;
	
	@XmlElementWrapper(name = "orderBySpecs")
	@XmlElement(name = "orderBySpec")
	private List<OrderBySpec> orderBySpecs = null;
	
	@XmlElementWrapper(name = "selectSpecs")
	@XmlElement(name = "selectSpec")
	private List<SelectSpec> selectSpecs = null;
	
	private Logger logger = Logger.getLogger(getClass());
	
	public QuerySpec() {
		this(Entity.class.getName());
	}
	
	public QuerySpec(String className) {
		this.className = className;
		this.propertySpecs = new ArrayList<PropertySpec>();
		this.orderBySpecs = new ArrayList<OrderBySpec>();
		this.selectSpecs = new ArrayList<SelectSpec>();	
		logger.debug("querySpec is created on " + className);
	}
	
	public void addSelectSpec(SelectSpec s) {
		getSelectSpecs().add(s);
		logger.debug("selectSpec " + s + " is addedd to the querySpec.");
	}
	
	public void addPropertySpec(PropertySpec p) {
		getPropertySpecs().add(p);
		logger.debug("propertySpec " + p + " is addedd to the querySpec.");
	}

	public void addOrderBySpec(OrderBySpec o) {
		getOrderBySpecs().add(o);
		logger.debug("orderBySpec " + o + " is addedd to the querySpec.");
	}
	
	public String getClassName() {
		return className;
	}
	
	public void setClassName(String className) {
		this.className = className;
	}
	
	public List<PropertySpec> getPropertySpecs() {
		return propertySpecs;
	}
	
	public void setPropertySpecs(List<PropertySpec> propertySpecs) {
		this.propertySpecs = propertySpecs;
	}
	
	public List<OrderBySpec> getOrderBySpecs() {
		return orderBySpecs;
	}
	
	public void setOrderBySpecs(List<OrderBySpec> orderBySpecs) {
		this.orderBySpecs = orderBySpecs;
	}
	
	public List<SelectSpec> getSelectSpecs() {
		return selectSpecs;
	}
	
	public void setSelectSpecs(List<SelectSpec> selectSpecs) {
		this.selectSpecs = selectSpecs;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(OPEN_BRACKET).append("classname=").append(className)
		.append(",\n");
		if (CollectionsUtil.isNonEmpty(propertySpecs)) {
			builder.append("propertySpecs=").append(propertySpecs).append(",\n");
		}
		if (CollectionsUtil.isNonEmpty(orderBySpecs)) {
			builder.append("orderBySpecs=").append(orderBySpecs).append(",\n");
		}
		return builder.append(CLOSE_BRACKET).toString();
	}
}
