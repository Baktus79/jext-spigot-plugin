package me.tajam.jext.configuration;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Configuration implements Configurable {

	private Configuration parent;
	private final List<Configurable> childs;
	@Getter
	private int level;

	public Configuration() {
		this.childs = new ArrayList<>();
		this.level = 0;
	}

	public Configurable getParent() {
		return this.parent;
	}

	private void setParent(Configuration parent) {
		this.parent = parent;
		this.level = this.parent.level + 1;
	}

	public void addChild(Configuration child) {
		child.setParent(this);
		childs.add(child);
	}

	public Iterator<Configurable> getChildsIterable() {
		return this.childs.iterator();
	}

}
