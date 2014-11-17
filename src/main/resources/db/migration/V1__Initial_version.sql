
CREATE TABLE ${schema}.property (
    id varchar(255) PRIMARY KEY NOT NULL
);

CREATE TABLE ${schema}.lang (
    id varchar(2) PRIMARY KEY NOT NULL
);

CREATE TABLE ${schema}.scheme (
    id varchar(255) PRIMARY KEY NOT NULL
);

CREATE TABLE ${schema}.scheme_properties (
    subject_id varchar(255) NOT NULL REFERENCES ${schema}.scheme(id),
    property_id varchar(255) NOT NULL REFERENCES ${schema}.property(id),
    lang varchar(2) NOT NULL REFERENCES ${schema}.lang(id),
    value varchar(10000) NOT NULL
);

CREATE TABLE ${schema}.concept (
    id varchar(255) PRIMARY KEY NOT NULL,
    scheme_id varchar(255) NOT NULL REFERENCES ${schema}.scheme(id),
    broader_id varchar(255) REFERENCES ${schema}.concept(id)
);

CREATE TABLE ${schema}.concept_related (
    concept_id varchar(255) REFERENCES ${schema}.concept(id),
    related_id varchar(255) REFERENCES ${schema}.concept(id)
);

CREATE TABLE ${schema}.concept_properties (
    subject_id varchar(255) NOT NULL REFERENCES ${schema}.concept(id),
    property_id varchar(255) NOT NULL REFERENCES ${schema}.property(id),
    lang varchar(2) NOT NULL REFERENCES ${schema}.lang(id),
    value varchar(10000) NOT NULL
);

CREATE TABLE ${schema}.collection (
    id varchar(255) PRIMARY KEY NOT NULL,
    scheme_id varchar(255) NOT NULL REFERENCES ${schema}.scheme(id)
);

CREATE TABLE ${schema}.collection_concept (
    collection_id varchar(255) REFERENCES ${schema}.collection(id),
    concept_id varchar(255) REFERENCES ${schema}.concept(id)
);

CREATE TABLE ${schema}.collection_properties (
    subject_id varchar(255) NOT NULL REFERENCES ${schema}.collection(id),
    property_id varchar(255) NOT NULL REFERENCES ${schema}.property(id),
    lang varchar(2) NOT NULL REFERENCES ${schema}.lang(id),
    value varchar(10000) NOT NULL
);
