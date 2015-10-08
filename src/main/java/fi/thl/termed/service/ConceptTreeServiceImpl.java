package fi.thl.termed.service;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import fi.thl.termed.model.Concept;
import fi.thl.termed.model.JsTree;
import fi.thl.termed.model.LazyConceptTree;
import fi.thl.termed.util.ConceptGraphUtils;
import fi.thl.termed.util.ConceptReferenceFunctions;
import fi.thl.termed.util.GetResourceId;
import fi.thl.termed.util.ListUtils;
import fi.thl.termed.util.ToJsTreeFunction;

@Service
@Transactional
public class ConceptTreeServiceImpl implements ConceptTreeService {

  @Autowired
  private CrudService crudService;

  private Function<Concept, List<Concept>> fastNarrowerFunction =
      new Function<Concept, List<Concept>>() {
        @Override
        public List<Concept> apply(Concept concept) {
          return crudService.query(Concept.class, "broader.id:" + concept.getId(), 0, -1, null);
        }
      };

  private Function<Concept, LazyConceptTree> toLazyConceptNarrowerTree =
      new Function<Concept, LazyConceptTree>() {
        @Override
        public LazyConceptTree apply(Concept concept) {
          return new LazyConceptTree(concept, fastNarrowerFunction);
        }
      };

  @Override
  public List<LazyConceptTree> getConceptNarrowerTree(Concept concept) {
    List<Concept> roots =
        ConceptGraphUtils.findRoots(concept, ConceptReferenceFunctions.getBroaderFunction);
    return Lists.transform(roots, toLazyConceptNarrowerTree);
  }

  @Override
  public List<JsTree> getConceptNarrowerJsTree(Concept concept) {
    List<List<Concept>> paths =
        ConceptGraphUtils.collectPaths(concept, ConceptReferenceFunctions.getBroaderFunction);

    List<Concept> roots = ConceptGraphUtils.findRoots(paths);
    Set<Concept> opened = Sets.newHashSet(ListUtils.flatten(paths));

    List<LazyConceptTree> narrowerTree = Lists.transform(roots, toLazyConceptNarrowerTree);

    return Lists.transform(narrowerTree,
                           new ToJsTreeFunction(
                               Sets.newHashSet(Iterables.transform(opened, new GetResourceId())),
                               concept.getId()));
  }

}
