package fi.thl.termed.controller;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Map;

import fi.thl.termed.model.Concept;
import fi.thl.termed.service.ConceptJsonService;
import fi.thl.termed.util.SKOS;

import static org.apache.commons.codec.digest.DigestUtils.sha1Hex;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(value = "/")
public class RdfImporter {

  @SuppressWarnings("all")
  private Logger log = LoggerFactory.getLogger(getClass());
  private Gson gson = new GsonBuilder().setPrettyPrinting().create();

  private final ConceptJsonService service;

  @Autowired
  public RdfImporter(ConceptJsonService service) {
    this.service = service;
  }

  @RequestMapping(method = POST, value = "import", consumes = "text/turtle;charset=UTF-8")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void importTurtle(@RequestBody String input) {
    Model model = ModelFactory.createDefaultModel();
    model.read(new ByteArrayInputStream(input.getBytes()), null, "TTL");

    log.info("read {} statements", model.size());

    Map<String, Concept> concepts = Maps.newHashMap();

    for (Resource r : model.listResourcesWithProperty(RDF.type, SKOS.Concept).toList()) {
      Concept c = new Concept(sha1Hex(r.getURI()));
      c.addProperty("label", "fi", getLiteralValues(model, r, SKOS.prefLabel, "fi"));
      c.addProperty("label", "sv", getLiteralValues(model, r, SKOS.prefLabel, "sv"));
      c.addProperty("label", "en", getLiteralValues(model, r, SKOS.prefLabel, "en"));
      c.addProperty("altLabel", "fi", getLiteralValues(model, r, SKOS.altLabel, "fi"));
      c.addProperty("altLabel", "sv", getLiteralValues(model, r, SKOS.altLabel, "sv"));
      c.addProperty("altLabel", "en", getLiteralValues(model, r, SKOS.altLabel, "en"));
      c.addProperty("hiddenLabel", "fi", getLiteralValues(model, r, SKOS.hiddenLabel, "fi"));
      c.addProperty("hiddenLabel", "sv", getLiteralValues(model, r, SKOS.hiddenLabel, "sv"));
      c.addProperty("hiddenLabel", "en", getLiteralValues(model, r, SKOS.hiddenLabel, "en"));
      concepts.put(c.getId(), c);
    }

    service.saveAll(gson.toJsonTree(concepts.values()).getAsJsonArray());

    for (Resource r : model.listResourcesWithProperty(RDF.type, SKOS.Concept).toList()) {
      Concept c = concepts.get(sha1Hex(r.getURI()));
      for (String parentUri : getObjectValues(model, r, SKOS.broader)) {
        String parentId = sha1Hex(parentUri);
        if (concepts.containsKey(parentId)) {
          c.setParent(new Concept(sha1Hex(parentUri)));
        }
      }
      for (String relatedUri : getObjectValues(model, r, SKOS.related)) {
        String relatedId = sha1Hex(relatedUri);
        if (concepts.containsKey(relatedId)) {
          c.addRelated(new Concept(relatedId));
        }
      }
    }

    service.saveAll(gson.toJsonTree(concepts.values()).getAsJsonArray());

    log.info("imported {} concepts", concepts.size());
  }

  private List<String> getObjectValues(Model m, Resource r, Property p) {
    List<String> values = Lists.newArrayList();

    for (RDFNode n : m.listObjectsOfProperty(r, p).toList()) {
      if (n.isURIResource()) {
        values.add(n.asResource().getURI());
      }
    }

    return values;
  }

  private String getLiteralValues(Model m, Resource r, Property p, String lang) {
    List<String> values = Lists.newArrayList();

    for (RDFNode n : m.listObjectsOfProperty(r, p).toList()) {
      if (n.isLiteral()) {
        Literal literal = n.asLiteral();
        if (lang.equals(literal.getLanguage())) {
          values.add(literal.getString());
        }
      }
    }

    return Joiner.on(", ").join(values);
  }

}