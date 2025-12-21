package com.orderservice.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// add table-level unique constraint and keep column-level unique for clarity
@Table(name="Order_Table",uniqueConstraints = @UniqueConstraint(columnNames = "orderNumber"))
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
	@Id
	private String id;
	
	@Column(unique = true)
	private Integer orderNumber;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "order")
	private List<OrderLineItem> orderLineItems;
}