package fi.thl.termed.model;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Concept extends Resource {

  private Map<String, Set<PropertyValue>> properties;

  private Concept type;
  private Concept parent;

  private List<Concept> children;
  private List<Concept> related;

  public Concept() {
    this(null);
  }

  public Concept(String id) {
    super(id);
    this.properties = Maps.newHashMap();
    this.type = null;
    this.parent = null;
    this.children = Lists.newArrayList();
    this.related = Lists.newArrayList();
  }

  public Map<String, Set<PropertyValue>> getProperties() {
    return properties;
  }

  public void setProperties(Map<String, Set<PropertyValue>> properties) {
    this.properties = properties;
  }

  public void addProperty(String propertyId, String lang, String value) {
    if (!properties.containsKey(propertyId)) {
      properties.put(propertyId, Sets.<PropertyValue>newHashSet());
    }
    properties.get(propertyId).add(new PropertyValue(lang, value));
  }

  public Concept getType() {
    return type;
  }

  public String getTypeId() {
    return type != null ? type.getId() : null;
  }

  public void setType(Concept type) {
    this.type = type;
  }

  public Concept getParent() {
    return parent;
  }

  public String getParentId() {
    return parent != null ? parent.getId() : null;
  }

  public void setParent(Concept parent) {
    this.parent = parent;
  }

  public List<Concept> getChildren() {
    return children;
  }

  public void setChildren(List<Concept> children) {
    this.children = children;
  }

  public List<Concept> getRelated() {
    return related;
  }

  public void setRelated(List<Concept> related) {
    this.related = related;
  }

  public Objects.ToStringHelper toStringHelper() {
    return super.toStringHelper()
        .add("properties", properties)
        .add("parent", parent)
        .add("type", type)
        .add("related", related);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Concept that = (Concept) o;

    return Objects.equal(getId(), that.getId()) &&
           Objects.equal(properties, that.properties) &&
           Objects.equal(type, that.type) &&
           Objects.equal(parent, that.parent) &&
           Objects.equal(related, that.related);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getId(), properties, type, parent, related);
  }

}
