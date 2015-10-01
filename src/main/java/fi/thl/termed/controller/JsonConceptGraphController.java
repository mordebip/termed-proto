package fi.thl.termed.controller;

import com.google.gson.JsonArray;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import fi.thl.termed.service.JsonConceptGraphService;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "/")
public class JsonConceptGraphController {

  @SuppressWarnings("all")
  private Logger log = LoggerFactory.getLogger(getClass());

  @Autowired
  private JsonConceptGraphService jsonConceptGraphService;

  @RequestMapping(method = GET, value = "concepts/{conceptId}/trees")
  @ResponseBody
  public JsonArray getConceptJsTrees(@PathVariable("conceptId") String conceptId) {
    return jsonConceptGraphService.getConceptJsTrees(conceptId);
  }

  @RequestMapping(method = GET, value = "concepts/{conceptId}/broader")
  @ResponseBody
  public JsonArray getConceptBroaderPaths(@PathVariable("conceptId") String conceptId) {
    return jsonConceptGraphService.getConceptBroaderPaths(conceptId);
  }

  @RequestMapping(method = GET, value = "concepts/{conceptId}/partOf")
  @ResponseBody
  public JsonArray getConceptPartOfPaths(@PathVariable("conceptId") String conceptId) {
    return jsonConceptGraphService.getConceptPartOfPaths(conceptId);
  }

}
