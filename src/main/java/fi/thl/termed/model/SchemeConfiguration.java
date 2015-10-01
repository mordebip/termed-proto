package fi.thl.termed.model;

import org.hibernate.search.annotations.Indexed;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Indexed
@Entity
@Table(name = "scheme_configuration")
public class SchemeConfiguration extends Resource {

  @OneToMany(mappedBy = "configuration", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<PropertyConfiguration> propertyConfigurations;

  @OneToMany(mappedBy = "configuration", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ConceptReferenceTypeConfiguration> conceptReferenceTypeConfigurations;

  public List<PropertyConfiguration> getPropertyConfigurations() {
    return propertyConfigurations;
  }

  public void setPropertyConfigurations(
      List<PropertyConfiguration> propertyConfigurations) {
    this.propertyConfigurations = propertyConfigurations;
  }

  public List<ConceptReferenceTypeConfiguration> getConceptReferenceTypeConfigurations() {
    return conceptReferenceTypeConfigurations;
  }

  public void setConceptReferenceTypeConfigurations(
      List<ConceptReferenceTypeConfiguration> conceptReferenceTypeConfigurations) {
    this.conceptReferenceTypeConfigurations = conceptReferenceTypeConfigurations;
  }

}
